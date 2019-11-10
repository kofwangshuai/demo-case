package club.spc.filters.core.context;

import club.spc.filters.core.constants.SpcContants;
import club.spc.filters.core.exception.Codes;
import club.spc.filters.core.exception.SpcException;
import club.spc.filters.core.utils.DeepCopy;
import club.spc.filters.core.utils.Pair;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.NotSerializableException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:SpcContext
 * @Description:  SpcContext 是用于保存调用对应的请求参数,返回结果,以及中间状态信息和数据的上下文.
 * @Author kof.wang
 * @Date : 2019-10-25 02:20
 * @Version 1.0.0
 *
 *
 *  todo : 1 优化===提供确保key不会被意外覆盖的机制 。
 *  todo : 2 优化===需要修改key的状态机制 。
 **/
@Slf4j
public class SpcContext extends ConcurrentHashMap<String, Object> {

    /**
     * 1  上下文的类型 ： SpcContext.class <T,V>
     * 2  允许定义子类扩展功能  {@link SpcContext}
     */
    protected static Class<? extends SpcContext> contextClass = SpcContext.class;

    /**
     * 1  覆盖 通过set方法覆盖当前线程的 spc上下文对象。todo ？？？？ 考虑关闭该功能的入口
     */
    private static SpcContext testContext = null;

    protected static final ThreadLocal<? extends SpcContext> threadLocal = new ThreadLocal<SpcContext>() {
        @Override
        protected SpcContext initialValue() {
            try {
                return contextClass.newInstance();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    };

    public SpcContext() {
        super();
    }

    /**
     *  支持重写覆盖默认的Spc上下文的类型：即为支持扩展 SpcContext
     * @param clazz
     */
    @Deprecated
    public static void setContextClass(Class<? extends SpcContext> clazz) {
        contextClass = clazz;
    }

    /**
     *  1 测试覆盖 SpcContext；
     * @param context
     */
    @Deprecated
    public static void testSetCurrentContext(SpcContext context) {
        testContext = context;
    }


    /**
     *  2 清空spcContext的参数
     */
    public void unset() {
        threadLocal.remove();
    }

    /**
     *  3 SpcContex 请求上下文 深拷贝对象
     *
     * @return
     */
    public SpcContext copy() {
        SpcContext copy = new SpcContext();
        Iterator<String> it = keySet().iterator();
        String key = it.next();
        while (key != null) {
            Object orig = get(key);
            try {
                Object copyValue = DeepCopy.copy(orig);
                if (copyValue != null) {
                    copy.set(key, copyValue);
                } else {
                    copy.set(key, orig);
                }
            } catch (NotSerializableException e) {
                log.error(" key is {} ,orig is {}" ,key, JSONObject.toJSONString(orig));
                copy.set(key, orig);
            }
            if (it.hasNext()) {
                key = it.next();
            } else {
                key = null;
            }
        }
        return copy;
    }

    /**
     *  获取一个SpcContext，
     * @return  the current SpcContext
     */
    public static SpcContext getCurrentContext() {
        if (testContext != null) return testContext;

        SpcContext context = threadLocal.get();
        return context;
    }


    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultResponse) {
        Boolean b = (Boolean) get(key);
        if (b != null) {
            return b.booleanValue();
        }
        return defaultResponse;
    }

    /**
     *  默认set一个 true的布尔值
     * @param key
     */
    public void set(String key) {
        put(key, Boolean.TRUE);
    }

    /**
     *  1 设置一个key 对应的value
     *  2 如果value=null 将会移除 key
     * @param key
     * @param value
     */
    public void set(String key, Object value) {
        if (value != null) put(key, value);
        else remove(key);
    }

//    /**
//     * 1  spc filter 是否启动的开关 :
//     * {@link  SpcContants#SPC_FILTRER_ENGINE_RAN}
//     * @return
//     */
//    public boolean getSpcEngineRan() {
//        return getBoolean(SpcContants.SPC_FILTRER_ENGINE_RAN);// "spcEngineRan"
//    }
//
//    /**
//     * 2  sets zuulEngineRan to true,标示开启spc filter
//     */
//    public void setSpcEngineRan() {
//        put(SpcContants.SPC_FILTRER_ENGINE_RAN, true);
//    }


    /**
     * SPC_FILTRER_DUBBO_PROVIDER_ENGINE_RAN
     */

    public void setDubboProviderEngineRan(){
        set(SpcContants.SPC_FILTRER_DUBBO_PROVIDER_ENGINE_RAN);
    }

    public Boolean getDubboProviderEngineRan(){
        return getBoolean(SpcContants.SPC_FILTRER_DUBBO_PROVIDER_ENGINE_RAN);
    }

    public Object[] getDubboProviderRequest(){
        if (getDubboProviderEngineRan()){
            return (Object[])getRequest();
        }else {
            return null;
        }
    }

    public Result getDubboProviderResponse(){
        if (getDubboProviderEngineRan()){
            return (Result)getRequest();
        }else {
            return null;
        }
    }

    /**
     * @return the HttpServletRequest from the "spc_reqest" key
     *  {@link  SpcContants#SPC_REQUEST}
     */
    public <T> T getRequest() {
        return (T) get(SpcContants.SPC_REQUEST); //"spc_reqest"
    }

    /**
     * {@link  SpcContants#SPC_REQUEST
     *
     * @param params
     */
    public <T> void setRequest(T spc_reqest) {
        put(SpcContants.SPC_REQUEST, spc_reqest);
    }

    public void setRequestType(Class<?>[] clz){
        put("spc_reqest_type", clz);
    }

    public Class[] getRequestType(Class<?>[] clz){
       return (Class[])get("spc_reqest_type");
    }

    /**
     * {@link SpcContants#SPC_REQUEST
     */
    public <V> V getResponse() {
        return (V) get(SpcContants.SPC_RESPONSE);
    }

    /**
     * {@link  SpcContants #SPC_REQUEST
     *
     * @param result
     */
    public <V> void setResponse(V spc_response) {
        set( SpcContants.SPC_RESPONSE, spc_response); // spc_response
    }

    public void setResponseType(){
        put(SpcContants.SPC_RESPONSE+"_type",Result.class);
    }
    /**
     * {@link SpcContants#SPC_THROWABLE
     *
     * @return a set throwable
     */
    public Throwable getThrowable() {
        return (Throwable) get(SpcContants.SPC_THROWABLE); //spc_throwable

    }

    /**
     *  {@link  SpcContants#SPC_THROWABLE
     *
     * @param th
     */
    public void setThrowable(Throwable th) {
        put(SpcContants.SPC_THROWABLE, th);

    }



    /**
     * 1 增加过滤器执行历史记录
     * appends filter name and status to the filter execution history for the
     * current request
     *
     * @param name   filter name
     * @param status execution status
     * @param time   execution time in milliseconds
     */
    public void addFilterExecutionSummary(String name, String status, long time) {
        StringBuilder sb = getFilterExecutionSummary();
        if (sb.length() > 0) sb.append(", ");
        sb.append(name).append('[').append(status).append(']').append('[').append(time).append("ms]");
    }

    /**
     * @return String that represents the filter execution history for the current request
     */
    public StringBuilder getFilterExecutionSummary() {
        if (get("executedFilters") == null) {
            putIfAbsent("executedFilters", new StringBuilder());
        }
        return (StringBuilder) get("executedFilters");
    }

    @Deprecated
    public void setDebugRouting(boolean bDebug) {
        set("_debugRouting", bDebug);
    }

    @Deprecated
    public boolean debugRouting() {
        return getBoolean("_debugRouting");
    }

   @Deprecated
    public void setDebugRequestHeadersOnly(boolean bHeadersOnly) {
        set("_debugRequestHeadersOnly", bHeadersOnly);

    }

    @Deprecated
    public boolean debugRequestHeadersOnly() {
        return getBoolean("_debugRequestHeadersOnly");
    }

    @Deprecated
    public void setDebugRequest(boolean bDebug) {
        set("_debugRequest", bDebug);
    }

    @Deprecated
    public boolean debugRequest() {
        return getBoolean("_debugRequest");
    }

    @Deprecated
    public void removeRouteHost() {
        remove("_routeHost");
    }

    @Deprecated
    public void setRouteHost(URL routeHost) {
        set("_routeHost", routeHost);
    }

    @Deprecated
    public URL getRouteHost() {
        return (URL) get("_routeHost");
    }

    /**
     * sets the "responseBody" value as a String. This is the response sent back to the client.
     *
     * @param body
     */
    public void setResponseBody(String body) {
        set("responseBody", body);
    }

    public String getResponseBody() {
        return (String) get("responseBody");
    }
    @Deprecated
    public void setResponseDataStream(InputStream responseDataStream) {
        set("responseDataStream", responseDataStream);
    }

    @Deprecated
    public void setResponseGZipped(boolean gzipped) {
        put("responseGZipped", gzipped);
    }

    @Deprecated
    public boolean getResponseGZipped() {
        return getBoolean("responseGZipped", true);
    }

    @Deprecated
    public InputStream getResponseDataStream() {
        return (InputStream) get("responseDataStream");
    }

    @Deprecated
    public boolean sendZuulResponse() {
        return getBoolean("sendZuulResponse", true);
    }

    @Deprecated
    public void setSendZuulResponse(boolean bSend) {
        set("sendZuulResponse", Boolean.valueOf(bSend));
    }

    @Deprecated
    public int getResponseStatusCode() {
        return get("responseStatusCode") != null ? (Integer) get("responseStatusCode") : 500;
    }

    @Deprecated
    public void setResponseStatusCode(int nStatusCode) {
//        getResult().setStatus(nStatusCode); // 错误吗设置的处理
        set("responseStatusCode", nStatusCode); //上下文中设置
    }

    @Deprecated
    public void addZuulRequestHeader(String name, String value) {
        getZuulRequestHeaders().put(name.toLowerCase(), value);
    }

    @Deprecated
    public Map<String, String> getZuulRequestHeaders() {
        if (get("zuulRequestHeaders") == null) {
            HashMap<String, String> zuulRequestHeaders = new HashMap<String, String>();
            putIfAbsent("zuulRequestHeaders", zuulRequestHeaders);
        }
        return (Map<String, String>) get("zuulRequestHeaders");
    }

    @Deprecated
    public void addZuulResponseHeader(String name, String value) {
        getZuulResponseHeaders().add(new Pair<String, String>(name, value));
    }

    @Deprecated
    public List<Pair<String, String>> getZuulResponseHeaders() {
        if (get("zuulResponseHeaders") == null) {
            List<Pair<String, String>> zuulRequestHeaders = new ArrayList<Pair<String, String>>();
            putIfAbsent("zuulResponseHeaders", zuulRequestHeaders);
        }
        return (List<Pair<String, String>>) get("zuulResponseHeaders");
    }

    @Deprecated
    public List<Pair<String, String>> getOriginResponseHeaders() {
        if (get("originResponseHeaders") == null) {
            List<Pair<String, String>> originResponseHeaders = new ArrayList<Pair<String, String>>();
            putIfAbsent("originResponseHeaders", originResponseHeaders);
        }
        return (List<Pair<String, String>>) get("originResponseHeaders");
    }

    @Deprecated
    public void addOriginResponseHeader(String name, String value) {
        getOriginResponseHeaders().add(new Pair<String, String>(name, value));
    }

    @Deprecated
    public Long getOriginContentLength() {
        return (Long) get("originContentLength");
    }

    @Deprecated
    public void setOriginContentLength(Long v) {
        set("originContentLength", v);
    }

    @Deprecated
    public void setOriginContentLength(String v) {
        try {
            final Long i = Long.valueOf(v);
            set("originContentLength", i);
        } catch (NumberFormatException e) {
            log.warn("error parsing origin content length", e);
        }
    }

    @Deprecated
    public boolean isChunkedRequestBody() {
        final Object v = get("chunkedRequestBody");
        return (v != null) ? (Boolean) v : false;
    }

    @Deprecated
    public void setChunkedRequestBody() {
        this.set("chunkedRequestBody", Boolean.TRUE);
    }


    @Deprecated
    public Map<String, List<String>> getRequestQueryParams() {
        return (Map<String, List<String>>) get("requestQueryParams");
    }

    @Deprecated
    public void setRequestQueryParams(Map<String, List<String>> qp) {
        put("requestQueryParams", qp);
    }


    public  Class[]  getDubboProviderRequestType() {
      return   getDubboProviderRequestType();
    }
}