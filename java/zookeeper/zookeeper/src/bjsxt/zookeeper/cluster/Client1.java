package bjsxt.zookeeper.cluster;

import bjsxt.zookeeper.cluster.ZKWatcher;

public class Client1 {
	// c1 c2 连接zk test 也去连，test节点做主管理，c1 c2 进行监听 zk改了，c1 c2 都监听到了
	public static void main(String[] args) throws Exception{
		
		ZKWatcher myWatcher = new ZKWatcher();
		Thread.sleep(100000000);
	}
}
