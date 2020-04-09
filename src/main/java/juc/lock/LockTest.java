package juc.lock;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title: ReentrantLock
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2019-12-10 15:39
 */

/**
 * ReentrantLock是一个可重入的互斥锁，又被称为”独占锁”。
 * ➣ ReentrantLock锁在同一个时间点只能被一个线程锁持有；而可重入的意思是，ReentrantLock锁，可以被单个线程多次获取。
 * ➣ ReentrantLock分为”公平锁”和“非公平锁”。它们的区别体现在获取锁的机制上是否公平以及执行速度上。
 * 而这两种锁的启用也是非常容易控制的。
 * 1.公平
 * ReentrantLock fairReentrantLock = new ReentrantLock(true);
 * 2.不公平(不传false也可以，默认不公平)
 * ReentrantLock unFairReentrantLock = new ReentrantLock(false);
 * ➣ ReentrantLock是通过一个FIFO的等待队列来管理获取该锁所有线程的。
 *  
 * ReentrantLock是一个独占锁，在获取锁的之后其所有的操作是线程独享的，其它的线程在没有获取到锁之前都需要进行等待。
 *
 */
public class LockTest {
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        while (true){
            executorService.execute(ticket::sale);
            System.out.println(ticket.getTicket());
            if(ticket.getTicket() == 0){
                executorService.shutdown();
                break;
            }
        }
    }
}

class Ticket {
    private ReentrantLock reentrantLock = new ReentrantLock();
    //卖票
    private volatile int ticket = 10;

    //在进行公平锁处理的时候每当锁定一个线程对象就会使用sync.acquire(1)方法进行表示，
    //在进行解锁的时候会使用一个sync.release(1)释放方法，1表示释放一个。
    public int sale() {
        System.out.println("线程名:" + Thread.currentThread().getName() + "在等锁---剩余票:" + ticket);
        reentrantLock.lock();
        System.out.println("线程名:" + Thread.currentThread().getName() + "获取锁---剩余票:" + ticket);
        try {
            if (ticket > 0) {
                ticket--;
                System.out.println("线程名:" + Thread.currentThread().getName() + "卖票---剩余票:" + ticket);
            }
            return ticket;
        } finally {
            reentrantLock.unlock();
            System.out.println("线程名:" + Thread.currentThread().getName() + "释放锁---剩余票:" + ticket);
        }
    }

    public int getTicket() {
        return ticket;
    }
}
