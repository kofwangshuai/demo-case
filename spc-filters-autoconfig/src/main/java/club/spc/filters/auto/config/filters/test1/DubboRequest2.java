package club.spc.filters.auto.config.filters.test1;

import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName:TestDTO2
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 13:53
 * @Version 1.0.0
 **/
@Slf4j
@Data
@Accessors(chain = true)
public class DubboRequest2 implements Serializable {
   private boolean isSkuId;
   private boolean isSpudId;
   private boolean isFeature;
}
