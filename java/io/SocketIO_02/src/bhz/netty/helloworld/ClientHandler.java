package bhz.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public class ClientHandler extends ChannelHandlerAdapter {

    //jboss序列化框架 mina不考虑 nettyall jar包需要 只需要这个包就足够了
    @Override
    //生命周期
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            //netty给封装了
            ByteBuf buf = (ByteBuf) msg;

            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);

            String body = new String(req, "utf-8");
            System.out.println("Client :" + body);
            String response = "收到服务器端的返回信息：" + body;
        } finally {
            //显示去释放，不然缓存会释放，不然长时间缓存会爆掉
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }

}
