package com.bjsxt.base.coll013;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class UseConcurrentMap {
	/*
	* ConcurrentMap实现由concurrnthashmap和concurrentskiplistmapzheb
	* listmap 有排序功能
	* 原理内部使用segment 每个段就是小的hashtable分成16个段
	* 方案1：减小锁的粒度，降低锁的竞争 大量运用volatile
	* 哪一个段不可控
	* */
    public static void main(String[] args) {
        ConcurrentHashMap<String, Object> chm = new ConcurrentHashMap<String, Object>();
        chm.put("k1", "v1");
        chm.put("k2", "v2");
        chm.put("k3", "v3");
        chm.putIfAbsent("k4", "vvvv");
        //System.out.println(chm.get("k2"));
        //System.out.println(chm.size());

        for (Map.Entry<String, Object> me : chm.entrySet()) {
            System.out.println("key:" + me.getKey() + ",value:" + me.getValue());
        }


    }
}
