package com.bjsxt.base.conn008;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * wait notfiy 方法，wait释放锁，notfiy不释放锁
 *
 * @author alienware
 */
public class ListAdd2 {
    private volatile static List list = new ArrayList();

    public void add() {
        list.add("bjsxt");
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {

        final ListAdd2 list2 = new ListAdd2();

        // 1 实例化出来一个 lock
        // 当使用wait 和 notify 的时候 ， 一定要配合着synchronized关键字去使用
        //唤醒时，wait线程从唤醒的时候去执行。不然无法解释为何会抛异常
        final Object lock = new Object();
		//就是这个对象 await等待了，countDown() 变成0直接往下走
		//一个人先等着，等着我喊着口号到0时，都给我执行 这个对象和那些锁没任何关系
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (lock) {
                        for (int i = 0; i < 10; i++) {
                            list2.add();
                            System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素..");
                            Thread.sleep(500);
                            if (list2.size() == 5) {
                                System.out.println("已经发出通知..");
//                                countDownLatch.countDown();
                                lock.notify();
//                                lock.wait();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, "t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    System.out.println("t2" + list2.size());
                    if (list2.size() != 5) {
                        try {
                            System.out.println("t2进入...");
                            lock.wait();
//                        countDownLatch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("当前线程：" + Thread.currentThread().getName() + "收到通知线程停止..");
                    throw new RuntimeException(list2.size() + "");
                }
            }
        }, "t2");

        t2.start();
        t1.start();

    }

}
