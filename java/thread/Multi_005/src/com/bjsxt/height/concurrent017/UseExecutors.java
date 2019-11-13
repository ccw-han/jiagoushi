package com.bjsxt.height.concurrent017;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
* 固定线程池 因为无界队列会很大的时候，处理不过来，内存会溢出
* single一个线程的线程池，绝对线程安全的 就一个线程
* cached线程池，初始化没线程，无限线程会去处理，队列为同步队列，有一个任务就创建一个线程去处理掉
* 延迟线程池，使用delay队列
* */
public class UseExecutors {

    public static void main(String[] args) {

        //ExecutorService pool = Executors.newSingleThreadExecutor()

        //cache fixed single


    }
}
