package design_patterns.createdmode;

/**
 * Title: 创建性模式-原型模式
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time:2019/1/25 15:03
 */

/**
 * 原型模式就是讲一个对象作为原型，使用clone()方法来创建新的实例。
 * 此处使用的是浅拷贝。
 */
public class Prototype implements Cloneable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        Prototype pro = new Prototype();
        Prototype pro1 = (Prototype) pro.clone();
    }
}
