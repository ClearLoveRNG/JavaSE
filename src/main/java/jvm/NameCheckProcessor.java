package jvm;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * Title: 注解处理器
 * Description:
 * Copyright: 2019
 * Company: 滴普科技
 * Project: JavaSE
 * Author: jianghaotian
 * Create Time: 2019-06-29 17:37
 */
//这个注解器对哪些注解感兴趣,*号表示对所有注解都感兴趣
@SupportedAnnotationTypes("*")
//这个注解处理器可以处理哪些版本的Java代码
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class NameCheckProcessor extends AbstractProcessor {


    private NameChecker nameChecker;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    /**
     * javac编译器在执行注解处理器代码要调用此方法
     * 对输入的语法树的各个节点进行名称检查
     *
     * @param annotations 此注解处理器索要处理的注解的集合
     * @param roundEnv 访问当前Round中的语法树节点
     * @return true-通知编译器这个Round的代码变化了，要重新构造一个新的JavaCompiler实例
     *         false-没变化，不需要构造新实例
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if(!roundEnv.processingOver()){
            for(Element element : roundEnv.getRootElements()){

            }
        }

        return false;
    }
}
