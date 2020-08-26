package juc.alearn.lock;

import cn.hutool.core.util.RandomUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Title: 用Lock实现的生产者消费者模型
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-23 13:30
 */
public class MyProducerConsumerWithLock {
    private static ExecutorService executor = Executors.newFixedThreadPool(10);
    private static Warehouse<Integer> warehouse = new Warehouse<>();

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            executor.execute(new Producer(RandomUtil.randomInt(1, 10), warehouse));
            executor.execute(new Consumer(warehouse));
        }
        executor.shutdown();
    }
}

/**
 * 生产者
 */
class Producer implements Runnable {
    //生产的内容是什么
    private int number;

    //生产到哪一个仓库
    private Warehouse<Integer> warehouse;

    public Producer(int number, Warehouse<Integer> warehouse) {
        this.number = number;
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
            try {
                Thread.sleep(RandomUtil.randomInt(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            warehouse.produce(number);

    }
}


/**
 * 消费者
 */
class Consumer implements Runnable {

    //到哪一个仓库去消费
    private Warehouse<Integer> warehouse;

    public Consumer(Warehouse<Integer> warehouse) {
        this.warehouse = warehouse;
    }

    @Override
    public void run() {
            try {
                Thread.sleep(RandomUtil.randomInt(1000, 2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            warehouse.consume();

    }
}

/**
 * 仓库
 */
class Warehouse<T> {

    //阻塞队列
    //BlockingQueue介绍：https://www.jianshu.com/p/7b2f1fa616c6
    private BlockingQueue<T> blockingQueue = new ArrayBlockingQueue<>(1);

    private Lock lock = new ReentrantLock();

    private Condition produceCondition = lock.newCondition();
    private Condition consumeCondition = lock.newCondition();


    public void produce(T obj) {
        lock.lock();
        try {
            while (!blockingQueue.offer(obj)) {
                System.out.println("仓库满了,生产者线程" + Thread.currentThread().getName() + "进入等待....");
                produceCondition.await();
            }
            System.out.println("生产者线程" + Thread.currentThread().getName() + "生产" + obj.toString() + "成功,此时仓库库存为:" + blockingQueue.size());
            consumeCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void consume() {
        lock.lock();
        try {
            T obj;
            while ((obj = blockingQueue.poll()) == null) {
                System.out.println("仓库空了,消费者线程" + Thread.currentThread().getName() + "进入等待....");
                consumeCondition.await();
            }
            System.out.println("消费者线程" + Thread.currentThread().getName() + "消费" + obj.toString() + "成功,此时仓库库存为:" + blockingQueue.size());
            produceCondition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
