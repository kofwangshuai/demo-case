package club.spc.filters.core;


import club.spc.filters.core.runner.*;

/**
 * @ClassName:ISpcFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:59
 * @Version 1.0.0
 *
 *  1   执行器
 *  1-1 适配手动执行            {@link DefaultSpcRunner}；
 *  1-2 适配dubbo接口          {@link DubboSpcRunner}
 *  1-3 适配http接口执行器      {@link HttpSpcRunner}
 *  1-4 适配restTemplate      {@link RestTemplateSpcRunner}
 *  1-5 适配httpClinet        {@link HttpClientSpcRunner}
 *
 *  2  过滤器五大类
 *  pre-processing,     ：前置处理
 *  pre-side-effect     ：前置副作用处理
 *  middle-processing   ：核心业务处理方法执行和调用
 *  post-processing     ：后置处理
 *  error-processing    ：异常处理
 *
 *  3  执行器定义了五大类过滤器的执行顺序。
 *  4  每一个大类的过滤器内部通过定义过滤器的执行顺序执行。
 *
 **/
public abstract class ISpcFilter {

    // 1  业务分类
    public abstract String bizType();

    // 2  处理流程大分类
    public abstract String filterType();

    // 3  在当前分类中的执行顺序
    public abstract int filterOrder();

    // 4  过滤器是否禁用
    public abstract boolean shouldFilter();

    // 5  执行的业务逻辑
    public abstract Object run();


}
