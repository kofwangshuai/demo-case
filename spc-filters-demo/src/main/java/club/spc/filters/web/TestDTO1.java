package club.spc.filters.web;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName:TestDTO1
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 13:52
 * @Version 1.0.0
 **/
@Slf4j
@Data
@Accessors(chain = true)
public class TestDTO1 implements Serializable {
    private Integer skuId;
    private Integer productId;
    private Map<String,String> feature;
}
