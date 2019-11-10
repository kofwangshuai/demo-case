package club.spc.filters.web;

import club.spc.filters.auto.config.EnableAutoSpc;
import club.spc.filters.auto.config.filters.test1.DubboRequest1;
import club.spc.filters.auto.config.filters.test1.DubboRequest2;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.collect.Maps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName:DemoApp
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-09 22:16
 * @Version 1.0.0
 **/
@SpringBootApplication(
        scanBasePackages = "club.spc.filters.web",
        exclude = {
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                H2ConsoleAutoConfiguration.class,
                JdbcTemplateAutoConfiguration.class,
        }
)

@EnableAutoSpc(basePackages = "club.spc")
@RestController
public class DemoApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }

    @Reference
    private DubboConsumerAPI dubboConsumerAPI;

    @RequestMapping(value = "test1")
    public DubboRequest1 test(){
        return dubboConsumerAPI.consumerTest(new DubboRequest1().setFeature(Maps.newHashMap()).setProductId(1000).setSkuId(1000),
                new DubboRequest2().setFeature(false).setFeature(false).setSpudId(false));
    }
}