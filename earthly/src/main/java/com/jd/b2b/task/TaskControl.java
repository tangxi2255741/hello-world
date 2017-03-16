package com.jd.b2b.task;

import com.jd.b2b.domain.TaskResult;
import com.jd.b2b.exception.JobException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author cdtangxi
 * @Date 2017/3/9 19:07
 */
public class TaskControl{
    private static final Logger LOGGER = LogManager.getLogger(TaskActuator.class);
    private static final int DEFAULT_THREADS = 5;
    /**
     * 最大并发线程数
     */
    private int maxThreadNum = DEFAULT_THREADS;
    /** 任务配置*/
    private Map<String,ShardingTask> shardingTaskMap;

    private ExecutorService pool;

    public void excute(String module,Map<String,Object> params,String... shardIds){
        LOGGER.info("module={},shardIds={}",module, Arrays.toString(shardIds));
        if(StringUtils.isBlank(module)){
            throw new JobException("module不能为空！");
        }
        if(shardIds == null || shardIds.length == 0){
            LOGGER.error("shardIds is emtry.");
            return;
        }
        // 通过module获取对应的任务
        ShardingTask shardingTask = shardingTaskMap.get(module);
        if(shardingTask == null){
            throw new JobException("找不到对应的ShardingTask.[module="+module+"]");
        }
        // 一个分片一个线程
        List<Future<TaskResult>> futureList = new ArrayList<Future<TaskResult>>(shardIds.length);
        if(pool == null){
            pool = Executors.newFixedThreadPool(maxThreadNum);
        }
        for(final String shardId : shardIds){
            TaskActuator task = new TaskActuator(shardingTask,params,Integer.parseInt(shardId),shardIds.length);
            FutureTask<TaskResult> listFutureTask = new FutureTask<TaskResult>(task);
            pool.submit(listFutureTask);
            futureList.add(listFutureTask);
        }
        printResult(module,futureList);
    }

    private void printResult(String taskName, List<Future<TaskResult>> futureList){
        int difCount = 0;
        int dealCount = 0;
        int totalCount = 0;
        for (Future<TaskResult> fs : futureList) {
            try {
                TaskResult taskResult = fs.get();
                totalCount += taskResult.getTotalCount();
                difCount += taskResult.getErrorCount();
                dealCount += taskResult.getDealCount();
            } catch (Exception ex) {
                LOGGER.error("线程异常",ex);
            }
        }
        LOGGER.info(taskName + "总共有：{}条记录，有：{}个不一致，正常处理了：{}条记录",totalCount,difCount,dealCount);
    }

    public Map<String, ShardingTask> getShardingTaskMap() {
        return shardingTaskMap;
    }

    public void setShardingTaskMap(Map<String, ShardingTask> shardingTaskMap) {
        this.shardingTaskMap = shardingTaskMap;
    }
}
