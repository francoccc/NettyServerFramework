package com.franco.spring.servlet;

/**
 * ServletContext
 *
 * @author franco
 */
public interface ServletContext {

    /**
     * 获取属性
     * @param key
     * @return
     */
    Object getAttribute(String key);

    /**
     * 设置属性
     * @param key
     * @param value
     * @return
     */
    Object setAttribute(String key, Object value);

    /**
     * 移除属性
     * @param key
     * @return
     */
    boolean removeAttribute(String key);

    /**
     * 使得所有属性失效
     */
    void invalidate();

    /**
     * 获取初始化属性值
     * @param key
     * @return
     */
    Object getInitParameter(String key);
}
