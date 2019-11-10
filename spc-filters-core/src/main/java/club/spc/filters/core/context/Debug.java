package club.spc.filters.core.context;

import club.spc.filters.core.context.SpcContext;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName:Debug
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:37
 * @Version 1.0.0
 **/
public class Debug {

    public static void setDebugRequest(boolean bDebug) {
        SpcContext.getCurrentContext().setDebugRequest(bDebug);
    }

    public static void setDebugRequestHeadersOnly(boolean bHeadersOnly) {
        SpcContext.getCurrentContext().setDebugRequestHeadersOnly(bHeadersOnly);
    }

    public static boolean debugRequestHeadersOnly() {
        return SpcContext.getCurrentContext().debugRequestHeadersOnly();
    }


    public static void setDebugRouting(boolean bDebug) {
        SpcContext.getCurrentContext().setDebugRouting(bDebug);
    }


    public static boolean debugRequest() {
        return SpcContext.getCurrentContext().debugRequest();
    }

    public static boolean debugRouting() {
        return SpcContext.getCurrentContext().debugRouting();
    }

    public static void addRoutingDebug(String line) {
        List<String> rd = getRoutingDebug();
        rd.add(line);
    }

    /**
     *
     * @return Returns the list of routiong debug messages
     */
    public static List<String> getRoutingDebug() {
        List<String> rd = (List<String>) SpcContext.getCurrentContext().get("routingDebug");
        if (rd == null) {
            rd = new ArrayList<String>();
            SpcContext.getCurrentContext().set("routingDebug", rd);
        }
        return rd;
    }

    /**
     * Adds a line to the  Request debug messages
     * @param line
     */
    public static void addRequestDebug(String line) {
        List<String> rd = getRequestDebug();
        rd.add(line);
    }

    /**
     *
     * @return returns the list of request debug messages
     */
    public static List<String> getRequestDebug() {
        List<String> rd = (List<String>) SpcContext.getCurrentContext().get("requestDebug");
        if (rd == null) {
            rd = new ArrayList<String>();
            SpcContext.getCurrentContext().set("requestDebug", rd);
        }
        return rd;
    }


    /**
     * Adds debug details about changes that a given filter made to the request context.
     * @param filterName
     * @param copy
     */
    public static void compareContextState(String filterName, SpcContext copy) {
        SpcContext context = SpcContext.getCurrentContext();
        Iterator<String> it = context.keySet().iterator();
        String key = it.next();
        while (key != null) {
            if ((!key.equals("routingDebug") && !key.equals("requestDebug"))) {
                Object newValue = context.get(key);
                Object oldValue = copy.get(key);
                if (oldValue == null && newValue != null) {
                    addRoutingDebug("{" + filterName + "} added " + key + "=" + newValue.toString());
                } else if (oldValue != null && newValue != null) {
                    if (!(oldValue.equals(newValue))) {
                        addRoutingDebug("{" +filterName + "} changed " + key + "=" + newValue.toString());
                    }
                }
            }
            if (it.hasNext()) {
                key = it.next();
            } else {
                key = null;
            }
        }

    }




}