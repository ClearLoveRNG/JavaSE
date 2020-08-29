package io.learn.nio.single;

import io.learn.nio.NIOUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
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
        //和服务器建立连接
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8989));

        Selector selector = Selector.open();
        //注册自己，事件OP_CONNECT:客户端连接服务端事件
        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        while (true){
            int select = selector.select();

            if(select > 0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if(key.isConnectable()){
                        System.out.println("连接服务器中...");
                        if(socketChannel.finishConnect()){
                            System.out.println("连接服务器成功!!");
                            SocketChannel socketChannel1 = (SocketChannel) key.channel();
                            socketChannel1.configureBlocking(false);
                            socketChannel1.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                        }
                    } else if (key.isReadable()){
                        NIOUtil.readFromSocketChannel(key);
//                        key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                    } else if(key.isWritable()) {
                        //写事件，和读事件很类似
                        NIOUtil.writeToSocketChannel(key,"服务端你妈死了");
                    }
                }
                iterator.remove();
            }
        }
    }
}
