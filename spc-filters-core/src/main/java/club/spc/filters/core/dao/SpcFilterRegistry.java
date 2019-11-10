package club.spc.filters.core.dao;


import club.spc.filters.core.SpcFilter;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName:FilterRegistry
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:41
 * @Version 1.0.0
 *
 * 2 filterDAO层 ：增删该改查
 **/
public class SpcFilterRegistry {

    private static final SpcFilterRegistry INSTANCE = new SpcFilterRegistry();

    public static final SpcFilterRegistry instance() {
        return INSTANCE;
    }

    private final ConcurrentHashMap<String, SpcFilter> filters = new ConcurrentHashMap<String, SpcFilter>();

    private SpcFilterRegistry() {
    }

    public SpcFilter remove(String key) {
        return this.filters.remove(key);
    }

    public SpcFilter get(String key) {
        return this.filters.get(key);
    }

    public void put(String key, SpcFilter filter) {
        this.filters.putIfAbsent(key, filter);
    }

    public int size() {
        return this.filters.size();
    }

    public Collection<SpcFilter> getAllFilters() {
        return this.filters.values();
    }
}
