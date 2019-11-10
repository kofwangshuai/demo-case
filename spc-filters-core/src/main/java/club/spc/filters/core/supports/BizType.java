package club.spc.filters.core.supports;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName:BizType
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 14:05
 * @Version 1.0.0
 **/

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BizType {

    String default_value="global";

    // 1 业务代号
    String[] value() default BizType.default_value;

    // 2 核心业务参数,按照顺序
    Class[] clazz() default void.class;
}
