package com.bjsxt.height.design014;

public class Main {
	/*
	* 业务没关系可以采用多线程，如果有关系的业务还得串行化
	* 各种优化
	* */
    public static void main(String[] args) throws InterruptedException {

        FutureClient fc = new FutureClient();
        Data data = fc.request("请求参数");
        System.out.println("请求发送成功!");
        System.out.println("做其他的事情...");

        String result = data.getRequest();
        System.out.println(result);

    }
}
