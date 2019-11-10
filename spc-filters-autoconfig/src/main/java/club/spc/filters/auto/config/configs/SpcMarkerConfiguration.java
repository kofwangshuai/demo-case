package club.spc.filters.auto.config.configs;

import club.spc.filters.auto.config.SpcFilterAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName:SpcMarkerConfiguration
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-27 11:29
 * @Version 1.0.0
 *
 *  用来标记并且触发一个bean对象，{@link SpcFilterAutoConfiguration}
 **/
@Configuration
public class SpcMarkerConfiguration {

    @Bean
    public Marker SpcMarkerBean() {
        return new Marker();
    }

    public class Marker {

    }

}
