package thread;


/**
 * Title:生产者消费者模型
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)˛
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/13 16:26
 */
public class ThreadTest {
    public static void main(String[] args){
        //创建一个线程池
        ThreadPool pool = new ThreadPool();

        //将任务加入到线程池里的任务容器中,这里加了5个任务
        for (int i = 0; i < 5; i++) {
            //每个任务都是要打印"执行任务"
            Runnable task = () -> {
                System.out.println("执行任务");
                //任务可能是打印一句话
                //可能是访问文件
                //可能是做排序
            };

            pool.add(task);

            try {
                //sleep主线程，让刚刚添加的任务有充足的时间(1000毫秒)运行完成
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
