package annotation.learn;

import java.lang.annotation.*;

/**
 * Title:
 * Description:
 * Copyright: 2018 北京拓尔思信息技术股份有限公司 版权所有.保留所有权
 * Company:北京拓尔思信息技术股份有限公司(TRS)
 * Project: test
 * Author: jianghaotian
 * Create Time:2018/11/16 10:46
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface MyAnnotation {
    String s1();
    int i2();
}
