package club.spc.filters.auto.config.filters.test1;

import club.spc.filters.core.supports.BizType;

/**
 * @ClassName:DubboConsumerAPI
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 13:48
 * @Version 1.0.0
 **/
public interface RunBizApi2 {

    public DubboRequest1 consumerTest(DubboRequest1 dto1, DubboRequest2 dto2);
}
