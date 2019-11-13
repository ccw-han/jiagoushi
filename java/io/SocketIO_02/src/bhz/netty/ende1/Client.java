package bhz.netty.ende1;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Client {
    //netty简单，编码简单 api强大 优化简单 性能也好 可扩展可定制
    //长连接 游戏 滴滴打车 健壮 openfire一体化技术
    //企业级开发就是快，互联网行业竞争激烈 一俩年就还新技术
    //基于nio2.0 解耦业务逻辑 所有都是异步非阻塞的
    public static void main(String[] args) throws Exception {

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) throws Exception {
                        //和server相同原理 一模一样
                        ByteBuf buf = Unpooled.copiedBuffer("$_".getBytes());
                        sc.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, buf));
                        sc.pipeline().addLast(new StringDecoder());
                        sc.pipeline().addLast(new ClientHandler());
                    }
                });

        ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();

        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("bbbb$_".getBytes()));
        cf.channel().writeAndFlush(Unpooled.wrappedBuffer("cccc$_".getBytes()));


        //等待客户端端口关闭
        cf.channel().closeFuture().sync();
        group.shutdownGracefully();

    }
}
