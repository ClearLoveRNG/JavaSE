package lambda.learn;

import java.util.*;

/**
 * Title: lambda表达式
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time:2019/3/28 15:24
 */
public class Demo {
    public static void main(String[] args) {
        List<Student> studentList = new ArrayList<Student>(){
            {
                add(new Student("stu1", 100.0));
                add(new Student("stu2", 97.0));
                add(new Student("stu3", 96.0));
                add(new Student("stu4", 95.0));
            }
        };
        //正常排序
//        Collections.sort(studentList, new Comparator<Student>() {
//            @Override
//            public int compare(Student o1, Student o2) {
//                return Double.compare(o1.getScore(), o2.getScore());
//            }
//        });
        //使用lambda表达式
        Collections.sort(studentList, (o1,o2)->Double.compare(o1.getScore(), o2.getScore()));
        System.out.println(studentList);

        /**
         * Lambda表达式有两个特点：一是匿名函数，二是可传递。

         匿名函数的应用场景是：
         通常是在需要一个函数，但是又不想费神去命名一个函数的场合下使用Lambda表达式。lambda表达式所表示的匿名函数的内容应该是很简单的，如果复杂的话，干脆就重新定义一个函数了，使用lambda就有点过于执拗了。

         可传递使用场景是：
         就是将Lambda表达式传递给其他的函数，它当做参数，Lambda作为一种更紧凑的代码风格，使Java的语言表达能力得到提升。
         */

        /**
         * lambda语法(引用类或者对象中需要重写的方法)
             1.多参数
             (1)lambda表达式的基本格式为(x1,x2)->{表达式...};
                 例子：(o1,o2)->Double.compare(o1.getScore(), o2.getScore())
             (2)在上式中，lambda表达式带有两个参数，此时参数类型可以省略，但两边的括号不能省略
             (3)如果表达式只有一行，那么表达式两边的花括号可以省略
             2.无参数
             (1）参数的括号不能省略
                 例子：new Thread(()-> System.out.println("hello, i am thread!")).start();
             (2）其他语法同多参数
             3.一个参数
             (1)可以省略参数的括号和类型
                 例子：x-> System.out.println(x)
             (2)其他语法同多参数
         */

        /**
         * 方法引用(引用类或者对象中已存在的方法)
         * Collections.sort(studentList, Comparator.comparingDouble(Student::getScore));
         * 上面使用lambda排序语句可以替换为上方的语句，使用的是方法引用。
         * 使用条件：只要方法的参数和返回值类型与函数式接口中抽象方法的参数和返回值类型一致，就可以使用方法引用。
         * getScore()和applyAsDouble()的返回值都是Double
         * 方法引用主要有如下三种使用情况
             (1)类::实例方法
             (2)类::静态方法
             (3)对象::实例方法
           其中后两种情况等同于提供方法参数的lambda表达式
         */
    }
}


class Student {
    private String name;
    private Double score;

    public Student() {
    }

    public Student(String name, Double score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "{"
                + "\"name\":\"" + name + "\""
                + ", \"score\":\"" + score + "\""
                + "}";
    }
}
