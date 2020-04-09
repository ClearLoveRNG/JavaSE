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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
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
        Lock lock = new ReentrantLock();
        //独占lock
        lock.lock();
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
