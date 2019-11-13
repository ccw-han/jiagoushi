package com.bjsxt.height.concurrent018;

import java.net.HttpURLConnection;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

public class MyRejected implements RejectedExecutionHandler {


    public MyRejected() {
    }

    @Override
    //有任务被拒绝 和阻塞队列很有关系 拒绝策略有很多种
	//策略 1 直接抛出异常阻止系统工作 2 只要线程池未关闭，该策略
	//直接在调用者线程中，运行当前被丢弃的任务
	//3 丢弃最老的请求，尝试再次提交当前任务4 丢弃无法锤
	//的任务 不给与任何处理
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("自定义处理..");
        System.out.println("当前被拒绝任务为：" + r.toString());


    }

}
