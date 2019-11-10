package club.spc.filters.core.exception;

/**
 * @ClassName:Codes
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-11-10 17:20
 * @Version 1.0.0
 **/
public class Codes {

    public static final String CODE_PRIFEX="SPC_FILTERS::";


    /**
     * not enable dubbo provider engine ran
     */
    public static final ErrorMessage NOT_ENABLE_DUBBO_PROVIDER =  ErrorMessage.of(CODE_PRIFEX+"30002",
            "not enable dubbo provider engine ran");
}
