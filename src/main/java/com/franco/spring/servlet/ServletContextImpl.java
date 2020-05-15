package com.franco.spring.servlet;

import java.util.concurrent.ConcurrentHashMap;

/**
 * ServletContext实现类
 *
 * @author franco
 */
public class ServletContextImpl implements ServletContext {

    /** 属性map */
    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<String, Object>();

    /** 配置 */
    private ServletConfig config;

    public ServletContextImpl() {
    }

    public ServletContextImpl(ServletConfig config) {
        this.config = config;
    }

    public Object getAttribute(String key) {
        return map.get(key);
    }

    public Object setAttribute(String key, Object value) {
        map.put(key, value);
        return map.get(key);
    }

    public boolean removeAttribute(String key) {
        return map.remove(key) != null;
    }

    public void invalidate() {
        map.clear();
    }

    public Object getInitParameter(String key) {
        if (config == null) {
            return null;
        }
        return config.getInitParameter(key);
    }
}
