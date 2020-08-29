package io.learn.bio;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-27 12:44
 */
public class Client {
    public static void main(String[] args) throws IOException, InterruptedException {
        //套接字是网络连接的一个端点。套接字使得一个应用可以从网络中读取和写入数据。放在两个不同计算机上的两个应用可以通过连接发送和接受字节流。为了从你的应用发送一条信息到另一个应用，你需要知道另一个应用的IP地址和套接字端口。在Java里边，套接字指的是java.net.Socket类。
        //Socket类代表一个客户端套接字，即任何时候你想连接到一个远程服务器应用的时候你构造的套接字
        Socket socket = new Socket("127.0.0.1", 8989);

        //这里加个Scanner，那边server就一直阻塞在read方法
        Scanner sc  = new Scanner(System.in);
        System.out.print("请输入:");
        socket.getOutputStream().write(("服务端你好,"+sc.nextLine()).getBytes(StandardCharsets.UTF_8));
        socket.getOutputStream().flush();

        byte[] b = new byte[1024];
        int read = socket.getInputStream().read(b);
        System.out.println("收到服务器发来消息:" + new String(b, 0, read));
        socket.close();
    }
}
