package thread.homework;

import java.util.LinkedList;

/**
 * Title:线程池例子
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/15 16:49
 */
public class ThreadPool {
    // 线程池大小
    int threadPoolSize;

    // 任务容器
    LinkedList<Runnable> tasks = new LinkedList<>();

    // 试图消费任务的线程
    public ThreadPool() {
        //创建线程池时候，默认线程池大小为10，也就是有10个线程作为消费者
        threadPoolSize = 10;

        // 启动10个任务消费者线程
        synchronized (tasks) {
            for (int i = 0; i < threadPoolSize; i++) {
                new TaskConsumeThread("任务消费者线程 " + i).start();
            }
        }
    }
    //向任务容器中添加任务
    public void add(Runnable r) {
        //加上锁的原因是，假如任务容器有大小限制，则必须一个一个加任务，否则如果容器就剩最后一个地方可以放任务，但同时有两个线程进入方法添加任务，就会出现越界异常
        synchronized (tasks) {
            //添加一个任务到任务容器
            tasks.add(r);
            // 唤醒等待的任务消费者线程，可以执行容器里的任务了
            tasks.notifyAll();
        }
    }

    //消费者线程
    class TaskConsumeThread extends Thread {
        public TaskConsumeThread(String name) {
            super(name);
        }

        //每一个消费者线程执行的任务
        Runnable task;

        @Override
        public void run() {
            System.out.println("启动： " + this.getName());
            //线程一启动，则一直试图占有任务容器对象锁
            while (true) {
                synchronized (tasks) {
                    //一旦占有成功，则一直判断任务容器是否有任务
                    while (tasks.isEmpty()) {
                        try {
                            //如果没任务则临时释放任务容器对象锁，进入等待状态
                            tasks.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //一旦任务容器中有任务，则取出任务，因为是链表，所以取最后一个任务，也就是最早的任务开始执行
                    task = tasks.removeLast();
                    // 允许添加任务的线程可以继续添加任务
                    tasks.notifyAll();
                }
                System.out.println(this.getName() + " 获取到任务，并执行");
                //执行线程任务
                task.run();
            }
        }
    }
}
