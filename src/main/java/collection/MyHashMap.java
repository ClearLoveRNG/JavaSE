package collection;


/**
 * @author 蒋浩天
 */

import java.util.HashMap;

/**
 * HashMap 和 HashTable 区别
 *  1.继承的父类不同
 *       Hashtable继承自Dictionary类，而HashMap继承自AbstractMap类。但二者都实现了Map接口。
 *  2.线程安全性不同
 *      hashMap不安全，hashTable安全
 *  3.是否提供contains方法
 *      HashMap把Hashtable的contains方法去掉了，改成containsValue和containsKey，因为contains方法容易让人引起误解。
 *      Hashtable则保留了contains，containsValue和containsKey三个方法，其中contains和containsValue功能相同。
 *  4.key和value是否允许null值
 *      Hashtable中，key和value都不允许出现null值。
 *      HashMap中，null可以作为键，这样的键只有一个；可以有一个或多个键所对应的值为null。
 *  5.两个遍历方式的内部实现上不同
 *      Hashtable、HashMap都使用了Iterator。而由于历史原因，Hashtable还使用了Enumeration的方式。
 *  6.hash值不同
 *      HashTable直接使用对象的hashCode。而HashMap重新计算hash值。
 *  7.内部实现使用的数组初始化和扩容方式不同
 *      HashTable在不指定容量的情况下的默认容量为11，而HashMap为16，Hashtable不要求底层数组的容量一定要为2的整数次幂，而HashMap则要求一定为2的整数次幂。
 *      Hashtable扩容时，将容量变为原来的2倍加1，而HashMap扩容时，将容量变为原来的2倍。
 *
 *      Hashtable和HashMap它们两个内部实现方式的数组的初始大小和扩容的方式。HashTable中hash数组默认大小是11，增加的方式是 old*2+1。
 *  为什么HashTable慢
 *        Hashtable之所以效率低下主要是因为其实现使用了synchronized关键字对put等操作进行加锁，
 *        而synchronized关键字加锁是对整个对象进行加锁，也就是说在进行put等修改Hash表的操作时，
 *        锁住了整个Hash表，从而使得其表现的效率低下。
 *
 *
 * JDK1.7和JDK1.8 HashMap的区别
 *      1.7哈希冲突只有链表，1.8不仅有链表还会有红黑树
 *      1.7哈希冲突是头插法，1.8尾插法
 *      1.8hash算法简化
 *      1.7并发扩容会生成死循环，1.8扩容逻辑修改，不会死循环
 *
 *
 * JDK基本问题
 *      基本属性
 *      initialCapacity:Node数组的大小，默认16
 *      loadFactor：负载因子，默认0.75f
 *      threshold：扩容的阈值，默认initialCapacity * loadFactor
 *      size：Node节点的数量，put时候+1，remove时候-1
 *
 *
 * 1.JDK1.7死循环问题
 *      https://blog.csdn.net/xuefeng0707/article/details/40797085
 * 2.为什么阈值是0.75
 *      大致意思就是说负载因子是0.75的时候，空间利用率比较高，而且避免了相当多的Hash冲突，使得底层的链表或者是红黑树的高度比较低，提升了空间效率。
 * 3.为什么链表长度>8才去转红黑树
 *      空间和时间的权衡。
 *      红黑树节点占用空间是普通节点的两倍，所以只有当链表包含足够多的节点时才会转成红黑树节点，而是否足够多就是由TREEIFY_THRESHOLD的值决定的(默认8)。
 *      当桶中节点数变少时，又会转成普通的链表。并且我们查看源码的时候发现，链表长度达到8就转成红黑树，当长度降到6就转成普通链表。
 *      这样就解析了为什么不是一开始就将其转换为红黑树，而是需要一定节点数才转为红黑树，说白了就是空间和时间的权衡
 *      当hashCode离散性很好的时候，红黑树用到的概率非常小，因为数据均匀分布在每个桶中，几乎不会有桶中链表长度会达到阈值。
 *      但是在随机hashCode下，离散性可能会变差，然而JDK又不能阻止用户实现这种不好的hash算法，因此就可能导致不均匀的数据分布。
 *      不过理想情况下随机hashCode算法下所有桶中节点的分布频率会遵循泊松分布，我们可以看到，一个桶中链表长度达到8个元素的概率为0.00000006，几乎是不可能事件
 * 4.为什么map长度必须是2的幂次方
 *     1）当length总是2的n次方时，h & (length-1)运算等价于对length取模，也就是h%length，但是&比%具有更高的效率。
 *     2）当length总是2的n次方时，length - 1 一定是奇数，所以末位一定是1，
 *        所以用hash去 & 操作的时候，末位的1 & key的末位，有可能是1也可能是0，所以获得的数组下标既可能是奇数也可能是偶数，散列效果更好
 *        而length - 1如果不是奇数而是偶数的话，偶数的末位是0，而0 & 任何东西，都是0，所以只会把元素散列到偶数下标里，几乎浪费一半的空间
 * 5.为什么初始化容量是16
 *      如果小了比如用8或者4，很容易扩容影响性能，而大了比如64或者128等，又会浪费空间
 * 6.如何定位桶的下标
 *      取key的hashCode值
 *          String hashCode = key.hashCode()
 *      用高位运算进行扰动(高16位 按位异或 低16位)，拿到hash值
 *          int hash = hashCode ^ hashCode >>> 16
 *      用hash值进行取模运算，获得下标
 *          int index = hash & (table.length - 1)
 * 7.转红黑树的条件
 *      1）数组长度大于64
 *      2）链表长度大于8
 */

public class MyHashMap{
    public static void main(String[] args) {

        //初始化
        //get
        //put
        //扩容

        HashMap<String, String> hashMap = new HashMap<>(8);
        hashMap.put("姓名","蒋浩天");
        hashMap.put("职业","Java");
        hashMap.get("");

        while(hashMap.keySet().iterator().hasNext()){
            hashMap.remove(hashMap.keySet().iterator().next());
        }


//        int hash1 = hash("姓名");
//        int index1 = hash1 & 15;
//        System.out.println("姓名的hash值:"+ hash1 +",数组下标:"+index1);
//
//
//        int hash2 = hash("职业");
//        int index2 = hash2 & 15;
//        System.out.println("职业的hash值:"+hash2+",数组下标:"+index2);
    }

    public static int hash(Object key) {
        int h = key.hashCode();
        System.out.println(key+"的hashcode二进制表示:"+Integer.toBinaryString(h));
        int i = h >>> 16;
        System.out.println(key+"的hashcode右移动16位二进制表示:"+Integer.toBinaryString(i));
        return h ^ i;
    }
}