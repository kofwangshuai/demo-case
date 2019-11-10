package club.spc.filters.web;

import club.spc.filters.auto.config.filters.test1.DubboRequest1;
import club.spc.filters.auto.config.filters.test1.DubboRequest2;
import club.spc.filters.core.supports.BizType;

/**
 * @ClassName:DubboProviderAPI
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 13:48
 * @Version 1.0.0
 **/
public interface DubboProviderAPI {

    /**
     * 思科待定的方案 todo ？？？？？ 更方便使用和直观 ；
     * 1  按照编码的 orders 执行顺序
     * 2  按照注解中 bizType的values属性执行 ？？？
     * @param dto1
     * @param dto2
     * @return
     */
    @BizType(value = {"init_dubbo_request1"})
    public DubboRequest1 provideTest(DubboRequest1 dto1 , DubboRequest2 dto2);
}
