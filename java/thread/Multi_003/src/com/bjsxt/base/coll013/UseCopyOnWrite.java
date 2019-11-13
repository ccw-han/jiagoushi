package com.bjsxt.base.coll013;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class UseCopyOnWrite {
	/*
	* 在写的时候进行copy
	* add时，复制出一个，在新的里面添加
	* 可以大大提高并发读，而不需要加锁，读写分离机制
	* 适应于读多写少，反过来怎么办，那就不要用了
	* */
    public static void main(String[] args) {

        CopyOnWriteArrayList<String> cwal = new CopyOnWriteArrayList<String>();
        CopyOnWriteArraySet<String> cwas = new CopyOnWriteArraySet<String>();
		//add方法还是有锁的，自己添加的时候只能自己添加

    }
}
