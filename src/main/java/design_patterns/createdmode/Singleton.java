package design_patterns.createdmode;

/**
 * Title: 创建性模式-单例模式
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time:2019/1/25 14:50
 */

/**
 * 在内部创建一个实例，构造器全部设置为private，所有方法均在该实例上改动，在创建上要注意类的实例化只能执行一次，可以采用许多种方法来实现，如Synchronized关键字，或者利用内部类等机制来实现。
 */
public class Singleton {
    private Singleton() {
    }

    private static class SingletonBuild {
        private static Singleton value = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonBuild.value;
    }
}

class Client {
    public static Singleton getSingleton() {
        return Singleton.getInstance();
    }
}