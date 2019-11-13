package com.bjsxt.height.design015;

import java.util.Random;

public class Main {
	/*
	* 俩个进程
	* master负责接收和分配任务
	* worker负责处理子任务
	* 处理结束返回给master master做总结
	* 大任务分成多个小任务 并行执行
	*
	* */
    public static void main(String[] args) {

        Master master = new Master(new Worker(), 20);

        Random r = new Random();
        for (int i = 1; i <= 100; i++) {
            Task t = new Task();
            t.setId(i);
            t.setPrice(r.nextInt(1000));
            master.submit(t);
        }
        master.execute();
        long start = System.currentTimeMillis();

        while (true) {
            if (master.isComplete()) {
                long end = System.currentTimeMillis() - start;
                int priceResult = master.getResult();
                System.out.println("最终结果：" + priceResult + ", 执行时间：" + end);
                break;
            }
        }

    }
}
