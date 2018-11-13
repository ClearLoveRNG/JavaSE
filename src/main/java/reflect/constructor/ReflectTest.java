package reflect.constructor;


/**
 * Title:获取Class对象
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: reflect.constructor
 * Author: jianghaotian
 * Create Time:2018/11/2 14:48
 */
public class ReflectTest {
    /*
     * 注意：在运行期间，一个类，只有一个Class对象产生。
     * 三种方式常用第三种，
     * 第一种对象都有了还要反射干什么。
     * 第二种需要导入类的包，依赖太强，不导包就抛编译错误。
     * 一般都第三种，一个字符串可以传入也可写在配置文件中等多种方法。
     *
     */
    public static void main(String[] args) {
        //第一种方式获取Class对象
        //Object类中的getClass方法,因为所有类都继承Object类,从而调用Object类来获取
        //这一new，产生两个东西，一个Student对象，一个Class对象。
        Student stu1 = new Student();
        //获取Class对象
        Class stuClass = stu1.getClass();
        System.out.println(stuClass.getName());

        //第二种方式获取Class对象
        Class stuClass2 = Student.class;
        //判断第一种方式获取的Class对象和第二种方式获取的是否是同一个
        System.out.println(stuClass == stuClass2);

        //第三种方式获取Class对象
        try {
            //注意此字符串必须是真实路径，就是带包名的类路径，包名.类名
            Class stuClass3 = Class.forName("reflect.constructor.Student");
            //判断三种方式是否获取的是同一个Class对象
            System.out.println(stuClass3 == stuClass2);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
