package thread.homework.consumerandproducer;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:生产者
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/16 15:43
 */
public class Producer extends Thread {
    //生产的数量
    private int produceNum;

    //仓库
    private Warehouse warehouse;

    //生产者名字
    private String name;

    //构造方法
    public Producer(int produceNum, Warehouse warehouse, String name) {
        this.produceNum = produceNum;
        this.warehouse = warehouse;
        this.name = name;
    }

    @Override
    public void run() {
        synchronized (warehouse) {
            //如果生产后的数量大于仓库的最大数量，则释放锁，进入等待池
            System.out.println(name + ",当前仓库库存量:" + warehouse.getCurrentSize());
            while(warehouse.getCurrentSize() + produceNum > Warehouse.warehouseSize) {
                try {
                    System.out.println(name + ",仓库+" + produceNum + "后超过最大库存,进入等待");
                    warehouse.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            //如果可以生产，则生产后唤醒其他线程进行生产或消费操作
            warehouse.produce(produceNum);
            System.out.println(name + ",操作:+" + produceNum);
            System.out.println("---------------------------");
            warehouse.notifyAll();
        }
    }
}
