package bhz.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {



    //complete收到数据完成后怎么做
	@Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("server channel active... ");
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
	    //netty封装好的buff
        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, "utf-8");
        System.out.println("Server :" + body);
        String response = "进行返回给客户端的响应：" + body;
        //写数据只能写缓冲类型数据，所以直接有工具类直接转为字节buff
        //不用释放，因为写操作有释放操作 也可以先write 最后flush flush才会去发给服务器端
        ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
        //加一个listener监听器，发完直接就关闭了 异步断开 可以做短连接
        //.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
            throws Exception {
        System.out.println("读完了");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
            throws Exception {
        ctx.close();
    }

}
