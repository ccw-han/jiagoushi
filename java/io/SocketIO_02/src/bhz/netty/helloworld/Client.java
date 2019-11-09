package bhz.netty.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
	/*
	* 1创建俩个线程组 一个网络事件 就是客户端连接 一个是网络读写
	* 2创建一个serverbootstrap对象 配置netty参数 比如缓存大小
	* 3实际处理数据的类 channelinitializer初始化工作 设置字符集之类
	* 4绑定端口 同步阻塞服务器启动即可
	*
	*
	* */
    public static void main(String[] args) throws Exception {
		//主要就是解耦 业务就自己决定就可以了
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture cf1 = b.connect("127.0.0.1", 8765).sync();
        //ChannelFuture cf2 = b.connect("127.0.0.1", 8764).sync();
        //发送消息
        Thread.sleep(1000);
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("777".getBytes()));
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("666".getBytes()));
        //cf2.channel().writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
        Thread.sleep(2000);
        cf1.channel().writeAndFlush(Unpooled.copiedBuffer("888".getBytes()));
        //cf2.channel().writeAndFlush(Unpooled.copiedBuffer("666".getBytes()));

        cf1.channel().closeFuture().sync();
        //cf2.channel().closeFuture().sync();
        group.shutdownGracefully();


    }
}
