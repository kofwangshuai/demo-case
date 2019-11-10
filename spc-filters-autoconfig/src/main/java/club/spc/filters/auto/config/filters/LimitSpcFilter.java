package club.spc.filters.auto.config.filters;

import club.spc.filters.core.FilterTypesEnum;
import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.supports.PrimarySpcFilters;

/**
 * @ClassName:LimitSpcFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 02:04
 * @Version 1.0.0
 **/
@PrimarySpcFilters(
        orders = 4000,
        filterType = FilterTypesEnum.PRE,
        bizType = "limit_request"
)
public class LimitSpcFilter extends SpcFilter {

    @Override
    public Object run() {
        System.out.println("retrying_request .....");
        return null;
    }

}
