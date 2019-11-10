package club.spc.filters.core.dao;

import club.spc.filters.core.SpcFilter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * @ClassName:DynamicCodeCompiler
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:42
 * @Version 1.0.0
 **/
public interface SpcDynamicCompiler {

    // 3 动态加载过滤器的源码字符串方式。
    Class compile(String sCode, String sName);

    // 2 动态加载class文件对象。
    Class compile(File file);

    // 1  接入apollo配置中心，动态上下线过滤器。
    void compile(Map<String, Boolean> apolloFlag);

    class DefaultSpcDynamicCompiler implements SpcDynamicCompiler {

        @Autowired
        private SpcFilterRegistry spcFilterRegistry;

        @Override
        public Class compile(String sCode, String sName) {
            return null;
        }

        @Override
        public Class compile(File file) {
            return null;
        }

        @Override
        public void compile(Map<String, Boolean> apolloFlag) {

            Collection<SpcFilter> allFilters = spcFilterRegistry.getAllFilters();
            allFilters.forEach(
                    filter -> {
                        filter.apolloFlag(apolloFlag.get(filter.getClass().getName()));
                    }
            );
            return ;
        }
    }
}
