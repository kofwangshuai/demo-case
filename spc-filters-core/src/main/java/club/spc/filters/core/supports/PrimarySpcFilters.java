package club.spc.filters.core.supports;

import club.spc.filters.core.FilterTypesEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName:SpcFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 16:07
 * @Version 1.0.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimarySpcFilters {

    String[] bizType();

    FilterTypesEnum filterType();

    int orders();

    boolean should() default true;

    boolean primary() default true;
}
