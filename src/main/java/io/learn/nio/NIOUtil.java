package io.learn.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-28 19:25
 */
public class NIOUtil {
    /**
     * 直接向SocketChannel里发送数据
     * <p>
     * 也可以用一句代码完成
     * socketChannel.write(ByteBuffer.wrap(new String("xxx").getBytes()));
     *
     * @param socketChannel
     * @param msg
     * @throws IOException
     */
    public static void writeToSocketChannel(SocketChannel socketChannel, String msg) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(msg.getBytes());
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
    }

    /**
     * 通过注册OP_WRITE事件方式向SocketChannel里发送数据
     *
     * @param key
     * @param msg
     */
    public static void writeToSocketChannel(SelectionKey key, String msg) throws IOException {
        System.out.println("正在处理Write事件");
        SocketChannel socketChannel = (SocketChannel) key.channel();

        //从key里直接拿到绑定好的buffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.put(msg.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        while (byteBuffer.hasRemaining()) {
            socketChannel.write(byteBuffer);
        }
        //要把写事件从key里排除。写操作的就绪条件为底层缓冲区有空闲空间，
        //而写缓冲区绝大部分时间都是有空闲空间的，所以当注册写事件后，写操作一直是就绪的，
        //选择处理线程会占用整个CPU资源。所以，只有当确实有数据要写时再注册写操作，并在写完以后马上取消注册。
        if (socketChannel.isConnected()) {
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
        System.out.println("Write事件处理完成");
    }


    /**
     * 从SocketChannel读数据
     *
     * @param key
     * @return
     * @throws IOException
     */
    public static void readFromSocketChannel(SelectionKey key) throws IOException {

        System.out.println("正在处理Read事件");
        SocketChannel socketChannel = (SocketChannel) key.channel();

        //NIO不管是读还是写都要先给缓冲区
        //注意：这里的读和写都是站在缓冲区的角度去看而不是通道的角度
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.clear();


        //防止对方直接关闭，导致一直触发READ事件
        int read = 0;
        boolean failure = true;
        try {
            read = socketChannel.read(byteBuffer);
            failure = false;
        } catch (Exception e) {
            // ignore
        }

        //如果读取不到数据，则取消OP_READ绑定
        if (read <= 0 || failure) {
            key.cancel();
            System.out.println("对方已关闭,取消OP_READ绑定");
        } else {

            //从Channel->Buffer read()
            //从Buffer->Channel write()
            StringBuilder sb = new StringBuilder();
            while (read > 0) {
                //读好了，切换写模式
                //ByteBuffer.flip()方法，position设回0，并将limit设成之前的position的值
                byteBuffer.flip();

                byte[] bytes = new byte[byteBuffer.limit()];
                //有可能一次性读不完，所以用hasRemaining循环检测到底还有没有数据了
                while (byteBuffer.hasRemaining()) {
                    //使用get()方法从Buffer中读取数据
                    for (int i = 0; i < bytes.length; i++) {
                        if (bytes[i] == 0) {
                            bytes[i] = byteBuffer.get();
                        }
                    }
                }
                sb.append(new String(bytes, StandardCharsets.UTF_8));
                //如果Buffer中仍有未读的数据，且后续还需要这些数据，但是此时想要先写些数据，那么使用compact()方法。
                //compact()方法将所有未读的数据拷贝到Buffer起始处。然后将position设到最后一个未读元素正后面。
                //limit属性依然像clear()方法一样，设置成capacity。现在Buffer准备好写数据了，但是不会覆盖未读的数据。
                //有点像JVM垃圾算法里的标记-整理算法
                //byteBuffer.compact();

                //不过因为上面用了while (byteBuffer.hasRemaining())，表示这次一定能把buffer里面的数据读完，所以也可以直接buffer.clear()
                //不过clear()，不会像方法名一样，真的把数据删除了，而是把position和limit指针全部重置，所以下一次读进来数据，直接就把原来的数据覆盖掉了
                byteBuffer.clear();

                read = socketChannel.read(byteBuffer);
            }
            System.out.println("Read事件处理完成");
            System.out.println("接收到对方发来的消息:" + sb.toString());
        }
    }
}
