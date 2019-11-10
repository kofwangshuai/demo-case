package club.spc.filters.core;


/**
 * @ClassName:SpcFilterUsageNotifier
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:13
 * @Version 1.0.0
 *
 * 2  过滤器执行结果 异步通知
 **/
public interface SpcFilterUsageNotifier {

    public void notify(SpcFilter filter, ExecutionStatusEnum status);

}
