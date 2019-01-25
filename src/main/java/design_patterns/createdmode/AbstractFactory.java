package design_patterns.createdmode;

/**
 * Title: 创建性模式-抽象工厂模式
 * Description:
 * Copyright: 2019 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time:2019/1/25 14:41
 */

/**
 * 一个基础接口定义了功能，每个实现接口的子类就是产品，然后定义一个工厂接口，实现了工厂接口的就是工厂，这时候，接口编程的优点就出现了，我们可以新增产品类（只需要实现产品接口），只需要同时新增一个工厂类，客户端就可以轻松调用新产品的代码。
 * 抽象工厂的灵活性就体现在这里，无需改动原有的代码，毕竟对于客户端来说，静态工厂模式在不改动StaticFactory类的代码时无法新增产品，如果采用了抽象工厂模式，就可以轻松的新增拓展类。
 */


//interface food {
//}
//
//class A implements food {
//}
//
//class B implements food {
//}
//
//interface produce {
//    food get();
//}
//
//class FactoryForA implements produce {
//    @Override
//    public food get() {
//        return new A();
//    }
//}
//
//class FactoryForB implements produce {
//    @Override
//    public food get() {
//        return new B();
//    }
//}
//
//public class AbstractFactory {
//    public void ClientCode(String name) {
//        food x = null;
//        if (name.equals("A")) {
//            x = new FactoryForA().get();
//        } else if (name.equals("B")) {
//            x = new FactoryForB().get();
//        }
//    }
//}
