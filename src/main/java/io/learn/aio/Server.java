package io.learn.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-30 10:43
 */
public class Server {
    /**
     * AIO模型里，不需要像BIO那样，干等着别人连过来或者有数据可写可读的时候才继续进行，
     * 也不像NIO那样，虽然不阻塞，但也是要自己不停的轮询
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        //AIO可以把一些通道进行分组，每一组都可以设置备用线程池，当监听到事件发生的时候，就会把事件扔给线程池里，让他们自己去通知应用程序
        //AsynchronousChannelGroup.withCachedThreadPool(),第一个参数是线程池实例，第二个参数是线程池实例的个数，在这里相当于事件通知的时候，AIO可以往这两个线程池里扔通知，进行回调，增大吞吐量
        //这个线程池是用来得到操作系统的“IO事件通知”的，但是要被包装成AsynchronousChannelGroup
        //然后把AsynchronousChannelGroup扔到AsynchronousServerSocketChannel.open()方法里
        AsynchronousChannelGroup asynchronousChannelGroup = AsynchronousChannelGroup.withCachedThreadPool(Executors.newFixedThreadPool(10), 2);
        AsynchronousServerSocketChannel asynchronousServerSocketChannel = AsynchronousServerSocketChannel
                .open(asynchronousChannelGroup).bind(new InetSocketAddress("127.0.0.1", 8989));

        //调用accept后，main线程直接往下走，不会等在这
        //方法第一个参数：依附在此监听器上的附件，等事件触发的时候可以拿来用
        //方法第二个参数：事件触发时候，具体的逻辑，也就是操作系统相关资源准备好了，开始回调你的方法了
        asynchronousServerSocketChannel.accept(null, new AcceptHandler(asynchronousServerSocketChannel));


        System.out.println("证明是异步的");
        //因为主线程异步调用accept，所以不能让主线程结束，阻塞一下
        new CountDownLatch(1).await();
    }
}

/**
 * 专门处理AsynchronousServerSocketChannel的accept事件的回调
 */
class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, Object> {

    private AsynchronousServerSocketChannel asynchronousServerSocketChannel;

    public AcceptHandler(AsynchronousServerSocketChannel asynchronousServerSocketChannel) {
        this.asynchronousServerSocketChannel = asynchronousServerSocketChannel;
    }

    /**
     * 进入这个方法，证明已经和客户端建立好连接了,并且把客户端的socket已经封装成AsynchronousSocketChannel了，
     * 可以直接拿来用，attachment是你注册该监听器的时候传入的附件
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(AsynchronousSocketChannel result, Object attachment) {
        try {
            System.out.println(result.getRemoteAddress()+"已建立连接.....");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //每次都要重新注册监听(一次注册，一次响应)，但是由于“文件状态标示符”是独享的，所以不需要担心有“漏掉的”事件
        asynchronousServerSocketChannel.accept(null, this);


        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //在建立好连接的前提下
        //如果有数据可以读的时候，读完了，麻烦操作系统调用我的回调函数
        //读到我给你提供的ByteBuffer里
        result.read(byteBuffer, null, new ReadHandler(result, byteBuffer));
    }


    /**
     * 这个方法表示连接客户端的时候出现了异常，异常对象和附件会作为参数传进来
     *
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, Object attachment) {
        System.out.println("处理AsynchronousServerSocketChannel的accept事件的回调,发生异常！");
        exc.printStackTrace();
    }
}


/**
 * 专门处理AsynchronousSocketChannel的read事件回调
 */
class ReadHandler implements CompletionHandler<Integer, Object> {

    private AsynchronousSocketChannel asynchronousSocketChannel;

    private ByteBuffer byteBuffer;

    public ReadHandler(AsynchronousSocketChannel asynchronousSocketChannel, ByteBuffer byteBuffer) {
        this.asynchronousSocketChannel = asynchronousSocketChannel;
        this.byteBuffer = byteBuffer;
    }

    /**
     * 数据读好了，并且读到了当时提供的byteBuffer里，可以直接拿出来用，result的意思是，这次一共读到了多少字节数，如果是-1，表示无法再读取了
     *
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, Object attachment) {
        System.out.println("开始处理read事件回调");
        //如果result == -1，说明客户端主动终止了TCP套接字，这时服务端终止就可以了
        if (result == -1) {
            try {
                this.asynchronousSocketChannel.close();
                System.out.println("客户端已关闭");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        //从buffer里读出来
        this.byteBuffer.flip();
        byte[] contexts = new byte[result];
        this.byteBuffer.get(contexts, 0, result);
        this.byteBuffer.clear();
        System.out.println("接受到客户端的数据:" + new String(contexts, StandardCharsets.UTF_8));
        System.out.println("处理read事件回调完成");

        //还要继续监听(一次监听一次通知)
        this.asynchronousSocketChannel.read(this.byteBuffer, null, this);

        //发送数据
        this.asynchronousSocketChannel.write(ByteBuffer.wrap("我是服务端，啦啦啦啦".getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 读取的时候出现异常
     *
     * @param exc
     * @param attachment
     */
    @Override
    public void failed(Throwable exc, Object attachment) {
        System.out.println("处理AsynchronousSocketChannel的read事件回调,发生异常！");
        exc.printStackTrace();
        try {
            this.asynchronousSocketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
