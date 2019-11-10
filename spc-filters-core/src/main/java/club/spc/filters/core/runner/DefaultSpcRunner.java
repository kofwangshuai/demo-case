package club.spc.filters.core.runner;

import club.spc.filters.core.SpcRunner;
import club.spc.filters.core.context.SpcContext;
import club.spc.filters.core.exception.SpcException;

import java.util.Map;

/**
 * @ClassName:DefaultSpcRunner
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 03:14
 * @Version 1.0.0
 *
 *  1  手动调用一次执行器方法
 *  3  服务端校验逻辑
 **/
public class DefaultSpcRunner  {

    private SpcRunner spcRunner;

    public  DefaultSpcRunner(Map<String ,String> filterConfig) {
        if (filterConfig==null||filterConfig.size()<=0){
            spcRunner = new SpcRunner();
            return;
        }
        String bufferReqsStr = filterConfig.get("buffer-requests");
        boolean bufferReqs = bufferReqsStr != null && bufferReqsStr.equals("true") ? true : false;
        spcRunner = new SpcRunner(bufferReqs);
    }

    public void execute(String bizType)  {
        try {
            try {
                preProcessing(bizType);
            } catch (SpcException e) {
                error(e);
                postProcessing(bizType);
                return;
            }

            try {
                sideEffect(bizType);
            } catch (SpcException e) {
                error(e);
                postProcessing(bizType);
                return;
            }


            try {
                middleProcessing(bizType);
            } catch (SpcException e) {
                error(e);
                postProcessing(bizType);
                return;
            }


            try {
                postProcessing(bizType);
            } catch (SpcException e) {
                error(e);
                return;
            }
        } catch (Throwable e) {
            error(new SpcException(e, 500, "UNCAUGHT_EXCEPTION_FROM_FILTER_" + e.getClass().getName()));
        } finally {
            SpcContext.getCurrentContext().unset();
        }
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

    public  <T,V> SpcRunner get(T params, V result) {
        spcRunner.init(params, result);
        return spcRunner;
    }

    void error(SpcException e) {
        SpcContext.getCurrentContext().setThrowable(e);
        spcRunner.error(null);
    }


}
