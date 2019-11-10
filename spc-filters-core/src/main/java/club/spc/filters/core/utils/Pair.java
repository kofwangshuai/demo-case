package club.spc.filters.core.utils;


import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * @ClassName:Pair
 * @Description: Spring boot 规约大于配置
 * @Author kof.wang
 * @Date : 2019-10-25 02:33
 * @Version 1.0.0
 **/
public class Pair<E1,E2> implements Serializable {

    private static final long serialVersionUID = 2L;

    private E1 mFirst;
    private E2 mSecond;

    public Pair(E1 first, E2 second) {
        mFirst = first;
        mSecond = second;
    }

    public E1 first() {
        return mFirst;
    }

    public E2 second() {
        return mSecond;
    }

    public void setFirst(E1 first) {
        mFirst = first;
    }

    public void setSecond(E2 second) {
        mSecond = second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equal(mFirst, pair.mFirst) &&
                Objects.equal(mSecond, pair.mSecond);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mFirst, mSecond);
    }
} //