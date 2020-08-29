package io.learn.bio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Title: 阻塞IO-BlockingIO(BIO)
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-27 12:44
 */

/**
 * 一般线上不使用BIO，因为大量线程阻塞中，然后CPU不停的进行线程上下文切换(因为要阻塞+唤醒线程)，效率非常低
 * 但效率低不代表不好用，如果确定客户端的数量很少，就可以用BIO，因为用法简单，不容易出错。
 */
public class Server {
    /**
     * 不允许直接在方法上直接throws异常，因为server端出问题会直接退出，但是和客户端的连接通道没有正确关闭，会导致资源浪费
     * public static void main(String[] args) throws IOException
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        //ServerSocket是服务器套接字，用于接收客户端的连接，相当于是一个插座，插座的电源连到ip+端口这个总电源上(也就是你要进行数据传输的应用)，随时准备接受客户端的socket
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress("127.0.0.1", 8989));

        while (true) {
            System.out.println(Thread.currentThread().getName()+"等待客户端连接....");

            //这个accept对象是把客户端的socket封装了一下
            //accept()会阻塞当前线程，直到有一个客户拉了一根电线插到自己的插座上,我就新开一个线程去和这个客户端进行数据传输

            /**
             * serverSocket.accept()会被阻塞? 这里涉及到阻塞式同步IO的工作原理:
             * 服务器线程发起一个accept动作，询问操作系统 是否有新的socket套接字信息从客户端发送过来。
             * 注意，是询问操作系统。也就是说socket套接字的IO模式支持是基于操作系统的，那么自然同步IO/异步IO的支持就是需要操作系统级别的了
             * 如果操作系统没有发现有套接字从指定的端口X来，那么操作系统就会等待。这样serverSocket.accept()方法就会一直等待。这就是为什么accept()方法为什么会阻塞: 它内部的实现是使用的操作系统级别的同步IO。
             */
            Socket socket = serverSocket.accept();


            System.out.println(Thread.currentThread().getName()+"客户端" + socket.getInetAddress() + "连接成功");
            new Thread(() -> {
                byte[] b = new byte[1024];
                try {
                    //这个read也会阻塞当前线程
                    //你服务端看似在一直读，但是客户端根本就没往流里写，那就一直阻塞在这，直到真的有数据读进来
                    //比如客户端用Scanner等待输入，将输入内容发送过来，如果一直不输入，大家都都等在这了
                    int read = socket.getInputStream().read(b);
                    System.out.println("收到客户端发来消息：" + new String(b, 0, read));

                    //这个write也会阻塞当前线程
                    Scanner sc  = new Scanner(System.in);
                    System.out.print("请输入:");
                    socket.getOutputStream().write(("我已收到消息"+sc.nextLine()).getBytes());
                    socket.getOutputStream().flush();
                    System.out.println(Thread.currentThread().getName() + "已经处理完一个客户端");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
