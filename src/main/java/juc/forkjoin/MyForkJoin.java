package juc.forkjoin;

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
 * Create Time: 2019-12-06 15:49
 */
public class MyForkJoin extends RecursiveAction {

    @Override
    protected void compute() {
        ForkJoinTask.invokeAll();
    }
}
