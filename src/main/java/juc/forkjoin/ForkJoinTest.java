package juc.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

/**
 * Title:
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2019-12-06 15:35
 */
public class ForkJoinTest {
    public static void main(String[] args) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        forkJoinPool.execute(new MyForkJoin());
    }
}
