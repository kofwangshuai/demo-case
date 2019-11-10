package club.spc.filters.consumers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * @ClassName:DemoApp
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-09 22:16
 * @Version 1.0.0
 **/
@SpringBootApplication(
        scanBasePackages = "club.spc.filters.consumers",
        exclude = {
                org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
                DataSourceTransactionManagerAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                H2ConsoleAutoConfiguration.class,
                JdbcTemplateAutoConfiguration.class,
        }
)
public class DemoApp {
    public static void main(String[] args) {
        SpringApplication.run(DemoApp.class, args);
    }
}