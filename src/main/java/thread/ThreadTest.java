package thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Title:
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)˛
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/13 16:26
 */
public class ThreadTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        final int[] a = {20};
//        for(int i = 0; i < 20;i++){
//            int finalI = i;
//            new Thread(() -> {
//                System.out.println("【线程"+ finalI +":"+(a[0] = a[0] - 1)+"】");
//                try {
//                    System.out.println(Thread.currentThread().getName());
//                    Thread.currentThread().join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }).start();
//            System.out.println("【线程"+ finalI +"进入就绪状态】");
//        }
        String a = "1";
        String b = "2";
        final String c = "1";

        String d = c + "2";
        String e = "12";
        String f = "1" + "2";
        String g =  a + b;
        System.out.println(d==g);
    }
}
