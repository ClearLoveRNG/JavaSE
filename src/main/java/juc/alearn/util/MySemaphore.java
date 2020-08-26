package juc.alearn.util;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-10 01:12
 */

import java.util.concurrent.Phaser;
import java.util.concurrent.Semaphore;

/**
 * Semaphore信号量
 *      在java中，使用了synchronized关键字和Lock锁实现了资源的并发访问控制，
 *      在同一时间只允许唯一了线程进入临界区访问资源(读锁除外)，这样子控制的主要目的是为了解决多个线程并发同一资源造成的数据不一致的问题。
 *      在另外一种场景下，一个资源有多个副本可供同时使用，比如打印机房有多个打印机、厕所有多个坑可供同时使用，
 *      这种情况下，Java提供了另外的并发访问控制--资源的多副本的并发访问控制，信号量Semaphore即是其中的一种。
 *
 * 示例:
 *      假若一个工厂有5台机器，但是有8个工人，一台机器同时只能被一个工人使用，只有使用完了，其他工人才能继续使用。
 *      1.创建Semaphore，Semaphore SEMAPHORE = new Semaphore(5)，5表示一共有5个相同的资源供竞争。
 *      2.子线程调用SEMAPHORE.acquire()，使得资源数量减1
 *      3.当资源数量减为0，则后面的线程进入自旋获取资源
 *      4.直到有子线程调用SEMAPHORE.release()，使得资源数量加1
 *      5.自旋的线程会根据公平或非公平的原则来竞争资源
 *
 * 问题：
 *      semaphore初始化有10个令牌，11个线程同时各调用1次acquire方法，会发生什么?
 *          答案：拿不到令牌的线程阻塞，不会继续往下运行。
 *      semaphore初始化有10个令牌，一个线程重复调用11次acquire方法，会发生什么?
 *          答案：线程阻塞，不会继续往下运行。可能会考虑类似于锁的重入的问题，很好，但是，令牌没有重入的概念。你只要调用一次acquire方法，就需要有一个令牌才能继续运行。
 *      semaphore初始化有1个令牌，1个线程调用一次acquire方法，然后调用两次release方法，之后另外一个线程调用acquire(2)方法，此线程能够获取到足够的令牌并继续运行吗?
 *          答案：能，原因是release方法会添加令牌，并不会以初始化的大小为准。
 *      semaphore初始化有2个令牌，一个线程调用1次release方法，然后一次性获取3个令牌，会获取到吗?
 *          答案：能，原因是release会添加令牌，并不会以初始化的大小为准。Semaphore中release方法的调用并没有限制要在acquire后调用。
 */
public class MySemaphore {
    // 初始化5个资源（机器）
    private final static Semaphore SEMAPHORE = new Semaphore(5);

    public static void main(String[] args) {
        // 8个工人争夺资源
        for (int i = 0; i < 8; i++) {
            final String name = "工人" + i;
            new Thread(() -> {
                try {
                    SEMAPHORE.acquire();
                    System.out.println(name + "占用一个机器在生产...");
                    Thread.sleep(2000);
                    System.out.println(name + "释放出机器");
                    SEMAPHORE.release();
                } catch (InterruptedException e) {
                    System.out.println(name + "争夺机器失败");
                }
            }).start();
        }
    }
}
