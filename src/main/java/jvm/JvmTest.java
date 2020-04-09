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
class JvmTest {
    public static void main(String[] args) {
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

class InnerClass {
    public int innerMethod(int i) {
        return i + 1;
    }
}
