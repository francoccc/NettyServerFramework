package com.franco.spring.core;

/**
 * Result
 *
 * @param <T>
 */
public interface Result<T> {

    /**
     * 获取view的名字
     */
    String getViewName();

    /**
     * 获取结果
     * @return
     */
    T getResult();

    /**
     * 获取字节数
     */
    int getBytesLength();
}
