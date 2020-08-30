package io.learn.nio;

import cn.hutool.core.date.DateUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title: NIO（Non-BlockingIO）非阻塞IO   单线程模型
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-27 15:42
 */
public class Server {

    /**
     * NIO提供了与传统BIO模型中的Socket和ServerSocket相对应的SocketChannel和ServerSocketChannel两种不同的套接字通道实现。
     * 新增的着两种通道都支持阻塞和非阻塞两种模式。
     *
     *
     *
     * IO和NIO的区别
     *      NIO是非阻塞的,IO是阻塞的
     *          IO的各种流是阻塞的。对accept()方法的调用可能会因为等待一个客户端连接而阻塞，
     *          对read()和write()方法的调用可能会因为没有数据可读或可写(也可能是是有数据可读可写，但没有完全写完或者读完)而阻塞，
     *          直到连接的另一端传来新的数据,该线程在此期间不能再干任何事情了。
     *          那么到底是因为啥情况发生了阻塞呢？网速慢的，或仅仅是简单的网络故障都可能导致任意时间的阻塞。
     *          然而不幸的是，在调用BIO的这些方法之前无法知道其是否阻塞。
     *
     *          NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，
     *          如果目前没有数据可用时，就什么都不会获取。而不是保持线程阻塞，所以直至数据变得可以读取之前，
     *          该线程可以继续做其他的事情。 非阻塞写也是如此。一个线程请求写入一些数据到某通道，
     *          但不需要等待它完全写入，这个线程同时可以去做别的事情。
     *          线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，
     *          所以一个单独的线程现在可以管理多个输入和输出通道（channel）。
     *          在非阻塞式通道上调用一个方法总是会立即返回。
     *          例如，在一个非阻塞式ServerSocketChannel上调用accept()方法，如果有连接请求来了，则返回客户端SocketChannel，否则返回null
     *
     *      NIO面向块，I/O 面向流
     *          Java IO面向流意味着每次从流中读一个或多个字节，直至读取所有字节，它们没有被缓存在任何地方。
     *          此外，它不能前后移动流中的数据。如果需要前后移动从流中读取的数据，需要先将它缓存到一个缓冲区。
     *          NIO的缓冲导向方法略有不同。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动。
     *          这就增加了处理过程中的灵活性。但是，还需要检查是否该缓冲区中包含所有您需要处理的数据。
     *          而且，需确保当更多的数据读入缓冲区时，不要覆盖缓冲区里尚未处理的数据。
     *
     * NIO三大核心
     *      通道 channel
     *          我们对数据的读取和写入要通过Channel，它就像水管一样，是一个通道。通道不同于流的地方就是通道是双向的，可以用于读、写和同时读写操作。
     *          底层的操作系统的通道一般都是全双工的，所以全双工的Channel比流能更好的映射底层操作系统的API。
     *          NIO中的Channel的主要实现有：
     *              FileChannel:
     *                  文件IO,用于文件操作
     *              DatagramChannel:
     *                  UDP连接
     *              ServerSocketChannel+SocketChannel:
     *                  TCP连接（Server和Client）
     *                  ServerSocketChannel和SocketChannel都是SelectableChannel的子类。
     *
     *
     *      缓冲区 buffer
     *          Buffer是一个对象，包含一些要写入或者读出的数据。
     *          在NIO库中，所有数据都是用缓冲区处理的。在读取数据时，它是直接读到缓冲区中的；在写入数据时，也是写入到缓冲区中。任何时候访问NIO中的数据，都是通过缓冲区进行操作。
     *          缓冲区实际上是一个数组，并提供了对数据结构化访问以及维护读写位置等信息。
     *          具体的缓存区有这些：ByteBuffer、CharBuffer、 ShortBuffer、IntBuffer、LongBuffer、FloatBuffer、DoubleBuffer。他们实现了相同的接口：Buffer。
     *
     *
     *      多路复用器 selector
     *          Selector是Java NIO 编程的基础。
     *          Selector提供选择已经就绪的任务的能力：Selector会不断轮询注册在其上的Channel，如果某个Channel上面发生读或者写事件，这个Channel就处于就绪状态，会被Selector轮询出来，然后通过SelectionKey可以获取就绪Channel的集合，进行后续的I/O操作。
     *          一个Selector可以同时轮询多个Channel，因为JDK使用了epoll()代替传统的select实现，所以没有最大连接句柄1024/2048的限制。所以，只需要一个线程负责Selector的轮询，就可以接入成千上万的客户端。
     *
     *
     *
     * 有一个selector，是个大管家，在Server端里会定时轮询
     *      1.有没有来连接的Client，有就连上
     *      2.连上来的Client有没有往Server端发数据？对应的Server端有没有写给Client数据？
     *      如果有，则一一进行
     *      selector负责client的连接，同时也负责C和S之间的读写
     *
     * 非阻塞体现在哪里呢？
     *
     *      通过配置监听的通道 Channel 为非阻塞，那么当 Channel 上的 IO 事件还未到达时，
     *      就不会进入阻塞状态一直等待，而是继续轮询其它 Channel，找到 IO 事件已经到达的 Channel 执行
     *
     *      比如：socketChannel.configureBlocking(false)了之后，注册了OP_READ事件，当事件来临，
     *      SocketChannel.read 没读到数据也会返回，返回参数等于0。
     *
     * 有一个坑 OP_WRITE事件
     *      OP_WRITE事件的就绪条件并不是发生在调用channel的write方法之后，而是在当底层缓冲区有空闲空间的情况下。
     *      因为写缓冲区在绝大部分时候都是有空闲空间的，所以如果你注册了写事件，这会使得写事件一直处于就就绪，选择处理现场就会一直占用着CPU资源。所以，只有当你确实有数据要写时再注册写操作，并在写完以后马上取消注册。
     *      其实，在大部分情况下，我们直接调用channel的write方法写数据就好了，没必要都用OP_WRITE事件。那么OP_WRITE事件主要是在什么情况下使用的了？
     *      其实OP_WRITE事件主要是在发送缓冲区空间满的情况下使用的。如：
     *
     *      while (buffer.hasRemaining()) {
     *          int len = socketChannel.write(buffer);
     *          if (len == 0) {
     *              selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_WRITE);
     *              selector.wakeup();
     *              break;
     *          }
     *      }
     *      selectionKey.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
     *
     *
     *      (一次要写100个字节，但是缓冲区只有64个，那就写完这次再注册WRITE事件，同时唤醒阻塞在select方法的selector，让下一次轮询开始)
     *      当buffer还有数据，但缓冲区已经满的情况下，socketChannel.write(buffer)会返回已经写出去的字节数，此时为0。
     *      那么这个时候我们就需要注册OP_WRITE事件，这样当缓冲区又有空闲空间的时候就会触发OP_WRITE事件，这是我们就可以继续将没写完的数据继续写出了。
     *      而且在写完后，一定要记得将OP_WRITE事件注销：selectionKey.interestOps(sk.interestOps() & ~SelectionKey.OP_WRITE);
     *      注意，这里在修改了interest之后调用了wakeup()；方法是为了唤醒被堵塞的selector方法，这样当while中判断selector返回的是0时，会再次调用selector.select()。
     *      而selectionKey的interest是在每次selector.select()操作的时候注册到系统进行监听的，所以在selector.select()调用之后修改的interest需要在下一次selector.select()调用才会生效。
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * 详细信息：https://www.jianshu.com/p/1af407c043cb
     */

    private static final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws IOException {

        //Java NIO中的 ServerSocketChannel 是一个可以监听新进来的TCP连接的通道, 就像标准IO中的ServerSocket一样
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //绑定好服务端的ip和端口
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8989));

        //创建selector
        Selector selector = Selector.open();
        //通道必须配置为非阻塞模式，否则使用选择器就没有任何意义了，因为如果通道在某个事件上被阻塞，那么服务器就不能响应其它事件，必须等待这个事件处理完毕才能去处理其它事件，显然这和选择器的作用背道而驰。
        //必须先设置Blocking属性为false再写register()，否则register方法会报异常
        serverSocketChannel.configureBlocking(false);

        //把通道注册到selector上去，并且告诉selector，一旦有accept事件，也就是建立连接，就把我轮训出来
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //select()方法意思是，开始轮询注册到我身上的通道里，有没有事件发生
        //因为一次 select() 调用不能处理完所有的事件，并且服务器端有可能需要一直监听事件，因此服务器端处理事件的代码一般会放在一个死循环内。
        while (true) {
            System.out.println(DateUtil.now() + "等待轮询事件.......");
            //select()阻塞到至少有一个通道在你注册的事件上就绪了。
            //select(long timeout)和select()一样，除了最长会阻塞timeout毫秒(参数)。
            //selectNow()不会阻塞，不管什么通道就绪都立刻返回（译者注：此方法执行非阻塞的选择操作。如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。）。
            //select方法返回的int值表示有多少通道已经就绪
            int select = selector.select();

            if (select > 0) {
                System.out.println("轮询成功！事件数量:" + select);
                //获取轮训出来的事件
                //keys里存的就是selector轮询完一次，收到要处理的事件集合
                Set<SelectionKey> keys = selector.selectedKeys();
                //拿到迭代器准备迭代
                Iterator<SelectionKey> keyIterator = keys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    //处理轮询到的事件
                    if (key.isAcceptable()) {
                        System.out.println("正在处理Accept事件");

                        //key.channel()的意思是，拿到这个key对应的channel，也就是谁往selector注册的就是谁
                        //在非阻塞模式下，accept() 方法会立刻返回，如果还没有新进来的连接,返回的将是null
                        //返回值SocketChannel包含了客户端的数据以及客户端通道本身
                        SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                        if (socketChannel != null) {
                            //这个也要设置非阻塞
                            socketChannel.configureBlocking(false);
                            //把这个通道注册到selector上去，并且告诉selector，一旦有读的操作，就把我轮训出来
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println("处理Accept事件完成");
                        }
                    } else if (key.isReadable()) {
                        executorService.submit(()->{
                            //先监听WRITE事件，防止read时候出现异常导致key.cancel()方法生效，从而导致interestOps()异常
                            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
                            //既然是读操作，证明一定是已经和客户端建立好连接，所以此时的key.channel()，返回的是客户端的SocketChannel,里面包含了客户端发来的数据
                            try {
                                NIOUtil.readFromSocketChannel(key);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                    } else if (key.isWritable()) {
                        executorService.submit(()->{
                            //写事件，和读事件很类似
                            try {
                                NIOUtil.writeToSocketChannel(key, "客户端曹尼玛");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    //处理完事件要删除，要不然下一次轮询又要再来一遍
                    keyIterator.remove();
                }
                System.out.println("本次轮询事件全部处理完成，准备进行下一次轮询.......");
                System.out.println("---------------------------------------------------");
            } else {
                System.out.println("本次轮询没有找到触发事件的通道，准备进行下一次轮询.......");
            }
        }
        //这两句写到finally里
        //serverSocketChannel.close();
        //selector.close();
    }

}
