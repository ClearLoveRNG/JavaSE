package nio.learn;

import java.nio.IntBuffer;

/**
 * Title: NIO学习
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time:2019/4/2 16:03
 */

/**
 * NIO是什么
 *      JDK 1.4中的java.nio.*包中引入新的Java I/O库，其目的是提高速度。
 *      实际上，“旧”的I/O包已经使用NIO重新实现过，即使我们不显式的使用NIO编程，也能从中受益。
 *      NIO翻译成 no-blocking io 或者 new io 都无所谓啦，都说得通~
 *
 * 为什么要用NIO
 *      IO操作往往在两个场景下会用到：
 *      1.文件IO
 *      2.网络IO
 *      NIO的魅力在网络中使用IO就可以体现出来了
 *
 * NIO和IO的区别
 *      IO：是面向流的处理，面向流的I/O系统一次一个字节地处理数据。
 *      NIO：NIO是面向块(缓冲区)的处理，一个面向块(缓冲区)的I/O系统以块的形式处理数据。
 *
 * NIO的三种组成部分
 *      1.Buffer缓冲区
 *          实现了Buffer接口的子类都可以直接调用静态方法allocate(缓冲区大小)
 *          get()：读取缓冲区的数据
 *          put()：写数据到缓冲区中
 *          get()之前调用flip(),put()之前调用clear()
 *
 *          Buffer类维护了4个核心变量属性来提供关于其所包含的数组的信息。它们是：
 *              容量Capacity
 *                  缓冲区能够容纳的数据元素的最大数量。容量在缓冲区创建时被设定，并且永远不能被改变。(不能被改变的原因也很简单，底层是数组嘛)
 *              上界Limit
 *                  缓冲区里的数据的总数，代表了当前缓冲区中一共有多少数据。
 *              位置Position
 *                  下一个要被读或写的元素的位置。Position会自动由相应的 get( )和 put( )函数更新。
 *              标记Mark
 *                  一个备忘位置。用于记录上一次读写的位置。
 *              0 <= mark <= position <= limit <= capacity
 *      2.Channel管道
 *
 *          只负责传输数据、不直接操作数据的，所以要和Buffer配合使用，通过Channel管道运输着存储数据的Buffer缓冲区的来实现数据的处理！
 *      3.Selector选择器
 *
 *
 *
 *
 *
 *
 *
 *
 *
 * 详细内容：https://mp.weixin.qq.com/s?__biz=MzI4Njg5MDA5NA==&mid=2247484235&idx=1&sn=4c3b6d13335245d4de1864672ea96256&chksm=ebd7424adca0cb5cb26eb51bca6542ab816388cf245d071b74891dd3f598ccd825f8611ca20c&scene=21###wechat_redirect
 */
public class NioDemo {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(8);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i + 1);
        }

        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
