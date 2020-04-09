package juc.alearn.collections;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-09 20:15
 */

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * CopyOnWriteArrayList是ArrayList 的一个线程安全(对List的操作都使用ReentrantLock保证并发安全,读不加锁，写加锁)的变体，
 * 其中所有可变操作(add、set 等等)都是通过对底层数组进行一次新的拷贝来实现的。
 *
 *
 *
 * CopyOnWriteArrayList 有几个缺点：
 *      1.由于写操作的时候，需要拷贝数组，会消耗内存，如果原数组的内容比较多的情况下，
 *      可能导致young gc或者full gc 不能用于实时读的场景，像拷贝数组、新增元素都需要时间，
 *      所以调用一个set操作后，读取到数据可能还是旧的,虽然CopyOnWriteArrayList 能做到最终一致性,但是还是没法满足实时性要求；
 *
 *      2.CopyOnWriteArrayList 合适读多写少的场景，不过这类慎用，因为谁也没法保证CopyOnWriteArrayList 到底要放置多少数据，
 *      万一数据稍微有点多，每次add/set都要重新复制数组，这个代价实在太高昂了。在高性能的互联网应用中，这种操作分分钟引起故障。
 *
 * CopyOnWriteArrayList为什么并发安全且性能比Vector好?
 *      Vector对单独的add，remove等方法都是在方法上加了synchronized;
 *      并且如果一个线程A调用size时，另一个线程B 执行了remove，然后size的值就不是最新的，
 *      然后线程A调用remove就会越界(这时就需要再加一个Synchronized)。这样就导致有了双重锁，
 *      效率大大降低，何必呢。于是vector废弃了，要用就用CopyOnWriteArrayList 吧。
 */
public class MyCopyOnWriteArrayList {
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();
        list.add("");
        list.get(0);
    }
}
