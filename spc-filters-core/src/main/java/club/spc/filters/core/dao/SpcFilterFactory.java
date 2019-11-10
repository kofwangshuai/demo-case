package club.spc.filters.core.dao;

import club.spc.filters.core.SpcFilter;

/**
 * @ClassName:FilterFactory
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:42
 * @Version 1.0.0
 *
 *  filter 工厂类
 **/
public interface SpcFilterFactory {

    SpcFilter newInstance(Class clazz) throws Exception;

    class DefaultSpcFilterFactory implements SpcFilterFactory {

        @Override
        public SpcFilter newInstance(Class clazz) throws Exception {
            return (SpcFilter) clazz.newInstance();
        }
    }
}
