package club.spc.filters.core.exception;

import club.spc.filters.core.supports.CounterFactory;

/**
 * @ClassName:SpcException
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:16
 * @Version 1.0.0
 **/
public class SpcException  extends Exception{

    public static final  Integer SUCCESS=200;
    public static final  Integer FAIL=400;
    private static final Integer UNKOWN=500;

    public int nStatusCode;
    public String errorCause;

    /**
     * Source Throwable, message, status code and info about the cause
     * @param throwable
     * @param sMessage
     * @param nStatusCode
     * @param errorCause
     */
    public SpcException(Throwable throwable, String sMessage, int nStatusCode, String errorCause) {
        super(sMessage, throwable);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
        incrementCounter("SPC_FILTERS::EXCEPTION:" + errorCause + ":" + nStatusCode);
    }

    /**
     * error message, status code and info about the cause
     * @param sMessage
     * @param nStatusCode
     * @param errorCause
     */
    public SpcException(String sMessage, int nStatusCode, String errorCause) {
        super(sMessage);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
        incrementCounter("SPC_FILTERS::EXCEPTION:" + errorCause + ":" + nStatusCode);

    }

    /**
     * Source Throwable,  status code and info about the cause
     * @param throwable
     * @param nStatusCode
     * @param errorCause
     */
    public SpcException(Throwable throwable, int nStatusCode, String errorCause) {
        super(throwable.getMessage(), throwable);
        this.nStatusCode = nStatusCode;
        this.errorCause = errorCause;
        incrementCounter("SPC_FILTERS::EXCEPTION:" + errorCause + ":" + nStatusCode);

    }

    public SpcException(Throwable throwable, ErrorMessage notEnableDubboProvider) {

        super(throwable.getMessage(), throwable);
        this.nStatusCode =getNStatusCode(notEnableDubboProvider);
        this.errorCause = notEnableDubboProvider.getMessage();
        incrementCounter("SPC_FILTERS::EXCEPTION:" + errorCause + ":" + nStatusCode);
    }

    private static final void incrementCounter(String name) {
        // 异常监控，上报cat监控
        CounterFactory.instance().increment(name);
    }

    private static final Integer getNStatusCode(ErrorMessage errorMessage){
       return Integer.valueOf(errorMessage.getErrorCode()
                .substring(Codes.CODE_PRIFEX.length(),errorMessage.getErrorCode().length()-1));
    }
}
