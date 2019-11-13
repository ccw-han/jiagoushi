package com.bjsxt.base.conn010;

public class ConnThreadLocal {
    //线程局部变量变量在我线程中有效，不用加锁，空间换时间手法
    public static ThreadLocal<String> th = new ThreadLocal<String>();

    public void setTh(String value) {
        th.set(value);
    }

    public void getTh() {
        System.out.println(Thread.currentThread().getName() + ":" + this.th.get());
    }

    public static void main(String[] args) throws InterruptedException {

        final ConnThreadLocal ct = new ConnThreadLocal();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ct.setTh("张三");
                ct.getTh();
            }
        }, "t1");

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    ct.getTh();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2");
		//只有线程内是有效的

        t1.start();
        t2.start();
    }

}
