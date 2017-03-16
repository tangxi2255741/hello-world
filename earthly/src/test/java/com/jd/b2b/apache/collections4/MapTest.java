package com.jd.b2b.apache.collections4;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.SingletonMap;
import org.junit.Test;

/**
 * @author cdtangxi
 * @Date 2017/3/15 17:23
 */
public class MapTest {

    @Test
    public void test(){
        SingletonMap singletonMap = new SingletonMap("test","test1");
        MapUtils.emptyIfNull();
    }
}
