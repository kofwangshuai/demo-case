package club.spc.filters.auto.config;

import club.spc.filters.auto.config.configs.SpcMarkerConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName:EnableSpc
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-27 11:36
 * @Version 1.0.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({SpcMarkerConfiguration.class
        ,SpcFiltersRegistrar.class
})
public @interface EnableAutoSpc {

    boolean enableSpc() default true;

    String[] value() default {};

    Class[] spcFilters() default {};

    String[] basePackages() default {};

    Class<?>[] basePackageClasses() default {};
}
