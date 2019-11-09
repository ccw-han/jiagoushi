package bhz.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ExecutionException;

public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {

	@Override
	public void completed(AsynchronousSocketChannel asc, Server attachment) {
		//当有下一个客户端接入的时候 直接调用Server的accept方法，
		// 这样反复执行下去，保证多个客户端都可以阻塞
		//有点递归的意思 接力
		attachment.assc.accept(attachment, this);
		read(asc);//发数据和写数据都是异步化的
		//nio是写入缓冲区然后准备然后通知cpu去读
		//aio是写入缓冲区的时候，开启了一个线程去异步的读缓冲区的数据
	}

	private void read(final AsynchronousSocketChannel asc) {
		//读取数据
		ByteBuffer buf = ByteBuffer.allocate(1024);
		//这边是异步的读，不影响其他代码执行 第一个是目的地，第二个是读的数据
		asc.read(buf, buf, new CompletionHandler<Integer, ByteBuffer>() {
			@Override
			public void completed(Integer resultSize, ByteBuffer attachment) {
				//进行读取之后,重置标识位
				attachment.flip();
				//获得读取的字节数
				System.out.println("Server -> " + "收到客户端的数据长度为:" + resultSize);
				//获取读取的数据
				String resultData = new String(attachment.array()).trim();
				System.out.println("Server -> " + "收到客户端的数据信息为:" + resultData);
				String response = "服务器响应, 收到了客户端发来的数据: " + resultData;
				write(asc, response);
			}
			@Override
			public void failed(Throwable exc, ByteBuffer attachment) {
				exc.printStackTrace();
			}
		});
	}
	
	private void write(AsynchronousSocketChannel asc, String response) {
		try {
			ByteBuffer buf = ByteBuffer.allocate(1024);
			buf.put(response.getBytes());
			buf.flip();
			//write 返回的是future模式 并行的一起去写 内部是多线程
			asc.write(buf).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void failed(Throwable exc, Server attachment) {
		exc.printStackTrace();
	}

}
