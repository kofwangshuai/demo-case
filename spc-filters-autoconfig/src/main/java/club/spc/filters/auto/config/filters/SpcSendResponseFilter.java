package club.spc.filters.auto.config.filters;


import club.spc.filters.core.FilterTypesEnum;
import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.supports.PrimarySpcFilters;

/**
 * @ClassName:SpcSendResponseFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-27 23:34
 * @Version 1.0.0
 **/
@PrimarySpcFilters(
        orders = 3000,
        filterType = FilterTypesEnum.POST,
        bizType = "send_response"
)
public class SpcSendResponseFilter extends SpcFilter {

    @Override
    public Object run() {
        System.out.println(" 执行 send_reponse");
        return null;
    }

}
