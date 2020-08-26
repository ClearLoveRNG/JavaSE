import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Title:
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time: 2019-06-30 18:53
 */
public class Test {

    public static void main(String[] args) throws Exception {
        if(false){
            System.out.println(1);
        }
        Integer a = 1;
        Integer b = 2;
        Integer c = 3;
        Integer d = 3;
        Integer e = 321;
        Integer f = 321;
        Integer z = 320;
        Long g = 3L;
        //1.Integer如果在-127至128之间，会使用Integer在缓存中创建好的对象，超过这个范围才在堆上new新对象
        //2.包装类使用==进行比较的时候，如果遇到运算符会进行拆箱，没有运算符则一直保持装箱状态
        //3.包装类使用equal()方法进行比较的时候,如果两边的类型不一样，则不进行类型转换

        //1
        System.out.println(c == d);//true
        System.out.println(e == f);//false
        //2
        System.out.println(c == (a + b));//true
        System.out.println(c.equals(a + b));//true
        //3
        System.out.println(g == (a + b));//true
        System.out.println(g.equals(a + b));//false
    }

}



