package proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * Title: JDK动态代理
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-13 01:20
 */
public class JdkProxyTest {
    public static void main(String[] args) {
        Gay gay = (Gay) Proxy.newProxyInstance(
                GayImpl.class.getClassLoader(),
                new Class[]{Gay.class},
                new GayJdkProxy(new GayImpl()));
        gay.gay();
    }
}
