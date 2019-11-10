package club.spc.filters.web;

import club.spc.filters.auto.config.filters.test1.DubboRequest1;
import club.spc.filters.auto.config.filters.test1.DubboRequest2;
import club.spc.filters.core.supports.BizType;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName:DubboProviderAPIImpl
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 13:51
 * @Version 1.0.0
 **/

@Service
@Slf4j
public class DubboProviderAPIImpl implements DubboProviderAPI {

    public DubboRequest1 provideTest(DubboRequest1 dto1 , DubboRequest2 dto2){
        log.info("DubboProviderAPI#provideTest end is {}",JSONObject.toJSONString(dto1));
        return dto1;
    }
}
