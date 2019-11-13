package bjsxt.zookeeper.base;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * Zookeeper base学习笔记
 *
 * @since 2015-6-13
 */
public class ZookeeperBase {

    /**
     * zookeeper地址
     */
    static final String CONNECT_ADDR = "192.168.0.111:2181,192.168.0.112:2181,192.168.0.113:2181";
    /**
     * session超时时间
     */
    static final int SESSION_OUTTIME = 2000;//ms
    /**
     * 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号
     */
    static final CountDownLatch connectedSemaphore = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        //是异步的
        ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher() {
            @Override
            //观察者
            public void process(WatchedEvent event) {
                //获取事件的状态
                KeeperState keeperState = event.getState();
                //获取事件的类型
                EventType eventType = event.getType();
                //如果是建立连接
                if (KeeperState.SyncConnected == keeperState) {
                    if (EventType.None == eventType) {
                        //如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
                        connectedSemaphore.countDown();
                        System.out.println("zk 建立连接");
                    }
                }
            }
        });

        //进行阻塞
        connectedSemaphore.await();

        System.out.println("执行啦..");
        //临时节点保持zk本地会话有效 可以做分布式锁
        //创建父节点 第三个参数安全认证不用关心
        //zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //不允许递归创建 先有父再有子
        //创建子节点 顺序节点 持久顺序节点
        //zk.create("/testRoot/children", "children data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        //分布式锁，就是多个客户端对一个数据进行修改 先get 有就不动 没有就创建 去修改数据 昨做完就去掉这个节点zk.close()
        //同一时间点，zk不可能有俩份数据 jmater压力测试 hession kryo java序列化框架
        //异步创建 多来个参数


        //获取节点洗信息
        byte[] data = zk.getData("/testRoot", false, null);
        System.out.println(new String(data));
        //返回的是list 元素为path 可以继续调getData
        System.out.println(zk.getChildren("/testRoot", false).get(0));

        //修改节点的值
//		zk.setData("/testRoot", "modify data root".getBytes(), -1);
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));		

        //判断节点是否存在
//		System.out.println(zk.exists("/testRoot/children", false));
        //删除节点 版本号dataversion -1 全清空
        zk.delete("/testRoot/children", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int i, String s, Object o) {
                //响应码 0表示成功 -4端口了解 -110节点存在 -112会话过期
                System.out.println(i);
                //节点path
                System.out.println(s);
                //传入的ctx值
                System.out.println(o);
            }
        }, "a");
        //不存在 返回null
//		System.out.println(zk.exists("/testRoot/children", false));

        zk.close();


    }

}
