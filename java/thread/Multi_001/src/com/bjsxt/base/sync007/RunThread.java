package com.bjsxt.base.sync007;

public class RunThread extends Thread{

	private volatile boolean isRunning = true;
	private void setRunning(boolean isRunning){
		this.isRunning = isRunning;
	}
	
	public void run(){
		System.out.println("进入run方法..");
		int i = 0;
		while(isRunning == true){
			//..
		}
		System.out.println("线程停止");
	}
	
	public static void main(String[] args) throws InterruptedException {
		RunThread rt = new RunThread();
		rt.start();
		Thread.sleep(1000);
		//主内存和线程空间都一致 主线程设置后，另外线程立马能读到
		//强制线程去主内存去读取变量而不是自己的内存区域
		rt.setRunning(false);

		System.out.println("isRunning的值已经被设置了false");
	}
	
	
}
