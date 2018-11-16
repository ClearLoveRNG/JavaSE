package thread.homework.consumerandproducer;

/**
 * Title:消费者
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/16 15:47
 */
public class Consumer extends Thread {
    //消费的数量
    private int consumeNum;

    //仓库
    private Warehouse warehouse;

    //消费者者名字
    private String name;

    //构造方法
    public Consumer(int consumeNum, Warehouse warehouse, String name) {
        this.consumeNum = consumeNum;
        this.warehouse = warehouse;
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (warehouse) {
            //如果消费后的数量小于0，则释放锁，进入等待池
            System.out.println(name + ",当前仓库库存量:" + warehouse.getCurrentSize());
            while (warehouse.getCurrentSize() - consumeNum < 0) {
                try {
                    System.out.println(name + ",仓库-" + consumeNum + "后小于0,进入等待");
                    warehouse.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //如果可以消费，则消费后唤醒其他线程进行生产或消费操作
            warehouse.consume(consumeNum);
            System.out.println(name + ",操作:-" + consumeNum);
            System.out.println("---------------------------");
            warehouse.notifyAll();
        }
    }
}
