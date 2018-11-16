package thread.homework.consumerandproducer;

/**
 * Title:生产者消费者模型
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/16 15:50
 */
public class ThreadTest {
    public static void main(String[] args) {
        Warehouse warehouse = new Warehouse(0);

        Producer producer1 = new Producer(23,warehouse,"生产者1");
        Producer producer2 = new Producer(57,warehouse,"生产者2");
        Producer producer3 = new Producer(16,warehouse,"生产者3");
        Producer producer4 = new Producer(79,warehouse,"生产者4");
        Producer producer5 = new Producer(33,warehouse,"生产者5");

        Consumer consumer1 = new Consumer(25,warehouse,"消费者1");
        Consumer consumer2 = new Consumer(80,warehouse,"消费者2");
        Consumer consumer3 = new Consumer(8,warehouse,"消费者3");
        Consumer consumer4 = new Consumer(67,warehouse,"消费者4");
        Consumer consumer5 = new Consumer(12,warehouse,"消费者5");

        producer1.start();
        producer2.start();
        producer3.start();
        producer4.start();
        producer5.start();

        consumer1.start();
        consumer2.start();
        consumer3.start();
        consumer4.start();
        consumer5.start();
    }
}
