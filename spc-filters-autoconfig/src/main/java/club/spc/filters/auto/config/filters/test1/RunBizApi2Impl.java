package club.spc.filters.auto.config.filters.test1;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @ClassName:RunBizApi
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 18:00
 * @Version 1.0.0
 **/
@Slf4j
public class RunBizApi2Impl  implements RunBizApi2{

    @Override
    public DubboRequest1 consumerTest(DubboRequest1 dto1, DubboRequest2 dto2) {
        dto2.setFeature(true);
        HashMap<String, String> objectObjectHashMap = Maps.newHashMap();
        objectObjectHashMap.put("data1", JSONObject.toJSONString(dto2));
        dto1.setSkuId(1).setProductId(1).setFeature(objectObjectHashMap);
        log.info("RunBizApi2#consumerTest {}",JSONObject.toJSONString(dto1));
        return dto1;
    }
}
