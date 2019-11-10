package club.spc.filters.auto.config;

import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.dao.SpcFilterLoader;
import club.spc.filters.core.dao.SpcFilterRegistry;
import club.spc.filters.core.supports.CounterFactory;
import club.spc.filters.core.supports.TracerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * @ClassName:SpcFilterInitializer
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-27 11:14
 * @Version 1.0.0
 *
 *  1 过滤器初始化入口
 *  2
 **/
@Slf4j
public class SpcFilterInitializer {

    private final Map<String, SpcFilter> filters;
    private final CounterFactory counterFactory;
    private final TracerFactory tracerFactory;
    private final SpcFilterLoader spcFilterLoader;
    private final SpcFilterRegistry spcFilterRegistry;

    public SpcFilterInitializer(
            Map<String, SpcFilter> filters,
             CounterFactory counterFactory,
             TracerFactory tracerFactory,
            SpcFilterLoader spcFilterLoader,
             SpcFilterRegistry spcFilterRegistry
    ){
        this.filters=filters;
        this.counterFactory=counterFactory;
        this.spcFilterLoader = spcFilterLoader;
        this.spcFilterRegistry = spcFilterRegistry;
        this.tracerFactory=tracerFactory;
    }

    @PostConstruct
    public void initContext(){
        TracerFactory.initialize(tracerFactory);
        CounterFactory.initialize(counterFactory);
        for (Map.Entry<String, SpcFilter> entry : this.filters.entrySet()) {
            spcFilterRegistry.put(entry.getKey(), entry.getValue());
        }
        try {
            spcFilterLoader.putFilterFirst();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage() ,e);
        }
    }

    @PreDestroy
    private void destoryedContext(){
        log.info("Stopping filter initializer");
        for (Map.Entry<String,SpcFilter> entry :filters.entrySet()){
            spcFilterRegistry.remove(entry.getKey());
        }
        clearLoaderCache();
        // 链路追踪处理
        TracerFactory.initialize(null);
        // 异常上报处理
        CounterFactory.initialize(null);
    }

    // TODO: 2019-10-27  去掉动态加载的处理逻辑
    @Deprecated
    private void clearLoaderCache() {
        Field field = ReflectionUtils.findField(SpcFilterLoader.class, "hashFiltersByType");
        ReflectionUtils.makeAccessible(field);
        @SuppressWarnings("rawtypes")
        Map cache = (Map) ReflectionUtils.getField(field, spcFilterLoader);
        cache.clear();
    }

}
