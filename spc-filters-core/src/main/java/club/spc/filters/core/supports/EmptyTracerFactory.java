package club.spc.filters.core.supports;


import club.spc.filters.core.utils.Tracer;

/**
 * @ClassName:EmptyTracerFactory
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 17:42
 * @Version 1.0.0
 * 1 todo 过滤器链路追踪记录功能 设计 ：
 **/
public class EmptyTracerFactory extends TracerFactory
{
    private final EmptyTracer emptyTracer = new EmptyTracer();

    @Override
    public Tracer startMicroTracer(String name) {
        return emptyTracer;
    }

    private static final class EmptyTracer implements Tracer {
        @Override
        public void setName(String name) {
        }

        @Override
        public void stopAndLog() {
        }
    }
}
