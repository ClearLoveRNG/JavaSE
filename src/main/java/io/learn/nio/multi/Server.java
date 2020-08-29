package io.learn.nio.multi;

/**
 * Title: NIO（Non-BlockingIO）非阻塞IO   多线程模型-Reactor模式（相应）
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-28 14:06
 */
public class Server {


    /**
     * 在单线程模型里，这个selector既负责建立连接，有负责数据读写，太累了
     *      于是在Reactor模式里，selector里只负责建立连接，读写的工作交给一个线程池
     * @param args
     */
    public static void main(String[] args) {

    }
}
