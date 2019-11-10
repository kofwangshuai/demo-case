package club.spc.filters.core.supports;

/**
 * @ClassName:CounterFactory
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 17:45
 * @Version 1.0.0
 *
 * 2 链路执行异常 技术统计设计 ： 考虑基于cat的实现方式
 **/
public abstract class CounterFactory {
    private static CounterFactory INSTANCE;

    public static final void initialize(CounterFactory f) {
        INSTANCE = f;
    }

    public static final CounterFactory instance() {
        if(INSTANCE == null) throw new IllegalStateException(String.format("%s not initialized", CounterFactory.class.getSimpleName()));
        return INSTANCE;
    }

    public abstract void increment(String name);
}
