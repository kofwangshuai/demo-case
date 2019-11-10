package club.spc.filters.core.dao;

import club.spc.filters.core.SpcFilter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:FilterLoader
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:39
 * @Version 1.0.0
 *
 * 2  filter 加载类
 *
 **/
@Slf4j
@Deprecated
public class SpcFilterLoader {

    final static SpcFilterLoader INSTANCE = new SpcFilterLoader();

    // 1  最新被修改的 过滤器集合
//    private final ConcurrentHashMap<String, Long> filterClassLastModified = new ConcurrentHashMap<String, Long>();
    // 2  类源码字符串
//    @Deprecated
//    private final ConcurrentHashMap<String, String> filterClassCode = new ConcurrentHashMap<String, String>();
//    private final ConcurrentHashMap<String, String> filterCheck = new ConcurrentHashMap<String, String>();
    private final ConcurrentHashMap<String, List<SpcFilter>> hashFiltersByType = new ConcurrentHashMap<String, List<SpcFilter>>();

    private SpcFilterRegistry spcFilterRegistry = SpcFilterRegistry.instance();

    static SpcDynamicCompiler COMPILER;

//    static SpcFilterFactory FILTER_FACTORY = new SpcFilterFactory.DefaultSpcFilterFactory();

//    public void setCompiler(SpcDynamicCompiler compiler) {
//        COMPILER = compiler;
//    }
//
//    // overidden by tests
//    public void setSpcFilterRegistry(SpcFilterRegistry r) {
//        this.spcFilterRegistry = r;
//    }
//
//    public void setFilterFactory(SpcFilterFactory factory) {
//        FILTER_FACTORY = factory;
//    }

    public static SpcFilterLoader getInstance() {
        return INSTANCE;
    }

//    public SpcFilter getFilter(String sCode, String sName) throws Exception {
//
//        if (filterCheck.get(sName) == null) {
//            filterCheck.putIfAbsent(sName, sName);
//            if (!sCode.equals(filterClassCode.get(sName))) {
//                log.info("reloading code " + sName);
//                spcFilterRegistry.remove(sName);
//            }
//        }
//        SpcFilter filter = spcFilterRegistry.get(sName);
//        if (filter == null) {
//            Class clazz = COMPILER.compile(sCode, sName);
//            if (!Modifier.isAbstract(clazz.getModifiers())) {
//                filter = (SpcFilter) FILTER_FACTORY.newInstance(clazz);
//            }
//        }
//        return filter;
//
//    }

    /**
     * @return the total number of Zuul filters
     */
    public int filterInstanceMapSize() {
        return spcFilterRegistry.size();
    }


    /**
     * From a file this will read the ZuulFilter source code, compile it, and add it to the list of current filters
     * a true response means that it was successful.
     *
     * @return true if the filter in file successfully read, compiled, verified and added to Zuul
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IOException
     */
//    @Deprecated
//    public boolean putFilter(File file) throws Exception {
//        String sName = file.getAbsolutePath() + file.getName();
//        if (filterClassLastModified.get(sName) != null && (file.lastModified() != filterClassLastModified.get(sName))) {
//            log.debug("reloading filter " + sName);
//            spcFilterRegistry.remove(sName);
//        }
//        SpcFilter filter = spcFilterRegistry.get(sName);
//        if (filter == null) {
//            Class clazz = COMPILER.compile(file);
//            if (!Modifier.isAbstract(clazz.getModifiers())) {
//                filter = (SpcFilter) FILTER_FACTORY.newInstance(clazz);
//                List<SpcFilter> list = hashFiltersByType.get(filter.filterType());
//                if (list != null) {
//                    hashFiltersByType.remove(filter.filterType()); //rebuild this list
//                }
//                spcFilterRegistry.put(file.getAbsolutePath() + file.getName(), filter);
//                filterClassLastModified.put(sName, file.lastModified());
//                return true;
//            }
//        }
//
//        return false;
//    }

    public boolean putFilterFirst() throws Exception {
        Collection<SpcFilter> filters = spcFilterRegistry.getAllFilters();
        if (filters == null) {
            COMPILER.compile(Maps.newHashMap());
            filters.forEach(filter->{
                List<SpcFilter> list = hashFiltersByType.get(filter.filterType());
                if (list != null) {
                    hashFiltersByType.remove(filter.filterType());
                }
            });
            return true;
        }

        return false;
    }

    // TODO: 2019-11-10  动态上下线已经生效的过滤器 ：
//    public boolean putFilter(Map<String,Boolean> apolloFlags) throws Exception {
////        String sName = file.getAbsolutePath() + file.getName();
////        if (filterClassLastModified.get(sName) != null && (file.lastModified() != filterClassLastModified.get(sName))) {
////            log.debug("reloading filter " + sName);
////            spcFilterRegistry.remove(sName);
////        }
//        SpcFilter filter = spcFilterRegistry.get(sName);
//        if (filter == null) {
//            Class clazz = COMPILER.compile(file);
//            if (!Modifier.isAbstract(clazz.getModifiers())) {
//                filter = (SpcFilter) FILTER_FACTORY.newInstance(clazz);
//                List<SpcFilter> list = hashFiltersByType.get(filter.filterType());
//                if (list != null) {
//                    hashFiltersByType.remove(filter.filterType()); //rebuild this list
//                }
//                spcFilterRegistry.put(file.getAbsolutePath() + file.getName(), filter);
//                filterClassLastModified.put(sName, file.lastModified());
//                return true;
//            }
//        }
//
//        return false;
//    }

    public List<SpcFilter> getFiltersByType(String filterType) {

        List<SpcFilter> list = hashFiltersByType.get(filterType);
        if (list != null) return list;

        list = new ArrayList<SpcFilter>();

        Collection<SpcFilter> filters = spcFilterRegistry.getAllFilters();
        for (Iterator<SpcFilter> iterator = filters.iterator(); iterator.hasNext(); ) {
            SpcFilter filter = iterator.next();
            if (filter.filterType().equals(filterType)) {
                list.add(filter);
            }
        }
        Collections.sort(list); // sort by priority

        hashFiltersByType.putIfAbsent(filterType, list);
        return list;
    }





}
