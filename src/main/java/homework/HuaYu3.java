package homework;

import java.math.BigInteger;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;


/**
 * Title: ForkJoin 分而治之  fork成一个个子任务 最后把子任务join到一起
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2019-12-06 16:06
 */
public class HuaYu3 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        BigInteger result = ForkJoinPool.commonPool()
                .submit(new MyForkJoin(50000))
                .invoke();
        long finish = System.currentTimeMillis();
        System.out.println("最终结果:" + result);
        System.out.println("用时:" + (finish - start) + "ms");
    }
}

class MyForkJoin extends RecursiveTask<BigInteger> {

    /**
     * 分割的阈值
     */
    private static final int FORK_THRESHOLD = 1000;

    /**
     * 阶乘底数起始值
     */
    private int factorialStart;

    /**
     * 阶乘底数结束值
     */
    private int factorialEnd;

    public MyForkJoin(Integer initNumber) {
        this.factorialStart = 1;
        this.factorialEnd = initNumber;
    }


    public MyForkJoin(Integer factorialStart, Integer factorialEnd) {
        this.factorialStart = factorialStart;
        this.factorialEnd = factorialEnd;
    }


    @Override
    protected BigInteger compute() {
        //如果计算的个数小于分割的阈值，则直接计算返回结果，不进行fork
        if (getFactorialAmount() < FORK_THRESHOLD) {
            return getFactorialResult();
        } else {
            //开始fork，对半分
            Integer middle = (this.factorialEnd + this.factorialStart) / 2;
            MyForkJoin myForkJoinOne = new MyForkJoin(this.factorialStart, middle);
            MyForkJoin myForkJoinTwo = new MyForkJoin(middle + 1, this.factorialEnd);
            myForkJoinOne.fork();
            myForkJoinTwo.fork();
            //计算完再join
            return myForkJoinOne.join().multiply(myForkJoinTwo.join());
        }
    }

    /**
     * 计算当前线程要准备计算多少个数
     *
     * @return
     */
    private int getFactorialAmount() {
        return this.factorialEnd - this.factorialStart + 1;
    }

    /**
     * 计算当前线程的"阶乘"
     */
    private BigInteger getFactorialResult() {
        BigInteger result = IntStream
                .rangeClosed(this.factorialStart, this.factorialEnd)
                .mapToObj(BigInteger::valueOf)
                .parallel()
                .reduce(BigInteger::multiply)
                .get();
        System.out.println(Thread.currentThread().getName() + ":计算" + this.factorialStart + "~" + this.factorialEnd);
        return result;
    }
}
