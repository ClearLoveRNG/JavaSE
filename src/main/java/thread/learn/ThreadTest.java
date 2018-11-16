package thread.learn;

/**
 * Title:
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/16 11:13
 */
public class ThreadTest {
    //sleep
//    不会释放当前锁，所以不建议在同步代码块中使用sleep方法，因为需要用到这把锁的线程会一直等待该线程醒来
//    Thread.sleep(1000); 表示当前线程暂停1000毫秒，其他线程不受影响
//    同时会抛出InterruptedException 中断异常，因为当前线程sleep的时候，有可能被停止，这时就会抛出 InterruptedException

    //join
//    xxx.join();加入到当前线程中,当前线程必须等待xxx线程结束后，才继续执行

    //setPriority
//    xxx.setPriority(1); 设置线程的优先级，优先级越高，占用CPU资源概率越大，
//    最大为Thread.MAX_PRIORITY(10)，最小为Thread.MIN_PRIORITY(1),如果不设置优先级，默认为Thread.NORM_PRIORITY(5)

    //yield
//    xxx.yield();临时释放CPU资源，以便其它线程占用CPU，当然自己也可以立即占用

    //setDaemon
//    xxx.setDaemon(true); 设置xxx线程为守护线程，用来日志，性能统计等
//    如果一个用户线程结束后，不管守护线程是否执行完毕，JVM都会停止服务，所以守护线程不能用来执行计算，IO操作等

    //wait
//    xxx.wait();指在一个已经进入了同步锁的线程内，让自己暂时让出同步锁，以便其他正在等待此锁的线程可以得到同步锁并运行
//    只有其他线程调用了notify方法，调用wait方法的一个或多个线程就会解除wait状态，重新参与竞争对象锁，程序如果可以再次得到锁，就可以继续向下运行。

    //notify notifyAll
//    xxx.notify();xxx.notifyAll();
//    调用某个对象的notify()方法能够唤醒一个正在等待这个对象的monitor的线程，如果有多个线程都在等待这个对象的monitor，则只能唤醒其中一个线程；
//    调用notifyAll()方法能够唤醒所有正在等待这个对象的monitor的线程，唤醒的线程获得锁的概率是随机的，取决于cpu调度
//    notify()或者notifyAll()方法并不是真正释放锁，必须等到synchronized方法或者语法块执行完才真正释放锁

    //注意：
//       流程上wait方法一定要在notify或者notifyAll方法前面，因为不唤醒的话会一直等待下去，造成线程死掉
    public static void main(String[] args) {
        System.out.println(args.length);
    }
}
