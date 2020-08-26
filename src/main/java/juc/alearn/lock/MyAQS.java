package juc.alearn.lock;


import java.util.concurrent.locks.AbstractQueuedSynchronizer;
/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-03 13:11
 */
/**
 *  AQS是一个用来构建锁和同步器的框架，使用AQS能简单且高效地构造出应用广泛的大量的同步器，
 *  比如ReentrantLock，Semaphore，其他的诸如ReentrantReadWriteLock，SynchronousQueue，FutureTask等等皆是基于AQS的。
 *  当然，我们自己也能利用AQS非常轻松容易地构造出符合我们自己需求的同步器。
 *
 *  AQS核心思想是，如果被请求的共享资源空闲，则将当前请求资源的线程设置为有效的工作线程，并且将共享资源设置为锁定状态。
 *  如果被请求的共享资源被占用，那么就需要一套线程阻塞等待以及被唤醒时锁分配的机制，这个机制AQS是用CLH队列锁实现的，即将暂时获取不到锁的线程加入到队列中。
 *  其中Sync queue，即同步队列，是双向链表，包括head结点和tail结点，head结点主要用作后续的调度。
 *  而Condition queue不是必须的，其是一个单向链表，只有当使用Condition时，才会存在此单向链表。并且可能会有多个Condition queue。
 *
 *  每一个结点都是由前一个结点唤醒 当结点发现前驱结点是head并且尝试获取成功，则会轮到该线程运行。
 *  condition queue中的结点向sync queue中转移是通过signal操作完成的。 当结点的状态为SIGNAL时，表示后面的结点需要运行。
 *
 *        +-----------+
 *        |  共享资源  |
 *        +-----------+
 *
 *
 *
 *        +------------+       +------------+       +------------+
 *        | waitStatus |  prev | waitStatus |  prev | waitStatus |
 *        |    prev    | <---- |    prev    | <---- |    prev    |
 *   head |    next    |       |    next    |       |    next    | tail
 *        |   thread   |  next |   thread   |  next |   thread   |
 *        | nextWaiter | ----> | nextWaiter | ----> | nextWaiter |
 *        +------------+       +------------+       +------------+
 *
 *
 *
 *     结点状态
 *          volatile int waitStatus
 *              CANCELLED，值为1，表示当前的线程被取消
 *              SIGNAL，值为-1，表示当前节点的后继节点包含的线程需要运行，也就是unpark
 *              CONDITION，值为-2，表示当前节点在等待condition，也就是在condition队列中
 *              PROPAGATE，值为-3，表示当前场景下后续的acquireShared能够得以执行
 *              值为0，表示当前节点在sync队列中，等待着获取锁
 *     前驱结点
 *          volatile Node prev
 *     后继结点
 *          volatile Node next
 *     结点所对应的线程
 *          volatile Thread thread
 *     下一个等待者
 *          Node nextWaiter
 *
 *  AQS定义两种资源共享方式
 *  Exclusive(独占)：只有一个线程能执行，如ReentrantLock。又可分为公平锁和非公平锁：
 *         公平锁：按照线程在队列中的排队顺序，先到者先拿到锁
 *         非公平锁：当线程要获取锁时，无视队列顺序直接去抢锁，谁抢到就是谁的
 *  Share(共享)：多个线程可同时执行，如Semaphore/CountDownLatch。Semaphore、CountDownLatch、 CyclicBarrier、ReadWriteLock
 *
 *
 *  使用者继承AbstractQueuedSynchronizer并重写指定的方法。(这些重写方法很简单，无非是对于共享资源state的获取和释放)
 *  将AQS组合在自定义同步组件的实现中，并调用其模板方法，而这些模板方法会调用使用者重写的方法。
 *  AQS使用了模板方法模式，自定义同步器时需要重写下面几个AQS提供的模板方法：
 *
 *  isHeldExclusively()  该线程是否正在独占资源。只有用到condition才需要去实现它。
 *  tryAcquire(int)      独占方式。尝试获取资源，成功则返回true，失败则返回false。
 *  tryRelease(int)      独占方式。尝试释放资源，成功则返回true，失败则返回false。
 *  tryAcquireShared(int)共享方式。尝试获取资源。负数表示失败；0表示成功，但没有剩余可用资源；正数表示成功，且有剩余资源。
 *  tryReleaseShared(int)共享方式。尝试释放资源，成功则返回true，失败则返回false。
 *
 *
 *  默认情况下，每个方法都抛出 UnsupportedOperationException。
 *  这些方法的实现必须是内部线程安全的，并且通常应该简短而不是阻塞。
 *  AQS类中的其他方法都是final,所以无法被其他类使用，只有这几个方法可以被其他类使用
 *
 *
 *  两大核心方法：acquire()和release()
 *  acquire():
 *      public final void acquire(int arg) {
 *          if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
 *              selfInterrupt();
 *      }
 *      tryAcquire()尝试直接去获取资源，如果成功则直接返回；
 *      addWaiter()将该线程加入等待队列的尾部，并标记为独占模式；
 *      acquireQueued()使线程在等待队列中获取资源，一直获取到资源后才返回。
 *      获取的时候会处于自旋状态，会一直检测两点：
 *      1.前驱是head
 *      2.能获取资源
 *      如果这两点都满足则把自己变为head节点
 *      如果有一个不满足，则会找寻休息点，把前驱节点的等待状态设为-1，然后自己阻塞(park)，等待前驱唤醒
 *      唤醒后会继续检测那两个点，然后循环上述几个操作，直到获取资源并成为head节点
 *
 *      如果在整个等待过程中被中断过，则返回true，否则返回false。
 *      如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
 *
 *
 *  release():
 *          public final boolean release(int arg) {
 *              if (tryRelease(arg)) {
 *                  Node h = head;
 *                  if (h != null && h.waitStatus != 0)
 *                      unparkSuccessor(h);
 *                  return true;
 *              }
 *              return false;
 *          }
 *      tryRelease()尝试释放资源
 *      unparkSuccessor()唤醒等待队列里，下一个没有放弃的线程(waitStatus<=0)
 *
 *      释放资源之后，如果自己的等待状态是-1，则会唤醒等待状态<=0的节点(unpark)，并把head指向后缀节点，然后把自己干掉，方便GC
 *
 *  独占：
 *      独占式的锁会去判断是否为后继节点，只有后继节点才有资格在头节点释放了同步状态以后获取到同步状态
 *  共享：
 *
 *
 *
 *  详细文章：https://www.jianshu.com/p/0f6d3530d46b
 *          https://blog.csdn.net/wry7162416/article/details/89323707
 */
public class MyAQS extends AbstractQueuedSynchronizer {


    @Override
    protected boolean tryRelease(int arg) {
        return super.tryRelease(arg);
    }

    @Override
    protected boolean tryAcquire(int arg) {
        return super.tryAcquire(arg);
    }

    @Override
    protected boolean isHeldExclusively() {
        return super.isHeldExclusively();
    }

    public static void main(String[] args) {

    }
}
