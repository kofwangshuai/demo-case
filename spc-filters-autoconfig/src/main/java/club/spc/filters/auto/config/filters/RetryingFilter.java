package club.spc.filters.auto.config.filters;

import club.spc.filters.core.FilterTypesEnum;
import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.supports.PrimarySpcFilters;

/**
 * @ClassName:RetryingFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 02:05
 * @Version 1.0.0
 **/
@PrimarySpcFilters(
        orders = 6000,
        filterType = FilterTypesEnum.POST,
        bizType = "retrying_request"
)
public class RetryingFilter extends SpcFilter
{

    @Override
    public Object run() {
        System.out.println(" 执行 retrying_request");
        return null;
    }

}
