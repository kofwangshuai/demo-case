package club.spc.filters.core;

import club.spc.filters.core.supports.PrimarySpcFilters;
import club.spc.filters.core.supports.TracerFactory;
import club.spc.filters.core.utils.Tracer;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName:SpcFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:38
 * @Version 1.0.0
 * <p>
 * 1  todo ：  开启 动态debug模式的功能 过滤器
 * 2  todo ： 设置全局默认的过滤器逻辑 ：
 **/
@Accessors(chain = true)
@Slf4j
public abstract class SpcFilter extends ISpcFilter implements Comparable<SpcFilter> {

    public static final String ERROR_INIT_SPC_FILTER_BIZ = "init_spc_error_biz";
    public static final String ERROR_INIT_SPC_FILTER_TYPE = "init_spc_error_type";
    public static final Boolean ERROR_INIT_SPC_FILTER_SHOULD = false;
    public static final Integer ERROR_INIT_SPC_FILTER_ORDERS = Integer.MIN_VALUE;
    public static final String DEFAULT_DESCRITION="请填写组件注释，说明组件的功能，参数条件以及返回结果和副作用";
    protected String AUTO_REGESTRY="自动注册";
    protected String UNAUTO_REGESTRY="手动注册";

    private Boolean apolloFlag = true; // 1 线程安全设置
    private String bizType;
    private String filterType;
    private Boolean shouldFilter;
    private Integer orders;
    private PrimarySpcFilters annotation;
    protected String regestry=AUTO_REGESTRY;
    private String filterDescrition=DEFAULT_DESCRITION;

    public void initMethodSpc(){
        annotation=this.getClass().getDeclaredAnnotation(PrimarySpcFilters.class);
        log.info("init Spc filters  ..... {} ..... ",UNAUTO_REGESTRY);
        this.regestry=UNAUTO_REGESTRY;
        this.setBizType(annotation.bizType()[0]);
        this.setShouldFilter(annotation.should());
        this.setFilterType(annotation.filterType().name());
        this.setOrders(annotation.orders());
    }

    public String bizType() {
        if (null != bizType){
            return bizType;
        }
        return ERROR_INIT_SPC_FILTER_BIZ;
    }

    public String filterType() {
        if (null != filterType){
            return filterType;
        }
        return ERROR_INIT_SPC_FILTER_TYPE;
    }

    public Boolean bizShouldFilter() {
        if (null != shouldFilter){
            return shouldFilter;
        }
        return ERROR_INIT_SPC_FILTER_SHOULD;
    }

    public int filterOrder() {
        if (null!= orders){
            return orders;
        }
        return ERROR_INIT_SPC_FILTER_ORDERS;
    }

    abstract public Object run();

    // 1 apollo 配置全局开关 优先级高于 当前过滤器的开关处理逻辑
    public final boolean shouldFilter() {
        return apolloFlag && bizShouldFilter();
    }

    // 1 apollo 动态上下线过滤器的开关
    public void apolloFlag(Boolean apolloFlag) {
        this.apolloFlag = apolloFlag;
    }

    // 2 过滤器执行处理
    public SpcFilterResult runFilter() {
        SpcFilterResult spcResult = new SpcFilterResult();
        if (true) {
            if (shouldFilter()) {
                Tracer t = TracerFactory.instance().startMicroTracer("SPC_FILTERS::" + this.getClass().getSimpleName());
                try {
                    Object res = run();
                    spcResult = new SpcFilterResult(res, ExecutionStatusEnum.SUCCESS);
                } catch (Throwable e) {
                    log.error(e.getLocalizedMessage() ,e);
                    t.setName("ZUUL::" + this.getClass().getSimpleName() + " failed");
                    spcResult = new SpcFilterResult(ExecutionStatusEnum.FAILED);
                    spcResult.setException(e);
                } finally {
                    t.stopAndLog(); // todo 异步上报异常结果 ，接入spc-log服务（日志管理平台）
                }
            } else {
                spcResult = new SpcFilterResult(ExecutionStatusEnum.SKIPPED);
            }
        }
        return spcResult;
    }

    // 3 过滤器排序
    public int compareTo(SpcFilter filter) {
        return Integer.compare(this.filterOrder(), filter.filterOrder());
    }


    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getFilterType() {
        return filterType;
    }

    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }

    public Boolean getShouldFilter() {
        return shouldFilter;
    }

    public void setShouldFilter(Boolean shouldFilter) {
        this.shouldFilter = shouldFilter;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public String getFilterDescrition() {
        return filterDescrition;
    }

    public void setFilterDescrition(String filterDescrition) {
        this.filterDescrition = filterDescrition;
    }
}

