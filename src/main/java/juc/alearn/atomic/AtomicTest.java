package juc.alearn.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-23 12:27
 */
public class AtomicTest {
    public static void main(String[] args) throws InterruptedException {

        //count会报编译时期异常
        //错误:(20, 17) java: 从lambda 表达式引用的本地变量必须是最终变量或实际上的最终变量
        //原因1：一个在主线程、一个在另一个线程，这俩count压根不是同一个东西，只不过另一个线程持有的是count副本而已，
        //      但你觉得是同一个count，所以Java为了避免错误发生，直接编译不通过
        //原因2：count是主线程栈上私有变量，你直接在另一个线程改变，则count就不是私有的了，就会被其他线程共享，造成地址泄露
        //      Java为了避免这种危险的操作,直接报了编译异常
//        int count = 0;
//        for(int i = 0;i<100;i++){
//            new Thread(()->{
//                count++;
//            }).start();
//        }
//        System.out.println(count);

        //所以换成AtomicXXX
        AtomicInteger count = new AtomicInteger(0);
        for(int i = 0;i<100;i++){
            new Thread(()->{
                count.getAndIncrement();
            }).start();
        }
        Thread.sleep(1000);
        System.out.println(count.get());
    }
}
