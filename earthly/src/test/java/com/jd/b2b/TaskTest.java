package com.jd.b2b;

import com.jd.b2b.task.ShardingTask;
import com.jd.b2b.task.TaskControl;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cdtangxi
 * @Date 2017/3/10 15:52
 */
public class TaskTest{

    @Test
    public void testTaskControl(){
        Map<String, ShardingTask> shardingTaskMap = new HashMap<String, ShardingTask>();
        String module = "testMyTask";
        Map<String,Object> params = new HashMap<String, Object>();
        params.put("222",2222);
        shardingTaskMap.put(module,new MyTestTask());
        TaskControl taskControl = new TaskControl();
        taskControl.setShardingTaskMap(shardingTaskMap);
        taskControl.excute(module,params,"1","2","3");
    }
}
