package club.spc.filters.auto.config.endpoints;

import club.spc.filters.core.SpcFilter;
import club.spc.filters.core.dao.SpcFilterRegistry;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;

import java.util.*;

/**
 * @ClassName:SpcEndpoints
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-27 11:48
 * @Version 1.0.0
 * 3  后续待完善的功能 ： 支持动态查询线上过滤器列表功能
 * 4
 **/
@Endpoint(id = "/spc/filters")
public class SpcEndpoints {

    private final SpcFilterRegistry spcFilterRegistry;

    public SpcEndpoints(SpcFilterRegistry spcFilterRegistry) {
        this.spcFilterRegistry = spcFilterRegistry;
    }

    @ReadOperation
    public Map<String, List<Map<String, Object>>> invoke() {
        // Map of filters by type
        final Map<String, List<Map<String, Object>>> filterMap = new TreeMap<>();

        for (SpcFilter filter : this.spcFilterRegistry.getAllFilters()) {
            // Ensure that we have a list to store filters of each type
            if (!filterMap.containsKey(filter.filterType())) {
                filterMap.put(filter.filterType(), new ArrayList<>());
            }

            final Map<String, Object> filterInfo = new LinkedHashMap<>();
            filterInfo.put("class", filter.getClass().getName());
            filterInfo.put("order", filter.filterOrder());
            filterInfo.put("disabled", filter.shouldFilter());
            filterMap.get(filter.filterType()).add(filterInfo);
        }
        return filterMap;
    }

}