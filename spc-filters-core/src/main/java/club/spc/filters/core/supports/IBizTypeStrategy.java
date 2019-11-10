package club.spc.filters.core.supports;

/**
 * @ClassName:IBizTypeStrategy
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 12:42
 * @Version 1.0.0
 **/
public interface IBizTypeStrategy {


    String[] getBizInfo(BizType bizType);

    class DefaultBizTypeStrategy implements IBizTypeStrategy {

        public DefaultBizTypeStrategy(){
        }

        @Override
        public String[] getBizInfo(BizType bizType) {
            String[] value = bizType.value();
            return value;
        }
    }
}
