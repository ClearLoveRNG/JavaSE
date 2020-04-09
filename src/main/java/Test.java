import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Title:
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time: 2019-06-30 18:53
 */
public class Test {

    public static AtomicInteger integer = new AtomicInteger(0);

    public static final int THREAD_COUNT = 20;

    private static void increase() {
        integer.incrementAndGet();
        Thread thread = Thread.currentThread();
        System.out.println("当前线程名字:"+thread.getName());
    }

    public static void main(String[] args) throws Exception {
//        for (int i = 0; i < THREAD_COUNT; i++) {
//            new Thread(Test::increase).start();
//        }
//
//        while (Thread.activeCount() > 2) {
//            System.out.println("当前线程:"+Thread.currentThread().getName()+"让出竞争位置");
//            Thread.yield();
//        }
//        Thread.currentThread().getThreadGroup().list();
//
//        System.out.println("多线程并发后:" + integer);
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        Unsafe unsafe = (Unsafe) field.get(null);
        System.out.println(unsafe.addressSize());

        Thread.currentThread().wait(10);
    }
}



