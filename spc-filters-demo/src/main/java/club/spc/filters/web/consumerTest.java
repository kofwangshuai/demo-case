package club.spc.filters.web;

import club.spc.filters.auto.config.filters.test1.DubboRequest1;
import club.spc.filters.auto.config.filters.test1.DubboRequest2;
import club.spc.filters.core.supports.BizType;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName:consumerTest
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 14:00
 * @Version 1.0.0
 **/
@Slf4j
@Service
public class consumerTest implements DubboConsumerAPI {

    @Reference
    private DubboProviderAPI dubboProviderAPI;

    private Integer skuId=0;
    private Integer SpudId=0;

    @Override
//    @BizType(value = {"init_dubbo_request1"})
    public DubboRequest1 consumerTest(DubboRequest1 dto1, DubboRequest2 dto2) {

        log.info("DubboConsumerAPI#consumerTest start is {}",JSONObject.toJSONString(dto1));
        DubboRequest1 testDTO1 = dubboProviderAPI.provideTest(dto1, dto2);
        log.info("DubboConsumerAPI#consumerTest end is {}",JSONObject.toJSONString(dto1));
        return testDTO1;
    }
}
