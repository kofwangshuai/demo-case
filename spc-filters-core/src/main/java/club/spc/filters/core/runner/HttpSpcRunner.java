package club.spc.filters.core.runner;

import club.spc.filters.core.SpcRunner;
import club.spc.filters.core.context.SpcContext;
import club.spc.filters.core.exception.SpcException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName:HttpSpcRunner
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 13:54
 * @Version 1.0.0
 *
 *  1 执行器: 适配Spring MVC接口的业务场景
 **/
public class HttpSpcRunner implements Filter {

    private SpcRunner spcRunner;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String bufferReqsStr = filterConfig.getInitParameter("buffer-requests");
        boolean bufferReqs = bufferReqsStr != null && bufferReqsStr.equals("true") ? true : false;
        spcRunner = new SpcRunner(bufferReqs);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            init((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);

            if (!SpcContext.getCurrentContext().getBoolean("bizType")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
           String bizType=(String)SpcContext.getCurrentContext().get("bizTypeValue");

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

            filterChain.doFilter(servletRequest, servletResponse);

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

    void init(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        spcRunner.init(servletRequest, servletResponse);
        String bizType = servletRequest.getParameter("bizType");
        if (null==bizType){
            SpcContext.getCurrentContext().set("bizType");
            SpcContext.getCurrentContext().set("bizTypeValue",bizType); //bizTypeValue
        }
    }

    void error(SpcException e) {
        SpcContext.getCurrentContext().setThrowable(e);
        spcRunner.error(null);
    }

    @Override
    public void destroy() {
    }
}