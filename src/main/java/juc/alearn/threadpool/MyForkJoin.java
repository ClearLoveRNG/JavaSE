package juc.alearn.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * Title:  使用ForkJoin计算1+2+………………+99+100
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-27 01:54
 */
public class MyForkJoin {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MyTask myTask = new MyTask(1, 100);
        System.out.println(forkJoinPool.invoke(myTask));
    }
}

class MyTask extends RecursiveTask<Integer> {

    private int start;
    private int end;

    public MyTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        if (end - start < 10) {
            System.out.println(Thread.currentThread().getName() + " 开始执行: " + start + "-" + end);
            return IntStream.rangeClosed(start, end).sum();
        }
        int i = (end + start) / 2;
        MyTask task1 = new MyTask(start, i);
        MyTask task2 = new MyTask(i+1, end);
        task1.fork();
        task2.fork();
        return task1.join() + task2.join();
    }
}
