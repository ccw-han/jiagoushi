package com.bjsxt.base.conn008;

import java.util.ArrayList;
import java.util.List;

public class ListAdd1 {

    //线程和主内存同步在同步代码块执行结束，释放锁的时候进行同步，否则是线程执行结束开始执行
   //有个延迟，反正是发出通知了，拿着size==5在那等着呢，一旦释放锁，然后就直接执行
    private static List list = new ArrayList();

    public void add() {
        list.add("bjsxt");
    }

    public int size() {
        return list.size();
    }

    public static void main(String[] args) {

        final ListAdd1 list1 = new ListAdd1();

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        list1.add();
                        System.out.println("当前线程：" + Thread.currentThread().getName() + "添加了一个元素..");
                        Thread.sleep(500);
                    }
                    if (list1.size() == 10) {
                        System.out.println("zhixingjieshu" + list1.size());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                //这边加个打印代码就执行异常，不加不异常，不知道为何
                while (true) {
                    System.out.println("t2 size" + list1.size());
                    if (list1.size() == 5) {
                        System.out.println("当前线程收到通知：" + Thread.currentThread().getName() + " list size = 10 线程停止..");
                        throw new RuntimeException();
                    }
                }
            }
        }, "t2");

        t1.start();
        t2.start();
    }


}
