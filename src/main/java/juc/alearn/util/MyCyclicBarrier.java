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
 *      2.
 *
 */
public class MyCyclicBarrier {
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(5);
    private final static CyclicBarrier BARRIER = new CyclicBarrier(5);

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            final String name = "玩家" + i;
            EXECUTOR_SERVICE.execute(() -> {
                try {
                    Thread.sleep(2000);
                    System.out.println(name + "已准备,等待其他玩家准备...");
                    BARRIER.await();
                    Thread.sleep(1000);
                    System.out.println(name + "已加入游戏");
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.out.println(name + "离开游戏");
                }
            });
        }
        EXECUTOR_SERVICE.shutdown();
    }
}
