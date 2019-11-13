package com.bjsxt.base.coll012;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * jdk1.5之前的同步手段 但是会有并发修改异常
 * 串行化的，性能低，降低吞吐量
 * 通过并发类容器大大的提高了并发量
 * 多线程使用Vector或者HashTable的示例（简单线程同步问题）
 *同步类容器都是安全的 独立的操作是安全的，整个业务得加锁
 * 迭代的同时，有其他线程在这个过程中进行remove 就会有异常，并发修改异常
 * @author alienware
 */
public class Tickets {

    public static void main(String[] args) {
        //初始化火车票池并添加火车票:避免线程同步可采用Vector替代ArrayList  HashTable替代HashMap

        final Vector<String> tickets = new Vector<String>();
        //这边是封装
        //Map<String, String> map = Collections.synchronizedMap(new HashMap<String, String>());

        for (int i = 1; i <= 1000; i++) {
            tickets.add("火车票" + i);
        }

//		for (Iterator iterator = tickets.iterator(); iterator.hasNext();) {
//			String string = (String) iterator.next();
//			tickets.remove(20);
//		}

        for (int i = 1; i <= 10; i++) {
            new Thread("线程" + i) {
                public void run() {
                    while (true) {
                        if (tickets.isEmpty()) break;
                        System.out.println(Thread.currentThread().getName() + "---" + tickets.remove(0));
                    }
                }
            }.start();
        }
    }
}
