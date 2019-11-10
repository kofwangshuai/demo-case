package club.spc.filters.core.utils;

/**
 * @ClassName:Tracer
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-26 17:39
 * @Version 1.0.0
 **/
public interface Tracer {
    /**
     * Stops and Logs a time based tracer
     *
     */
    void stopAndLog();

    /**
     * Sets the name for the time based tracer
     *
     * @param name a <code>String</code> value
     */
    void setName(String name);
}
