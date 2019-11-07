package bhz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class Client {

    //需要一个Selector 如果需要读操作，需要和服务端一样写法
    //问题是边界，什么时候算长度
    //nio很复杂
    public static void main(String[] args) {

        //创建连接的地址
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8765);

        //声明连接通道 建立一个管道 双向的 读 写 或者同时进行
        //有多种状态 accept connect readable writeable
        SocketChannel sc = null;

        //建立缓冲区 数据存到缓冲区的，不是通过网络传输的 本质是字节数组
        //有java基本LongBuffer
        ByteBuffer buf = ByteBuffer.allocate(1024);

        try {
            //打开通道
            sc = SocketChannel.open();
            //进行连接
            sc.connect(address);

            while (true) {
                //定义一个字节数组，然后使用系统录入功能：
                byte[] bytes = new byte[1024];
                System.in.read(bytes);

                //把数据放到缓冲区中 缓冲区好了以后才去读取
                buf.put(bytes);
                //对缓冲区进行复位
                buf.flip();
                //写出数据
                sc.write(buf);
                //清空缓冲区数据
                buf.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sc != null) {
                try {
                    sc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}
