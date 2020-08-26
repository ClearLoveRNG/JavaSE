package jvm;


/**
 * Title:
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time: 2019-06-27 19:10
 */


/**
 * JDK1.7 类元数据、静态变量、常量池等放入方法区
 * JDK1.8 类元数据放到本地内存，不受JVM限制，静态变量、常量池改为在堆内生成
 *        permSize（方法区）：原来的jar包及你自己项目的class存放的内存空间，这部分空间是固定的，启动参数里面-permSize确定，如果你的jar包很多，经常会遇到permSize溢出，且每个项目都会占用自己的permGen空间
 * 　　　　metaSpaces（元空间），各个项目会共享同样的class内存空间，比如两个项目都用了fast-json开源包，在metaSpaces里面只存一份class，提高内存利用率，且更利于垃圾回收
 *
 * JVM启动参数共分为三类：
 * 　　1、标准参数（-），所有的JVM实现都必须实现这些参数的功能，而且向后兼容。例如：-verbose:class（输出jvm载入类的相关信息，当jvm报告说找不到类或者类冲突时可此进行诊断）；-verbose:gc（输出每次GC的相关情况）；-verbose:jni（输出native方法调用的相关情况，一般用于诊断jni调用错误信息）。
 * 　　2、非标准参数（-X），默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容。例如：-Xms512m；-Xmx512m；-Xmn200m；-Xss128k）。
 * 　　3、非稳定参数（-XX），此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用。例如：-XX:PermSize=64m；-XX:MaxPermSize=512m。
 *
 * 堆大小（-Xmx） = 新生代+老年代+元空间
 * JDK1.8 默认参数
 *      新生代:老年代=1：2
 *      新生代GC收集器：Parallel Scavenge
 *      老年代收集器：ParallelOld
 *      Eden：S1：S2 = 8：1：1
 *      会自动打开自适应划分开关，所以新生代和老年代大小会随时改变
 *
 * -Xms128m
 * -Xmx128m
 * -XX:+PrintGCDetails
 * -XX:+PrintGCDateStamps
 * -XX:+PrintGCTimeStamps
 * -XX:
 *
 * GC调优原理：
 *      1.延时：要求一个请求在几秒内做出响应
 *      2.吞吐量：要求同时处理1万个用户的请求
 *      3.容量：为了达到1和2的目的所哟啊付出的内存容量
 *
 *
 *
 *
 *
 *
 *
 *
 */
public class JvmTest {
    public static void main(String[] args) {
        //1024字节 = 1KB
        //1024*1024字节 = 1MB
        //
        byte[] bytes = new byte[1024 * 1024 * 15];

    }
}
