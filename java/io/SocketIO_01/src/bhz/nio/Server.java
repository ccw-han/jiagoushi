package bhz.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server implements Runnable {
    //原始用tcp注册到服务器 现在是注册到selector上 轮询注册的通道，状态来执行操作
    //本质上是减少3次握手操作，减少连接的开销
    //1 多路复用器（管理所有的通道）
    private Selector seletor;
    //2 建立缓冲区
    private ByteBuffer readBuf = ByteBuffer.allocate(1024);
    //3
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

    public Server(int port) {
        try {
            //1 打开路复用器
            this.seletor = Selector.open();
            //2 打开服务器通道
            ServerSocketChannel ssc = ServerSocketChannel.open();
            //3 设置服务器通道为非阻塞模式
            ssc.configureBlocking(false);
            //4 绑定地址
            ssc.bind(new InetSocketAddress(port));
            //5 把服务器通道注册到多路复用器上，并且监听阻塞事件 服务端的通道在这等着
            //这边只是一个标识位
            ssc.register(this.seletor, SelectionKey.OP_ACCEPT);

            System.out.println("Server start, port :" + port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                //1 必须要让多路复用器开始监听
                this.seletor.select();
                //2 返回多路复用器已经选择的结果集
                Iterator<SelectionKey> keys = this.seletor.selectedKeys().iterator();
                //3 进行遍历
                while (keys.hasNext()) {
                    //4 获取一个选择的元素
                    SelectionKey key = keys.next();
                    //5 直接从容器中移除就可以了
                    keys.remove();
                    //6 如果是有效的 选出你需要的状态然后去操作
                    //程序员自己完成状态（非业务不应该关心的） 自己去写处理操作
                    //理论上可以负责不限制的，获得连接句柄没有限制，成千上万 使用epoll 一个链接成千上万
                    //轮询数据已经准备好，通知cpu读取或者写入 写入用户进程还是阻塞
                    //之前是连接好去读取，如果网络不行，就阻塞
                    //现在是好的，已经写入好了才去通知cpu读写
                    //key 找到管道，然后管道读写数据，写入到数据缓冲区
                    //可以一直走肯定是非阻塞的 连接是有效的
                    if (key.isValid()) {
                        //7 如果为阻塞状态
                        if (key.isAcceptable()) {
                            this.accept(key);
                        }
                        //8 如果为可读状态
                        if (key.isReadable()) {
                            this.read(key);
                        }
                        //9 写数据
                        if (key.isWritable()) {
                            //this.write(key); //ssc
                        }
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void write(SelectionKey key) {
        //ServerSocketChannel ssc =  (ServerSocketChannel) key.channel();
        //ssc.register(this.seletor, SelectionKey.OP_WRITE);
    }

    private void read(SelectionKey key) {
        try {
            //1 清空缓冲区旧的数据
            this.readBuf.clear();
            //2 获取之前注册的socket通道对象
            SocketChannel sc = (SocketChannel) key.channel();
            //3 读取数据到buff
            int count = sc.read(this.readBuf);
            //4 如果没有数据
            if (count == -1) {
                key.channel().close();
                key.cancel();
                return;
            }
            //5 有数据则进行读取 读取之前需要进行复位方法(把position 和limit进行复位)
            this.readBuf.flip();
            //6 根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
            byte[] bytes = new byte[this.readBuf.remaining()];
            //7 接收缓冲区数据 放入数组中
            this.readBuf.get(bytes);
            //8 打印结果
            String body = new String(bytes).trim();
            System.out.println("Server : " + body);

            // 9..可以写回给客户端数据

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void accept(SelectionKey key) {
        try {
            //1 获取服务通道
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            //2 执行阻塞方法 真正接收数据的时候客户端的channel 和bio是一样的，延后做了
            SocketChannel sc = ssc.accept();
            //3 设置阻塞模式
            sc.configureBlocking(false);
            //4 注册到多路复用器上，并设置读取标识
            sc.register(this.seletor, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //一个线程进行所有操作，或者少量的线程去操作所有请求 一个请求一个线程
        new Thread(new Server(8765)).start();
        ;
    }


}
