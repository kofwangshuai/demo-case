package club.spc.filters.core;

import club.spc.filters.core.context.SpcContext;
import club.spc.filters.core.dao.SpcFilterLoader;
import club.spc.filters.core.exception.SpcException;
import club.spc.filters.core.supports.DynamicCounter;
import club.spc.filters.core.context.Debug;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName:SpcFilterProcessor
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:11
 * @Version 1.0.0
 *  1 zuul FilterProcessor；
 **/
@Slf4j
public class SpcFilterProcessor {

    static SpcFilterProcessor INSTANCE = new SpcFilterProcessor();

    private SpcFilterUsageNotifier usageNotifier;

    private SpcFilterProcessor() {
        usageNotifier = new BasicFilterUsageNotifier();
    }

    public static SpcFilterProcessor getInstance() {
        return INSTANCE;
    }

    public static void setProcessor(SpcFilterProcessor processor) {
        INSTANCE = processor;
    }

    public void setFilterUsageNotifier(SpcFilterUsageNotifier notifier) {
        this.usageNotifier = notifier;
    }

    /**
     *  1 预处理逻辑
     * @throws SpcException
     */
    public void preProcessing(String bizType) throws SpcException {
        try {
            runFilters(FilterTypesEnum.PRE.name(),bizType);
        } catch (SpcException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
            throw new SpcException(e, 500, "UNCAUGHT_EXCEPTION_IN_PRE_FILTER_" + e.getClass().getName());
        }
    }

    /**
     *  2 带有副作用的业务处理
     * @throws SpcException
     */
    public void sideEffect(String bizType) throws SpcException {
        try {
            runFilters(FilterTypesEnum.SIDE_EFFECT.name(),bizType);
        } catch (SpcException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
            throw new SpcException(e, 500, "UNCAUGHT_EXCEPTION_IN_PRE_FILTER_" + e.getClass().getName());
        }
    }

    /**
     * 3 调用业务处理接口处理业务
     * @throws SpcException
     */
    public void middleProcessing (String bizType) throws SpcException {
        try {
            runFilters(FilterTypesEnum.POST.name(),bizType);
        } catch (SpcException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
            throw new SpcException(e, 500, "UNCAUGHT_EXCEPTION_IN_ROUTE_FILTER_" + e.getClass().getName());
        }
    }

    /**
     *  4 处理完成后续处理
     * @throws SpcException
     */
    public void postProcessing(String bizType) throws SpcException {
        try {
            runFilters(FilterTypesEnum.POST.name(),bizType);        } catch (SpcException e) {
            throw e;
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
            throw new SpcException(e, 500, "UNCAUGHT_EXCEPTION_IN_POST_FILTER_" + e.getClass().getName());
        }
    }

    /**
     * 5 发生了异常以后处理
     */
    public void error(String bizType) {
        try {
            runFilters(FilterTypesEnum.ERROR.name(),bizType);
        } catch (Throwable e) {
            log.error(e.getMessage(),e);
            log.error(e.getMessage(), e);
        }
    }




    /**
     * runs all filters of the filterType sType/ Use this method within filters to run custom filters by type
     *
     * @param filterType the filterType.
     * @return
     * @throws Throwable throws up an arbitrary exception
     */
    public Object runFilters(String filterType,String bizType) throws Throwable {

        if (SpcContext.getCurrentContext().debugRouting()) {
            Debug.addRoutingDebug("Invoking {" + filterType + "} type filters");
        }
        boolean bResult = false;
        // 加载 过滤器 todo 重新过滤器加载机制 ；
        List<SpcFilter> list = SpcFilterLoader.getInstance().getFiltersByType(filterType);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                SpcFilter spcFilter = list.get(i);
                if (!spcFilter.bizType().equals(bizType)){
                    continue;
                }
                Object result = processSpcFilter(spcFilter);
                if (result != null && result instanceof Boolean) {
                    bResult |= ((Boolean) result);
                }
            }
        }
        return bResult;
    }

    public Object processSpcFilter(SpcFilter filter) throws SpcException {

        SpcContext ctx = SpcContext.getCurrentContext();
        boolean bDebug = ctx.debugRouting();
        // 基数 前置
        final String metricPrefix = "spc.filter-";
        long execTime = 0;
        String filterName = "";
        try {
            long ltime = System.currentTimeMillis();
            filterName = filter.getClass().getSimpleName();

            SpcContext copy = null;
            Object o = null;
            Throwable t = null;

            if (bDebug) {
                Debug.addRoutingDebug("Filter " + filter.filterType() + " " + filter.filterOrder() + " " + filterName);
                copy = ctx.copy();
            }

            SpcFilterResult result = filter.runFilter();
            ExecutionStatusEnum s = result.getStatus();
            execTime = System.currentTimeMillis() - ltime;

            switch (s) {
                case FAILED:
                    t = result.getException();
                    ctx.addFilterExecutionSummary(filterName, ExecutionStatusEnum.FAILED.name(), execTime);
                    break;
                case SUCCESS:
                    o = result.getResult();
                    ctx.addFilterExecutionSummary(filterName, ExecutionStatusEnum.SUCCESS.name(), execTime);
                    if (bDebug) {
                        Debug.addRoutingDebug("Filter {" + filterName + " TYPE:" + filter.filterType() + " ORDER:" + filter.filterOrder() + "} Execution time = " + execTime + "ms");
                        Debug.compareContextState(filterName, copy);
                    }
                    break;
                default:
                    break;
            }

            if (t != null) throw t;

            usageNotifier.notify(filter, s);
            return o;

        } catch (Throwable e) {
            if (bDebug) {
                Debug.addRoutingDebug("Running Filter failed " + filterName + " type:" + filter.filterType() + " order:" + filter.filterOrder() + " " + e.getMessage());
            }
            usageNotifier.notify(filter, ExecutionStatusEnum.FAILED);
            if (e instanceof SpcException) {
                throw (SpcException) e;
            } else {
                SpcException ex = new SpcException(e, "Filter threw Exception", 500, filter.filterType() + ":" + filterName);
                ctx.addFilterExecutionSummary(filterName, ExecutionStatusEnum.FAILED.name(), execTime);
                throw ex;
            }
        }
    }

    /**
     * Publishes a counter metric for each filter on each use.
     */
    // todo

    public static class BasicFilterUsageNotifier implements SpcFilterUsageNotifier {
        private static final String METRIC_PREFIX = "zuul.filter-";

        @Override
        public void notify(SpcFilter filter, ExecutionStatusEnum status) {
            DynamicCounter.increment(METRIC_PREFIX + filter.getClass().getSimpleName(), "status", status.name(), "filtertype", filter.filterType());
        }
    }

}
