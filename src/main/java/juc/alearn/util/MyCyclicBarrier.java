package juc.alearn.util;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-09 23:16
 */

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CyclicBarrier是一个同步工具类，它允许一组线程在到达某个栅栏点(common barrier point)互相等待，
 * 发生阻塞，直到最后一个线程到达栅栏点，栅栏才会打开，处于阻塞状态的线程恢复继续执行，它非常适用于一组线程之间必需经常互相等待的情况。
 * CyclicBarrier字面理解是循环的栅栏，之所以称之为循环的是因为在等待线程释放后，该栅栏还可以复用。
 *
 * 示例：
 *      模拟对战平台中玩家需要完全准备好了,才能进入游戏的场景。
 *      1.创建对象,CyclicBarrier BARRIER = new CyclicBarrier(5)
 *          默认的构造方法是CyclicBarrier(int parties)，其参数表示屏障拦截的线程数量，每个线程调用await方法告诉CyclicBarrier已经到达屏障位置，线程被阻塞。
 *          另外一个构造方法CyclicBarrier(int parties, Runnable barrierAction)，其中barrierAction任务会在所有线程到达屏障后执行。
 *      2.子线程们执行逻辑后调用CyclicBarrier.await()方法，会等待直到有足够数量的线程调用await(也就是开闸状态)。
 *      3.子线程们都到达栅栏处，则直接开闸，全部往下执行逻辑
 *
 *      reset()方法可以破坏栅栏，并唤醒所有等待的线程
 *
 * 和CountDownLatch的区别
 *      1.CountDownLatch的使用是一次性的，而CyclicBarrier可以用reset进行重用。
 *      2.CountDownLatch是一个线程等待多个线程执行完了，再进行执行。
 *      而CyclicBarrier是多个线程等待所有线程都执行完了，再进行执行(调用多次await方法，也就是设置多个栅栏)。
 */
public class MyCyclicBarrier {
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
    private final static CyclicBarrier BARRIER = new CyclicBarrier(5);

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            final String name = "玩家" + i;
            EXECUTOR_SERVICE.execute(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println(name + "已准备,等待其他玩家准备...");
                    BARRIER.await();
                    Thread.sleep(500);
                    System.out.println(name + "已加入游戏");
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println(name + "离开游戏");
                }
            });
        }
        EXECUTOR_SERVICE.shutdown();
    }
}
