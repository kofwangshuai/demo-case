package club.spc.filters.auto.config.filters;


import club.spc.filters.core.FilterTypesEnum;
import club.spc.filters.core.context.SpcContext;
import club.spc.filters.core.exception.SpcException;
import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.supports.PrimarySpcFilters;

/**
 * @ClassName:SendErrorFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-27 23:23
 * @Version 1.0.0
 **/
@PrimarySpcFilters(
        orders = 5000,
        filterType = FilterTypesEnum.ERROR,
        bizType = "error_excutor"
)
public class ErrorFilter extends SpcFilter {

    @Override
    public Object run() {
        Throwable throwable = SpcContext.getCurrentContext().getThrowable();
        if (throwable instanceof SpcException) {
            // TODO: 2019-10-27
        } else {
            // TODO: 2019-10-27
        }
        System.out.println("error_excutor .....");
        return null;
    }

}
