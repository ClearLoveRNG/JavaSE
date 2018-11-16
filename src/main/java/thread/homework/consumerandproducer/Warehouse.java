package thread.homework.consumerandproducer;

/**
 * Title:仓库
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/16 15:49
 */
public class Warehouse {
    //仓库最大数量
    public static int warehouseSize = 80;

    //当前仓库数量
    private int currentSize;

    //构造方法
    public Warehouse(int currentSize){
        this.currentSize = currentSize;
    }

    //添加库存
    public void produce(int produceNum){
        this.currentSize += produceNum;
    }

    //消耗库存
    public void consume(int consumeNum){
        this.currentSize -= consumeNum;
    }

    //获取当前仓库数量
    public int getCurrentSize() {
        return currentSize;
    }
}
