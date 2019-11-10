package club.spc.filters.web;

import club.spc.filters.auto.config.filters.test1.DubboRequest1;
import club.spc.filters.auto.config.filters.test1.DubboRequest2;
import club.spc.filters.core.supports.BizType;

/**
 * @ClassName:DubboConsumerAPI
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 13:48
 * @Version 1.0.0
 **/
public interface DubboConsumerAPI {

    /**
     *  1 尽量一个接口定义一个参数的方式 ；
     *  2 尽量把核心参数放在第一个参数的对象中
     *  3 目前支持
     * @param dto1
     * @param dto2
     * @return
     */
    @BizType(value = {"init_dubbo_request1"})
    DubboRequest1 consumerTest(DubboRequest1 dto1 , DubboRequest2 dto2);
}
