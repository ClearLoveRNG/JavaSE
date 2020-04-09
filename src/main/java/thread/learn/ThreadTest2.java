package thread.learn;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Title: Callable接口  有返回值的线程
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time:2019/4/4 11:33
 */
public class ThreadTest2 {
    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Future<String> a = executorService.submit(new CallDemo(1));
        Future<String> b = executorService.submit(new CallDemo(2));
        Future<String> c = executorService.submit(new CallDemo(3));
        Future<String> d = executorService.submit(new CallDemo(4));
        Future<String> e = executorService.submit(new CallDemo(5));
        System.out.println(a.get());
        System.out.println(b.get());
        System.out.println(c.get());
        System.out.println(d.get());
        System.out.println(e.get());
    }
}

class CallDemo implements Callable<String> {

    private int id;

    public CallDemo(int id) {
        this.id = id;
    }

    @Override
    public String call() throws Exception {
        return "CallDemoId:" + id;
    }
}
