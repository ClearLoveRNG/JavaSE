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
 * HashTable也是线程安全的，为什么不常用呢？
 *      多线程环境下，使用JDK1.7的HashMap进行put操作会引起死循环，导致CPU利用率接近100%，
 *      所以在并发情况下不能使用HashMap。虽然已经有一个线程安全的HashTable，
 *      但是HashTable容器使用synchronized（他的get和put方法的实现代码如下）来保证线程安全，
 *      在线程竞争激烈的情况下HashTable的效率非常低下。因为当一个线程访问HashTable的同步方法时，
 *      访问其他同步方法的线程就可能会进入阻塞或者轮训状态。如线程1使用put进行添加元素，
 *      线程2不但不能使用put方法添加元素，并且也不能使用get方法来获取元素，所以竞争越激烈效率越低。
 *
 *
 * !!!!!!!!!!!!不管是JDK1.7还是1.8，ConcurrentHashMap中的key和value都不允许为null
 *      原因：并发环境，无法知道是真的没这个key还是有这个key，只不过是key=null
 *
 *
 *
 *
 * JDK1.5~1.7的ConcurrentHashMap使用分段锁机制实现，每一段表示一个Segment
 *      Segment通过继承 ReentrantLock 来进行加锁，所以每次需要加锁的操作锁住的是一个 segment，
 *      这样只要保证每个 Segment 是线程安全的，也就实现了全局的线程安全。
 *      Segment 数默认是 16，也就是说 ConcurrentHashMap 有 16 个 Segments，
 *      所以理论上，这个时候，最多可以同时支持 16 个线程并发写，只要它们的操作分别分布在不同的 Segment 上。
 *      这个值可以在初始化的时候设置为其他值，但是一旦初始化以后，它是不可以扩容的。
 *
 *      ConcurrentHashMap内部分为很多个Segment，每一个Segment拥有一把锁，然后每个Segment（继承ReentrantLock）下面包含很多个HashEntry列表数组。
 *      对于一个key，需要经过三次hash操作，才能最终定位这个元素的位置，这三次hash分别为：
 *      1.对于一个key，先进行一次hash操作，得到hash值h1，也即h1 = hash1(key)；
 *      2.将得到的h1的高几位进行第二次hash，得到hash值h2，也即h2 = hash2(h1高几位)，通过h2能够确定该元素的放在哪个Segment；
 *      3.将得到的h1进行第三次hash，得到hash值h3，也即h3 = hash3(h1)，通过h3能够确定该元素放置在哪个HashEntry。
 *
 *
 *
 *
 *
 *
 *
 *
 * JDK1.8则使用数组+链表+红黑树数据结构和CAS原子操作和synchronized实现ConcurrentHashMap
 *      提问一：
 *      ConcurrentHashMap(JDK1.8)为什么要放弃JDK1.7版本的Segment(分段锁)？
 *      1)锁的粒度
 *          1.7版本的CMAP锁的数量与并发等级concurrencyLevel有关，初始化后就确定了；
 *          而1.8版本后，锁的数量即为桶的数量；随着你的扩容会导致桶的数量翻倍，扩容后支持的最大并发访问数也同时翻倍；
 *          锁的粒度并没有变粗，甚至变得更细了。每当扩容一次，ConcurrentHashMap的并发度就扩大一倍。
 *      2)Hash冲突
 *          JDK1.7中，ConcurrentHashMap从过二次hash的方式（Segment -> HashEntry）能够快速的找到查找的元素。在1.8中通过链表加红黑树的形式弥补了put、get时的性能差距。
 *      3)扩容
 *          JDK1.8中，在ConcurrentHashMap进行扩容时，其他线程可以通过检测数组中的节点决定是否对这条链表（红黑树）进行扩容，减小了扩容的粒度，提高了扩容的效率。
 *
 *      提问二：
 *      为什么JDK1.8用的是synchronized进行加锁，而不继续用JDK1.7的ReentrantLock可重入锁？
 *      1)减少内存开销
 *          假设使用可重入锁来获得同步支持，那么每个节点都需要通过继承AQS来获得同步支持。但并不是每个节点都需要获得同步支持的，只有链表的头节点（红黑树的根节点）需要同步，这无疑带来了巨大内存浪费。
 *      2.)获得JVM的支持
 *          可重入锁毕竟是API这个级别的，后续的性能优化空间很小。
 *          synchronized则是JVM直接支持的，JVM能够在运行时作出相应的优化措施：锁粗化、锁消除、锁自旋等等。这就使得synchronized能够随着JDK版本的升级而不改动代码的前提下获得性能上的提升。
 *
 *
 *      必须先介绍一下sizeCtl属性，他有多种可能出现的值：
 *          当前未初始化:
 * 	            = 0  //未指定初始容量
 * 	            > 0  //由指定的初始容量计算而来，再找最近的2的幂次方。比如传入6，计算公式为6+6/2+1=10，最近的2的幂次方为16，所以sizeCtl就为16。
 *          初始化中：
 * 	            = -1 //table正在初始化
 * 	            = -N //N是int类型，分为两部分，高15位是指定容量n扩容标识，低16位表示,并行扩容线程数+1，具体在resizeStamp函数介绍。
 *          初始化完成：
 * 	            = table.length * 0.75  //扩容阈值调为table容量大小的0.75倍
 *
 *
 *
 *
 *     cas都是数组下标位置，一旦进入链表或者红黑树，则synchronized整个头结点，也就是当前槽
 *
 *     并发扩容总结
 *          1.单线程新建nextTable，新容量一般为原table容量的两倍。
 *          2.每个线程想增/删元素时，如果访问的桶是ForwardingNode节点，则表明当前正处于扩容状态，协助一起扩容完成后再完成相应的数据更改操作。
 *          3.扩容时将原table的所有桶倒序分配，每个线程每次最小分配16个桶，防止资源竞争导致的效率下降。单个桶内元素的迁移是加锁的，但桶范围处理分配可以多线程，在没有迁移完成所有桶之前每个线程需要重复获取迁移桶范围，直至所有桶迁移完成。
 *              补充：
 *                  原数组长度为 n，所以我们有 n 个迁移任务，让每个线程每次负责一个小任务是最简单的，
 *                  每做完一个任务再检测是否有其他没做完的任务，帮助迁移就可以了，而 Doug Lea 使用了一个 stride，简单理解就是步长，
 *                  每个线程每次负责迁移其中的一部分，如每次迁移 16 个小任务。所以，我们就需要一个全局的调度者来安排哪个线程执行哪几个任务，
 *                  这个就是属性 transferIndex 的作用。 第一个发起数据迁移的线程会将 transferIndex 指向原数组最后的位置，然后从后往前的 stride 个任务属于第一个线程，
 *                  然后将 transferIndex 指向新的位置，再往前的 stride 个任务属于第二个线程，依此类推。当然，这里说的第二个线程不是真的一定指代了第二个线程，也可以是同一个线程，
 *                  其实就是将一个大的迁移任务分为了一个个任务包。
 *          4.一个旧桶内的数据迁移完成但不是所有桶都迁移完成时，查询数据委托给ForwardingNode结点查询nextTable完成（这个后面看find()分析）。
 *          5.迁移过程中sizeCtl用于记录参与扩容线程的数量，全部迁移完成后sizeCtl更新为新table容量的0.75倍。
 *
 *
 *  详细信息：
 *      https://www.pdai.tech/md/java/thread/java-thread-x-juc-collection-ConcurrentHashMap.html
 */
public class MyConcurrentHashMap {
    public static void main(String[] args) {
        //初始化
        //get
        //put
        //扩容
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>(11);
        map.put(null,"");
        map.get("");
    }
}
