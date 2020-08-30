package io.learn.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title: 客户端
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-27 15:42
 */
public class Client {

    private static final ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws IOException {
        //Java NIO中的SocketChannel是一个连接到TCP网络套接字的通道
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        //和服务器建立连接(非阻塞的，也就是可能直接返回false)
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8989));

        Selector selector = Selector.open();
        //注册自己，事件OP_CONNECT:客户端连接服务端事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true) {
            int select = selector.select();

            if (select > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isConnectable()) {
                        System.out.println("连接服务器中...");

                        //isConnectionPending()
                        //告知这个channel是否正在进行连接操作。
                        //仅当这个channel的连接操作已经启动，但是还没完成
                        if (socketChannel.isConnectionPending()) {
                            socketChannel.finishConnect();
                            System.out.println("连接服务器成功!!");
                        }

                        //socketChannel.finishConnect()
                        //如果这个channel已经连接了，那么调用该方法不会阻塞并会立即返回true。
                        //如果这个channel是非阻塞模式的，那么该方法将返回false如果连接操作还没完成
                        //所以要在isConnectionPending()==true的时候手动完成连接操作
                        if (socketChannel.finishConnect()) {
                            SocketChannel socketChannel1 = (SocketChannel) key.channel();
                            socketChannel1.configureBlocking(false);
                            socketChannel1.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        }
                    } else if (key.isReadable()) {
                        //这里注释如果放开，server端和client端会互相read完了write，write完了read，死循环
                        //key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                        NIOUtil.readFromSocketChannel(key);
                    } else if (key.isWritable()) {
                        //写事件，和读事件很类似
                        NIOUtil.writeToSocketChannel(key, "服务端你妈死了");
                    }
                }
                iterator.remove();
            }
        }
    }
}
