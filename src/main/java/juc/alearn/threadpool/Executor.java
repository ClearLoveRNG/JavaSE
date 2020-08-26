package juc.alearn.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-08-23 16:53
 */

/**
 * 多线程的线程管理：线程池
 */
public class Executor {
    public static void main(String[] args) {
        Executors.newFixedThreadPool(1);
    }
}
