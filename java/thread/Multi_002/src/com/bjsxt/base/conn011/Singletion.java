package com.bjsxt.base.conn011;

public class Singletion {
	//静态内部类天然的线程安全
	private static class InnerSingletion {
		private static Singletion single = new Singletion();
	}
	
	public static Singletion getInstance(){
		return InnerSingletion.single;
	}
	
}
