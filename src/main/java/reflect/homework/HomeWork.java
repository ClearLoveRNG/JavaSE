package reflect.homework;

import java.lang.reflect.Method;

/**
 * Title:反射调用student的main方法
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/13 15:04
 */
public class HomeWork {
    public static void main(String[] args) throws Exception {
        //获取Student对象字节码文件
        Class<?> clazz = Class.forName("reflect.homework.Student");
        //获取main方法对象
        Method method = clazz.getDeclaredMethod("main",String[].class);
        //因为main方法是静态的，不需要实例化student对象即可调用，所以不需要这么写
//        Object obj = clazz.newInstance();
//        method.invoke(obj,new String[]{"a","b","c"});

        //第一个参数，对象类型，因为方法是static静态的，所以为null可以，第二个参数是String数组，这里要注意在jdk1.4时是数组，jdk1.5之后是可变参数
        // methodMain.invoke(null, new String[]{"a","b","c"});
        //这里拆的时候将  new String[]{"a","b","c"} 拆成3个对象。。。所以需要将它强转。
        method.invoke(null, (Object)new String[]{"a","b","c"});//方式一
        // methodMain.invoke(null, new Object[]{new String[]{"a","b","c"}});//方式二
    }
}
