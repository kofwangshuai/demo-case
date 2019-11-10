package club.spc.filters.auto.config.filters;

import club.spc.filters.auto.config.filters.test1.DubboRequest1;
import club.spc.filters.auto.config.filters.test1.RunBizApi2;
import club.spc.filters.auto.config.filters.test1.DubboRequest2;
import club.spc.filters.core.FilterTypesEnum;
import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.context.SpcContext;
import club.spc.filters.core.supports.PrimarySpcFilters;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName:EmptySpcFilter
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 02:06
 * @Version 1.0.0
 **/
@PrimarySpcFilters(
        orders = 100,
        filterType = FilterTypesEnum.PRE,
        bizType = "init_dubbo_request1"
)
@Slf4j
public class EmptySpcFilter extends SpcFilter {

    @Autowired
    private RunBizApi2 runBizApi2;

    @Override
    public Boolean bizShouldFilter(){
        Object[] dubboProviderRequest = getDubboProviderRequest();
        DubboRequest1 request1=(DubboRequest1) dubboProviderRequest[0];
        Integer sellerId = request1.getSellerId();
        // TODO: 2019-11-10  判断逻辑 ；
        return true;
    }

    @Override
    public Object run() {
        Object[] dubboProviderRequest = getDubboProviderRequest();
        runBizApi2.consumerTest((DubboRequest1) dubboProviderRequest[0],
                (DubboRequest2)dubboProviderRequest[1]);

        log.info("init_dubbo_request1 .....{}  .",
                JSONObject.toJSONString(dubboProviderRequest));
        return null;
    }

    private Object[] getDubboProviderRequest(){
        SpcContext currentContext = SpcContext.getCurrentContext();
        Object[] dubboProviderRequest = currentContext.getDubboProviderRequest();
        return dubboProviderRequest;
    }
}
