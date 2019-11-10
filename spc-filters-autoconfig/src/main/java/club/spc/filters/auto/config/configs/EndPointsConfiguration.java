package club.spc.filters.auto.config.configs;

import club.spc.filters.auto.config.endpoints.SpcEndpoints;
import club.spc.filters.core.dao.SpcFilterRegistry;
import org.springframework.boot.actuate.autoconfigure.endpoint.condition.ConditionalOnAvailableEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName:EndPointsConfiguration
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 00:20
 * @Version 1.0.0
 **/
@Configuration
@ConditionalOnClass(Health.class)
public class EndPointsConfiguration {

    @ConditionalOnAvailableEndpoint
    @Bean
    public SpcEndpoints spcEndpoints() {
        SpcFilterRegistry spcFilterRegistry = SpcFilterRegistry.instance();
        return new SpcEndpoints(spcFilterRegistry);
    }
}
