package com.jd.b2b.task;

import java.util.List;
import java.util.Map;

/**
 * @author cdtangxi
 * @Date 2017/1/11 16:16
 */
public interface ShardingTask<T> {

    /**
     * 需要处理的数据总量；只是用总进度预估使用的；
     *  <0(小于0) :表示不需要预先知道有多少总量；
     *  =0(等于0) :表示当前任务没有数据需要执行；
     * @return
     */
    int count(int shardId, int mod, Map<String, Object> params);
    /***
     * 查询数据
     * @param shardId
     * @param last 最后处理到一行数据,当page为1时，last为空
     * @return 如果返回List为空（list== null || list.size == 0）时，则表示任务已经处理完成；
     */
    List<T> fetch(int shardId, int mod, Map<String, Object> params, T last);

    /**
     * 处理一条数据
     */
    void handle(T vo);

    List<T> batchHandle(List<T> voList);

    /**
     * 进度提示信息，可以为空；如果返回空，则自动输出rowNum+vo.toString()
     * @param count
     * @param rowNum
     * @param vo
     * @return
     */
    String format(int count, int rowNum, T vo);

}
