package club.spc.filters.auto.config;

import club.spc.filters.auto.config.configs.SpcMarkerConfiguration;
import club.spc.filters.auto.config.filters.ErrorFilter;
import club.spc.filters.auto.config.filters.SpcSendResponseFilter;
import club.spc.filters.auto.config.filters.test1.RunBizApi2;
import club.spc.filters.auto.config.filters.test1.RunBizApi2Impl;
import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.dao.SpcFilterLoader;
import club.spc.filters.core.dao.SpcFilterRegistry;
import club.spc.filters.core.runner.DubboSpcRunner;
import club.spc.filters.core.supports.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName:SpcFilterAutoConfiguration
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-27 11:13
 * @Version 1.0.0
 **/
@Slf4j
@Configuration
@ConditionalOnBean(SpcMarkerConfiguration.Marker.class)
public class SpcFilterAutoConfiguration {

    @Configuration
    protected static class ZuulFilterConfiguration {

        @Autowired
        private Map<String, SpcFilter> filters;

        @Bean(initMethod = "initMethodSpc")
        @ConditionalOnMissingBean(SpcSendResponseFilter.class)
        @Primary
        public SpcFilter spcSendResponseFilter() {
            SpcSendResponseFilter spcSendResponseFilter = new SpcSendResponseFilter();
            return spcSendResponseFilter;
        }
        @Bean(initMethod = "initMethodSpc")
        @ConditionalOnMissingBean(ErrorFilter.class)
        @Primary
        public SpcFilter errorFilter() {
            return new ErrorFilter();
        }

        @Bean
        public SpcFilterInitializer spcFilterInitializer(
                CounterFactory counterFactory, TracerFactory tracerFactory) {

            SpcFilterLoader spcFilterLoader = SpcFilterLoader.getInstance();
            SpcFilterRegistry spcFilterRegistry = SpcFilterRegistry.instance();
            log.info("\n =========================== \n" +
                            "spcFilter  Initializer data info : \n[{}] " +
                            "\n =========================== \n"
                    , JSONObject.toJSONString(filters));
            checkIllegalFilter(this.filters);
            checkIllegalFilterOrders(this.filters);
            checkIllegalFilterDescrition(this.filters);
            return new SpcFilterInitializer(this.filters, counterFactory, tracerFactory, spcFilterLoader, spcFilterRegistry);
        }

        private void checkIllegalFilterDescrition(Map<String, SpcFilter> filters) {

            if (CollectionUtils.isEmpty(filters)){
                throw new NullPointerException(" spc filters init error");
            }
            List<String> illegalFilterList = filters.keySet().stream().filter(
                    name -> {
                        SpcFilter spcFilter = filters.get(name);
                        String filterDescrition = spcFilter.getFilterDescrition();

                        if (spcFilter.getFilterDescrition()==null||
                                SpcFilter.DEFAULT_DESCRITION.equals(spcFilter.getFilterDescrition())) {
                            return false;
                        } else {
                            return true;
                        }
                    }
            ).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(illegalFilterList)){
                throw new NullPointerException("spc filters descrition is not overrided" +
                        " ,to tell consumer something how to do ");
            }
        }

        private void checkIllegalFilterOrders(Map<String, SpcFilter> filters) {
            if (CollectionUtils.isEmpty(filters)){
                throw new NullPointerException(String.format(" spc filters init error: filters is {}",0));
            }
            Set<Integer> orders = filters.keySet().stream()
                    .map(spcFilter->filters.get(spcFilter).filterOrder())
            .collect(Collectors.toSet());
            if (CollectionUtils.isEmpty(orders)||orders.size()!=filters.size()){
                throw new NullPointerException(String.format(" spc filters orders error :orders size is { %s }, filters is { %s }"
                        ,orders.size() ,filters.size()));
                // TODO: 2019-11-10  返回 过滤器顺序重叠的过滤器名称 列表;
                // TODO: 2019-11-10  目前不会支持；动态修改过滤器的顺序的功能 ；如果在已经存在业务规则过滤器的情况下，
                //  需要开放者 重新审视，增加新的过滤器以后，是否需要重新调整相关业务的过滤器的执行顺序；
                // TODO: 2019-11-10  提供基于bizType 即为 针对特定业务生效的过滤器检索功能。
                // TODO: 2019-11-10  需要动态配置全局过滤器的执行顺序，防止过滤器数量庞大时候，引起的过滤器执行顺序错乱的问题

            }
        }

        private void checkIllegalFilter(Map<String, SpcFilter> filters) {
            if (CollectionUtils.isEmpty(filters)){
                throw new NullPointerException(" spc filters init error");
            }
            List<String> illegalFilterList = filters.keySet().stream().filter(
                    name -> {
                        SpcFilter spcFilter = filters.get(name);
                        Boolean IllegalFilter = SpcFilter.ERROR_INIT_SPC_FILTER_BIZ.equals(spcFilter.bizType())
                                && SpcFilter.ERROR_INIT_SPC_FILTER_TYPE.equals(spcFilter.bizType())
                                && spcFilter.bizShouldFilter() == null
                                && spcFilter.filterOrder() == Integer.MIN_VALUE;
                        if (IllegalFilter) {
                            return true;
                        } else {
                            return false;
                        }
                    }
            ).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(illegalFilterList)){
                throw new NullPointerException(" spc filters init error");
            }
        }


        @Bean // TODO: 2019-10-27   预定义一个壳子对象
        public CounterFactory counterFactory() {
            return new EmptyCounterFactory();
        }

        @Bean // TODO: 2019-10-27  预定义一个壳子对象
        public TracerFactory tracerFactory() {
            return new EmptyTracerFactory();
        }

        @Bean()
        public RunBizApi2 runBizApi2(){
            return new RunBizApi2Impl();
        }

    }

//    @Configuration
//    protected static class SpcRunnerConfiguration implements InitializingBean {
//
//        @Autowired(required = false)
//        private DubboSpcRunner dubboSpcRunner;
//
//        @Autowired(required = false)
//        private IBizTypeStrategy iBizTypeStrategy;
//
//        @Override
//        public void afterPropertiesSet() throws Exception {
//            if (iBizTypeStrategy == null){
//                iBizTypeStrategy=new IBizTypeStrategy.DefaultBizTypeStrategy();
//            }
//            if (dubboSpcRunner == null){
//                throw  new NullPointerException("DubboSpcRunner bean is missing");
//            }
//            dubboSpcRunner.init(iBizTypeStrategy);
//        }
//    }
}
