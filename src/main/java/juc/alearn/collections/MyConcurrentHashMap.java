package juc.alearn.collections;

/**
 * Title:
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-08 11:42
 */

import java.util.concurrent.ConcurrentHashMap;

/**
 * JDK1.5~1.7的ConcurrentHashMap使用分段锁机制实现，每一段表示一个Segment
 *      Segment通过继承 ReentrantLock 来进行加锁，所以每次需要加锁的操作锁住的是一个 segment，
 *      这样只要保证每个 Segment 是线程安全的，也就实现了全局的线程安全。
 *      Segment 数默认是 16，也就是说 ConcurrentHashMap 有 16 个 Segments，
 *      所以理论上，这个时候，最多可以同时支持 16 个线程并发写，只要它们的操作分别分布在不同的 Segment 上。
 *      这个值可以在初始化的时候设置为其他值，但是一旦初始化以后，它是不可以扩容的。
 *
 *
 *
 * JDK1.8则使用数组+链表+红黑树数据结构和CAS原子操作和synchronized实现ConcurrentHashMap
 *
 *
 *
 *
 *
 */
public class MyConcurrentHashMap {
    public static void main(String[] args) {
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();
        map.put("","");
        map.get("");
    }
}
