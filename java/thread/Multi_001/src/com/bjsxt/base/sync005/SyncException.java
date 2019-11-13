package com.bjsxt.base.sync005;
/**
 * synchronized异常
 * @author alienware
 *异常会释放锁的情况
 */
public class SyncException {

	private int i = 0;
	public synchronized void operation(){
		while(true){
			try {
				i++;
				Thread.sleep(100);
				System.out.println(Thread.currentThread().getName() + " , i = " + i);
				if(i == 20){
					//Integer.parseInt("a");
					throw new RuntimeException();
				}
			} catch (InterruptedException e) {
				//这边锁会释放，防止线程安全问题，需要业务支持
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		
		final SyncException se = new SyncException();
		Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				se.operation();
			}
		},"t1");
		t1.start();
	}
	
	
}
