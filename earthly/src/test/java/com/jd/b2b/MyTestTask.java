package com.jd.b2b;

import com.jd.b2b.task.ShardingTask;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author cdtangxi
 * @Date 2017/3/10 16:57
 */
public class MyTestTask implements ShardingTask<MyObject>{
    private static final Logger LOGGER = LogManager.getLogger(MyTestTask.class);

    public int count(int shardId, int mod, Map params) {
        LOGGER.info("shardId = {},mod = {},params = {}",shardId,mod,params);
        return 1000;
    }

    public List<MyObject> fetch(int shardId, int mod, Map params, MyObject last) {
        LOGGER.info("shardId = {},mod = {},params = {},last = {}",shardId,mod,params);
        List<MyObject> list = new ArrayList<MyObject>();
        Long startNum = 0l;
        if(last != null){
            startNum = last.getId();
            if(last.getId() != null && last.getId() >= (count(shardId,mod,params) / mod)){
                return null;
            }
        }
        for(int count = 0;count<100;count++){
            MyObject myObject = new MyObject();
            myObject.setId(startNum);
            myObject.setName("startNum = " + startNum);
            startNum ++;
            list.add(myObject);
        }
        return list;
    }

    public void handle(MyObject vo) {

    }

    public List<MyObject> batchHandle(List<MyObject> voList) {
        LOGGER.info("本次处理：{}条记录",voList.size());
        if(CollectionUtils.isEmpty(voList)){
            return null;
        }
        List<MyObject> myObjects = new ArrayList<MyObject>();
        for(MyObject myObject : voList){
            if(myObject.getId() %10 ==0){
                myObjects.add(myObject);
            }
        }
        return myObjects;
    }

    public String format(int count, int rowNum, MyObject vo) {
        return "当前进度：" + rowNum +"/" + count + ",当前id = " + vo.getId();
    }
}
