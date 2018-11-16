package annotation.learn;

/**
 * Title:注解练习
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/16 10:28
 */
@MyAnnotation(s1 = "哈哈",i2 = 999)
public class AnnotationTest {
    //@Target
//        @Target 表示这个注解能放在什么位置上，是只能放在类上？还是即可以放在方法上，又可以放在属性上。自定义注解@JDBCConfig 这个注解上的@Target是：@Target({METHOD,TYPE})，表示他可以用在方法和类型上（类和接口），但是不能放在属性等其他位置。 可以选择的位置列表如下：
//        ElementType.TYPE：能修饰类、接口或枚举类型
//        ElementType.FIELD：能修饰成员变量
//        ElementType.METHOD：能修饰方法
//        ElementType.PARAMETER：能修饰参数
//        ElementType.CONSTRUCTOR：能修饰构造器
//        ElementType.LOCAL_VARIABLE：能修饰局部变量
//        ElementType.ANNOTATION_TYPE：能修饰注解
//        ElementType.PACKAGE：能修饰包
    //@Retention
//        @Retention 表示生命周期
//        RetentionPolicy.SOURCE： 注解只在源代码中存在，编译成class之后，就没了。@Override 就是这种注解。
//        RetentionPolicy.CLASS： 注解在java文件编程成.class文件后，依然存在，但是运行起来后就没了。@Retention的默认值，即当没有显式指定@Retention的时候，就会是这种类型。
//        RetentionPolicy.RUNTIME： 注解在运行起来之后依然存在，程序可以通过反射获取这些信息
    //@Inherited
//        @Inherited 表示该注解具有继承性。如例，有一个父类(father)的子类(son)，son的方法，可以获取到father上的注解信息。
    //@Documented
//        在用javadoc命令生成API文档后，使用该注解的类的文档里会出现该注解说明。
    //@Repeatable (java1.8 新增)
//        当没有@Repeatable修饰的时候，注解在同一个位置，只能出现一次
//        使用@Repeatable之后，再配合一些其他动作，就可以在同一个地方使用多次了。
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("annotation.learn.AnnotationTest");
        MyAnnotation myAnnotation = clazz.getAnnotation(MyAnnotation.class);
        if(myAnnotation == null){
            System.out.println("没有MyAnnotation注解");
        } else {
            System.out.println(myAnnotation.i2());
            System.out.println(myAnnotation.s1());
        }
    }
}
