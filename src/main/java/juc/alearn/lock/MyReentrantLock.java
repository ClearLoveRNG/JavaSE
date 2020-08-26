package juc.alearn.lock;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-06 00:29
 */
import sun.java2d.pipe.AAShapePipe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 为什么JDK已经有synchronized同步机制和wait(),notify()以及notifyAll()线程协同机制后
 * 还要提供一个Lock和Condition呢？
 *      一个锁只能有一个条件(要不都去竞争，要不都去阻塞等待，无法指定具体的线程去竞争)
 *      无法读写分离(100个线程 99个读 1个写 读不会产生并发问题，但依旧要全部加锁，导致性能变差)
 *      无法知道当前线程是否获取到了锁
 *      无法实现公平性（一个等待锁阻塞了10分钟线程，被一个刚等了2秒的线程抢到了锁，这不公平，所以会产生一个线程运气很差，一直拿不到锁的现象-线程饥饿现象）
 *      等待锁的时候只能阻塞，并且不可中断
 * 而Lock和Condition就是解决上述问题而生的
 *
 *
 *
 *
 * 可重入锁ReentrantLock的底层是通过AbstractQueuedSynchronizer实现
 *
 * ReentrantLock实现了Lock接口，Lock接口中定义了lock与unlock相关操作，并且还存在newCondition方法，表示生成一个条件。
 *      Condition是用来替代JDK原生唤醒线程的工具
 *          Condition.await()替代Object.wait()
 *          Condition.signal()替代Object.notify()
 *          Condition.signalAll()替代Object.notifyAll()
 *      相比于原生唤醒方式，Condition更具灵活性，假如一共10个线程
 *      JDK：都监听一个对象，所以这10个线程只能听从这一个对象的指挥
 *      Condition:虽然都共用一个Lock，但可以new出3个Condition，c1、c2、c3
 *                1-3号线程听从c1指挥、4-6号线程听从c2指挥、6-10号线程听从c3指挥
 *
 * ReentrantLock类内部总共存在Sync、NonfairSync、FairSync三个类，
 * NonfairSync与FairSync类继承自Sync类，Sync类继承自AbstractQueuedSynchronizer抽象类
 * Fair只要资源被其他线程占用，该线程就会添加到sync queue中的尾部，而不会先尝试获取资源。
 * 这也是和Nonfair最大的区别，Nonfair每一次都会尝试去获取资源，如果此时该资源恰好被释放，则会被当前线程获取，这就造成了不公平的现象，当获取不成功，再加入队列尾部。
 *
 *
 * 工作过程：
 *     state初始化为0，表示未锁定状态。
 *     A线程lock()时，会调用tryAcquire()独占该锁并将state+1。
 *     此后，其他线程再tryAcquire()时就会失败，直到A线程unlock()到state=0（即释放锁）为止，其它线程才有机会获取该锁。
 *     当然，释放锁之前，A线程自己是可以重复获取此锁的（state会累加），这就是可重入的概念。
 *     但要注意，获取多少次就要释放多么次，这样才能保证state是能回到零态的。
 *
 *
 *
 * 详细信息：https://www.jianshu.com/p/b6efbdbdc6fa
 */
public class MyReentrantLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();


        //假如线程A和线程B使用同一个锁LOCK，此时线程A首先获取到锁LOCK.lock()，并且始终持有不释放。如果此时B要去获取锁，有四种方式：
        //
        //LOCK.lock(): 此方式会始终处于等待中，即使调用B.interrupt()也不能中断，除非线程A调用LOCK.unlock()释放锁。
        //
        //LOCK.lockInterruptibly(): 此方式会等待，但当调用B.interrupt()会被中断等待，并抛出InterruptedException异常，否则会与lock()一样始终处于等待中，直到线程A释放锁。
        //
        //LOCK.tryLock(): 该处不会等待，获取不到锁并直接返回false，去执行下面的逻辑。
        //
        //LOCK.tryLock(10, TimeUnit.SECONDS)：该处会在10秒时间内处于等待中，但当调用B.interrupt()会被中断等待，并抛出InterruptedException。10秒时间内如果线程A释放锁，会获取到锁并返回true，否则10秒过后会获取不到锁并返回false，去执行下面的逻辑。
        //
        //
        //
        //Lock和TryLock的区别
        //1:  lock拿不到锁会一直等待。tryLock是去尝试，拿不到就返回false，拿到返回true。
        //
        //2:  tryLock是可以被打断的，被中断 的，lock是不可以。

        //独占lock
        lock.lock();
        lock.tryLock();
        try {
            //业务逻辑……………………
            System.out.println("线程id:" + Thread.currentThread().getId() + "获得锁");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            //释放lock
            lock.unlock();
            System.out.println("线程id:" + Thread.currentThread().getId() + "释放锁");
        }
        new Thread(()->{
            lock.lock();

            try {
                //业务逻辑……………………
                System.out.println("线程id:" + Thread.currentThread().getId() + "获得锁");
            } finally {
                //释放lock
                lock.unlock();
                System.out.println("线程id:" + Thread.currentThread().getId() + "释放锁");
            }
        }).start();
    }
}
