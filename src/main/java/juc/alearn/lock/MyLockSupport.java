package juc.alearn.lock;

import java.util.concurrent.locks.LockSupport;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-02 23:55
 */

/**
 *  LockSupport类是用来替代Object.wait()与Object.notify()的搭配。
 *  它是一个线程阻塞工具类，所有的方法都是静态方法，可以让线程在任意位置阻塞，当然阻塞之后肯定得有唤醒的方法
 *  当调用LockSupport.park时，表示当前线程将会等待，直至获得许可，并且不会释放锁
 *  当调用LockSupport.unpark时，必须把等待获得许可的线程作为参数进行传递，好让此线程继续运行
 *
 *  详细文章：https://www.jianshu.com/p/f1f2cd289205
 */
public class MyLockSupport {
    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("blocker:" + LockSupport.getBlocker(thread));
            System.out.println("unpark开始");
            LockSupport.unpark(thread);
            System.out.println("unpark结束");
        }).start();
        System.out.println("park开始");
        LockSupport.park();
        System.out.println("blocker:" + LockSupport.getBlocker(thread));
        System.out.println("park结束");
    }
}
