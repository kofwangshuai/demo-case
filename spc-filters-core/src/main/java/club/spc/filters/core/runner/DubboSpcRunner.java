package club.spc.filters.core.runner;


import club.spc.filters.core.SpcRunner;
import club.spc.filters.core.constants.SpcContants;
import club.spc.filters.core.context.SpcContext;
import club.spc.filters.core.exception.SpcException;
import club.spc.filters.core.supports.BizType;
import club.spc.filters.core.supports.IBizTypeStrategy;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * @ClassName:DubboSpcRunner
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 03:15
 * @Version 1.0.0
 * <p>
 * 1  执行器 ：适配dubbo接口的业务场景
 * 2  服务端校验逻辑
 **/
@Slf4j
@Component
public class DubboSpcRunner implements Filter {

    private SpcRunner spcRunner;

    private IBizTypeStrategy strategy;

    public DubboSpcRunner(){
        init(null);
    }

    public void init(IBizTypeStrategy strategy) {
        this.strategy = strategy;
        if (this.strategy==null){
            this.strategy=new IBizTypeStrategy() {
                @Override
                public String[] getBizInfo(BizType bizType) {
                    return bizType.value();
                }
            };
        }
    }

    ;

    /**
     * todo 如何解RPC的超时的问题 ？？？？：增加逻辑判断，使得 RPC调用超时加重 。
     *
     * @param invoker
     * @param invocation
     * @return
     * @throws RpcException
     */
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 1 暂时不支持接口级别的注解的指定 ;
//        BizType annotationInterface = invoker.getInterface().getAnnotation(BizType.class);

        BizType annotationMethod = null;
        String methodName = invocation.getMethodName();
        Class<?>[] parameterTypes = invocation.getParameterTypes();
        try {
            Method method = invoker.getInterface().getMethod(methodName, parameterTypes);
            annotationMethod = method.getAnnotation(BizType.class);
        } catch (NoSuchMethodException e) {
            log.error(e.getLocalizedMessage(), e);
        }
        if (null == annotationMethod) {
            return invoker.invoke(invocation);
        }
        return spcInvoke(invoker, invocation, annotationMethod);
    }

    private Result spcInvoke(Invoker<?> invoker, Invocation invocation, BizType annotation) {
        spcRunner = new SpcRunner();
        SpcContext currentContext = SpcContext.getCurrentContext();
        currentContext.set(SpcContants.SPC_FILTRER_DUBBO_PROVIDER_ENGINE_RAN);
        Result result = null;
        try {
            // TODO: 2019-11-10  开放 bizType注解处理策略的扩展点 ：
            String[] bizInfo = strategy.getBizInfo(annotation);
            init(invoker, invocation, annotation);

            try { // 预处理 ；
                preProcessing(bizInfo[0]);
            } catch (SpcException e) {
                error(e);
                postProcessing(bizInfo[0]);
            }
            try { // 带副作用预处理 ；
                sideEffect(bizInfo[0]);
            } catch (SpcException e) {
                error(e);
                postProcessing(bizInfo[0]);
            }
            try { // 核心业务流程 :构建Dubbo的上下文参数
                Object[] arguments = invocation.getArguments();
                log.info(" 原始参数 ：{}" , JSONObject.toJSONString(arguments));
                Object[] dubboProviderRequest = SpcContext.getCurrentContext().getDubboProviderRequest();
                String methodName = invocation.getMethodName();
                log.info("methodName is {}" ,methodName);
                Method[] method = invoker.getInterface().getMethods()
                        ;
                log.info("methods is {}" ,JSONObject.toJSONString(method));

                Invocation invocationSideEffect=new RpcInvocation(method[0],dubboProviderRequest,invocation.getAttachments());
                log.info(" 副作用以后参数 ：{}" ,JSONObject.toJSONString(arguments));
                result = invoker.invoke(invocationSideEffect);
                // TODO: 2019-11-10  优化的点 ：开启异步支持模式；降低核心功能接口调用超时的发生
                middleProcessing(bizInfo[0]);
            } catch (SpcException e) {
                error(e);
                postProcessing(bizInfo[0]);
            }
            try { // 后置处理逻辑 ；
                // TODO: 2019-11-10  优化的点 ：开启异步支持模式；降低核心功能接口调用超时的发生
                postProcessing(bizInfo[0]);
            } catch (SpcException e) {
                error(e);
            }
        } catch (Throwable e) {
            log.error(e.getLocalizedMessage() ,e);
            error(new SpcException(e, 500, "UNCAUGHT_EXCEPTION_FROM_FILTER_" + e.getClass().getName()));
        } finally {
            SpcContext.getCurrentContext().unset();
        }
        return result;
    }


    public void init(Invoker<?> invoker, Invocation invocation, BizType annotation) {

        /****************** RpcContext test **********************/
        Object[] arguments = invocation.getArguments();
        Class<?>[] parameterTypes = invocation.getParameterTypes();
        Object response = RpcContext.getContext().getResponse();
        /****************** RpcContext test **********************/

        if (parameterTypes.length != arguments.length) {
            throw new IllegalArgumentException();
        }
        // 复制 和拷贝 dubbo的RPC上下文到 Spc上下文中。
        spcRunner.initDubbo(arguments, (Result) response);
        spcRunner.initDubboType(parameterTypes ,(Result)response);
        spcRunner.initMethodName(invocation.getMethodName());
        SpcContext.getCurrentContext().setDubboProviderEngineRan();
    }

    void preProcessing(String bizType) throws SpcException {
        spcRunner.preProcessing(bizType);
    }

    void sideEffect(String bizType) throws SpcException {
        spcRunner.sideEffect(bizType);
    }

    void middleProcessing(String bizType) throws SpcException {
        spcRunner.middleProcessing(bizType);
    }

    void postProcessing(String bizType) throws SpcException {
        spcRunner.postProcessing(bizType);
    }

    void error(SpcException e) {
        SpcContext.getCurrentContext().setThrowable(e);
        spcRunner.error("error_excutor");
    }

}
