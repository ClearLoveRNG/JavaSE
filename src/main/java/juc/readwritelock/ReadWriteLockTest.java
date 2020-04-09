package juc.readwritelock;

/**
 * Title: ReadWriteLock 读写锁
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2019-12-10 17:38
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 所谓的读写锁指的是有两把锁，在进行数据写入的时候有一把”写锁”，而在进行数据读取的时候有一把”读锁”，很明显写锁一定会实现线程安全同步处理操作，而读锁可以被多个对象读取获得。
 * ➣ 分为读锁和写锁，多个读锁不互斥（共享锁），读锁与写锁互斥，这是由jvm自己控制的，开发者只要上好相应的锁即可；
 * ➣ ReentrantReadWriteLock会使用两把锁来解决问题，一个读锁（多个线程可以同时读），一个写锁（单个线程写）。
 * ➣ ReadLock可以被多个线程持有并且在作用时排斥任何的WriteLock，而WriteLock则是完全的互斥，这一特性最为重要，             
 * 因为对于高读取频率而相对较低写入的数据结构，使用此类锁同步机制则可以提高并发量；
 */
public class ReadWriteLockTest {
    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();
        o.wait();
    }
}

class Account {
    private String name;
    private int myMoney = 0;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock readLock = readWriteLock.readLock();
    private Lock writeLock = readWriteLock.writeLock();

    public void saveMoney(String name, int money) {
        writeLock.lock();
        try {
            if (money > 0) {
                myMoney += money;
                System.out.println("姓名:" + name + "给我打" + money + "余额:" + myMoney);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public int getMoney() {
        readLock.lock();
        try {
            return myMoney;
        } finally {
            readLock.unlock();
        }
    }
}
