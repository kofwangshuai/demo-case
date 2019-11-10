package club.spc.filters.core.supports;


import club.spc.filters.core.utils.Tracer;

/**
 * @ClassName:TracerFactory
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 17:39
 * @Version 1.0.0
 **/
public abstract class TracerFactory {
    private static TracerFactory INSTANCE;

    public static final void initialize(TracerFactory f) {
        INSTANCE = f;
    }

    public static final TracerFactory instance() {
        if(INSTANCE == null) throw new IllegalStateException(String.format("%s not initialized", TracerFactory.class.getSimpleName()));
        return INSTANCE;
    }

    public abstract Tracer startMicroTracer(String name);
}
