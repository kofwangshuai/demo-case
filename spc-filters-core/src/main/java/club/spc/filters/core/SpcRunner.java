package club.spc.filters.core;


import club.spc.filters.core.context.SpcContext;
import club.spc.filters.core.exception.SpcException;
import com.alibaba.dubbo.rpc.Result;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName:SpcRunner
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:54
 * @Version 1.0.0
 *
 * 1  核心入口类 ：执行器 ，规定了过滤器的执行顺序。
 *
 **/
@Slf4j
public  class SpcRunner{

    //是否缓存body信息 ,用于反复读取body数据。
    //
    // 1   http接口 ：HttpServletRequst对象包装为 HttpServletRequestWrapper，HttpServletResponset对象包装为 HttpServletResponseWrapper
    // 2   dubbo接口  RpcContext的request 和 response.不需要处理。
    private boolean bufferRequests;

    public SpcRunner() {
        this.bufferRequests = true;
    }

    public SpcRunner(boolean bufferRequests) {
        this.bufferRequests = bufferRequests;
    }

    /**
     *  初始化入参数和出参数
     *  http 请求
     *  {@link javax.servlet.http.HttpServletRequest }
     *  {@link javax.servlet.http.HttpServletResponse}
     *  dubbo请求
     *  {@link com.alibaba.dubbo.rpc.RpcContext #Request and Response}
     *
     * @param request
     * @param response
     */
    public <T, V> void init(T request, V response) {
        SpcContext ctx = SpcContext.getCurrentContext();
        if (bufferRequests) {
            ctx.setRequest(request);
        } else {
            ctx.setRequest(request);
        }
        ctx.setResponse(response);
    }

    /**
     *
     * @param request {@link com.alibaba.dubbo.rpc.RpcContext #arguments }
     * @param response {@link com.alibaba.dubbo.rpc.RpcContext #Result}
     * @param <T>
     * @param <V>
     */
    public <T, V> void initDubbo(Object[] request, Result response) {
        SpcContext ctx = SpcContext.getCurrentContext();
        if (bufferRequests) {
            ctx.setRequest(request);
        } else {
            ctx.setRequest(request);
        }
        ctx.setResponse(response);
    }
    public <T, V> void initDubboType(Class<?>[] request, Result response) {
        SpcContext ctx = SpcContext.getCurrentContext();
        if (bufferRequests) {
            ctx.setRequestType(request);
        } else {
            ctx.setRequestType(request);
        }
        ctx.setResponseType();
    }

    public void preProcessing(String bizType) throws SpcException {
        SpcFilterProcessor.getInstance().preProcessing(bizType);
    }
    public void sideEffect(String bizType) throws SpcException {
        SpcFilterProcessor.getInstance().preProcessing(bizType);
    }

    public void postProcessing(String bizType) throws SpcException {
        SpcFilterProcessor.getInstance().postProcessing(bizType);
    }

    public void middleProcessing(String bizType) throws SpcException {
        SpcFilterProcessor.getInstance().middleProcessing(bizType);
    }

    public void error(String bizType) {
        SpcFilterProcessor.getInstance().error(bizType);
    }

    public static void main(String[] args) {

    }

    public void initMethodName(String methodName) {
        SpcContext.getCurrentContext().set("dubboMethodName" ,Thread.currentThread().getId()+"::"+methodName);
        log.info("dubboMethodName is {}",(Thread.currentThread().getId()+"::"+methodName));
    }
}
