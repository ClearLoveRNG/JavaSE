package proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Title: GayImpl增强类
 * Description:
 * Company:
 * Project: JavaSE
 *
 * @Author: jianghaotian
 * Create Time: 2020-04-13 01:22
 */
public class GayJdkProxy implements InvocationHandler {

    private Object o;

    public GayJdkProxy(Object o) {
        this.o = o;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("开始gay");
        method.invoke(o,args);
        System.out.println("gay完了");
        return proxy;
    }
}
