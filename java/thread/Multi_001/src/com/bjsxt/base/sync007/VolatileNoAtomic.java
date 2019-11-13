package com.bjsxt.base.sync007;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * volatile关键字不具备synchronized关键字的原子性（同步）
 * @author alienware
 *	变量在多个线程间可见
 * 每个线程有自己的工作区，对内存的拷贝
 * volatile只是可见不能保证原子性 轻量级的synchronized 性能很高
 *
 */
public class VolatileNoAtomic extends Thread{
	//private static volatile int count;
	private static AtomicInteger count = new AtomicInteger(0);
	private static void addCount(){
		for (int i = 0; i < 1000; i++) {
			//count++ ;
			count.incrementAndGet();
		}
		System.out.println(count);
	}
	
	public void run(){
		addCount();
	}
	
	public static void main(String[] args) {
		
		VolatileNoAtomic[] arr = new VolatileNoAtomic[100];
		for (int i = 0; i < 10; i++) {
			arr[i] = new VolatileNoAtomic();
		}
		
		for (int i = 0; i < 10; i++) {
			arr[i].start();
		}
	}
	
	
	
	
}
