package club.spc.filters.core.constants;

/**
 * @ClassName:SpcContants
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 11:26
 * @Version 1.0.0
 **/
public class SpcContants {

    public static final String SPC_FILTRER_TYPE_PRE_DO="pre_todo"; //  预处理
    public static final String SPC_FILTRER_TYPE_SIDE_EFFECT="side_effect"; // 和db有交互的预处理
    public static final String SPC_FILTRER_TYPE_BIZ_DOING="biz_doing"; // 处理业务
    public static final String SPC_FILTRER_TYPE_AFRTER_DONE="after_done"; // 处理业务后
    public static final String SPC_FILTRER_TYPE_ERROR="after_done"; //  处理业务后


    public static final String SPC_FILTRER_HTTP_ENGINE_RAN="spcFilterEngineRan";  // spc filter 启动运行
    public static final String SPC_FILTRER_DUBBO_PROVIDER_ENGINE_RAN="spcfilter_dubboprovider_engineran";  // spc filter 启动运行
    public static final String SPC_REQUEST="spc_request";
    public static final String SPC_RESPONSE="spc_response";
    public static final String SPC_THROWABLE="spc_throwable";
    public static final String SPC_BUFFER_REQUESTS="buffer-requests"; // 判断请求的body信息 ，是否可以返回读取的开关


    public static final String SKU_BIZ_TYPE="sku";
    public static final String SPU_BIZ_TYPE="spu";


    // Prevent instantiation
    private SpcContants() {
        throw new AssertionError("Must not instantiate constant utility class");
    }
}
