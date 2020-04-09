package juc.alearn.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-09 22:33
 */

/**
 * CountDownLatch也叫闭锁，它是J.U.C包中基于AQS实现的一个很简单的类，它允许一个或多个线程等待其他线程完成操作后再执行。
 * CountDownLatch内部会维护一个资源数量为初始化值为的计数器，当A线程调用await方法后，A线程会在计数器大于0的时候一直阻塞等待。
 * 当一个线程完成任务后，计数器的值会减1。当计数器变为0时，表示所有的线程已经完成任务，等待的主线程被唤醒继续执行。
 *
 * 示例:
 *      把一个大任务划分成n个小任务，相当于主线程划分成n个子线程，子线程全部执行完毕，主线程才开始进行
 *
 *      1.创建一个CountDownLatch工具，CountDownLatch countDownLatch = new CountDownLatch(5);
 *      2.主线程调用CountDownLatch.await()，使自己进入等待状态
 *      3.子线程执行完逻辑的最后调用CountDownLatch.countDown()表示自己执行完毕，同时把n减1
 *      4.随着子线程依次执行完毕，n减到0，主线程开始运行
 *
 * 代码过程:
 *      因为构造方法里面我们设置了资源值，所以在await的时候会调用tryAcquireShared返回-1进行阻塞等待。
 *      而countDown方法则每次调用tryReleaseShared(1)进行资源-1的操作，当资源变为0时，唤醒Sync队列里的节点进行资源获取的操作，
 *      从而让阻塞的主线程又活跃起来。
 */
public class MyCountDownLatch {
    private final static CountDownLatch countDownLatch = new CountDownLatch(5);
    private final static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                try {
                    // 模拟执行任务
                    Thread.sleep(1000);
                    System.out.println(Thread.currentThread().getName() + "执行完任务");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        System.out.println("主线程等待子线程执行任务完毕，继续执行");
    }
}
