package thread;

import java.time.Instant;
import java.util.*;
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
//        List<Integer> list = new ArrayList<>();
//        for(int i = 0;i<2000000;i++){
//            list.add(i);
//        }
//        for(int i = 0;i<10;i++){
//            Collections.shuffle(list);
//            long start = System.currentTimeMillis();
//            Integer target = 15609;
//            for(Integer integer : list){
//                if(integer.equals(target)){
//                    System.out.println("找到了！");
//                    break;
//                }
//            }
//            System.out.println("第"+i+"次查找共花费"+(System.currentTimeMillis()-start)+"毫秒");
//        }

        Map<String,Integer> map = new HashMap<>();
        for(int i = 0;i<2000000;i++){
            map.put(String.valueOf(i),i);
        }
        for(int i = 0;i<10;i++){
            long start = System.currentTimeMillis();
            String target = "15469";
            map.get(target);
            System.out.println("找到了");
            System.out.println("第"+i+"次查找共花费"+(System.currentTimeMillis()-start)+"毫秒");
        }
    }
}
