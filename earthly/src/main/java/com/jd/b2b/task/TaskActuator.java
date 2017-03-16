package com.jd.b2b.task;

import com.jd.b2b.domain.TaskResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author cdtangxi
 * @Date 2017/1/11 16:14
 */
public class TaskActuator<T> implements Callable {

    private static final Logger LOGGER = LogManager.getLogger(TaskActuator.class);
    private ShardingTask<T> shardingTask;
    private Map<String,Object> params;
    private int shardId;
    private int threadNum;

    public TaskActuator(ShardingTask<T> shardingTask, Map<String, Object> params, int shardId, int threadNum) {
        this.shardingTask = shardingTask;
        this.params = params;
        this.shardId = shardId;
        this.threadNum = threadNum;
    }

    public TaskResult call() throws Exception {
        long st = System.currentTimeMillis();
        String tip;
        int rowNum = 0;
        int errorCount = 0;
        int count = 0;
        try {
            count = shardingTask.count(shardId,threadNum,params);
            LOGGER.info("需要执行的任务总量有：" + count);
            T last = null;
            List<T> list = shardingTask.fetch(shardId,threadNum,params,last);
            while (list != null && list.size() > 0) {
                List<T> errorList = shardingTask.batchHandle(list);
                if(CollectionUtils.isNotEmpty(errorList)){
                    errorCount += errorList.size();
                }
                int listSize = list.size();
                last = list.get(listSize - 1);
                rowNum += listSize;
                tip = shardingTask.format(count, rowNum, last);
                LOGGER.info(tip);
                list = shardingTask.fetch(shardId,threadNum,params, last);
            }
            tip = "任务执行完成！总共处理:" + rowNum + "行数据，总用时:" + (System.currentTimeMillis() - st) + "(ms)";
            LOGGER.info(tip);
        }catch (Exception e){
            LOGGER.error("shardId执行异常",e);
            tip = "shardId = " + shardId + "执行异常";
            return new TaskResult(tip,count,errorCount,rowNum,false);
        }
        LOGGER.info("shardId = {},总共有：{}条记录，有：{}个不一致，正常处理了：{}条记录",shardId,count,errorCount,rowNum);
        return new TaskResult(tip,count,errorCount,rowNum,true);
    }
}
