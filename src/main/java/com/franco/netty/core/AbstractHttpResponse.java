package com.franco.netty.core;

import com.franco.spring.core.Cookies;

import java.util.Map;

/**
 * Http请求
 *
 * @author franco
 */
public abstract class AbstractHttpResponse extends AbstractResponse implements Cookies {

    /**
     * 设置响应头
     * @param name
     * @param value
     */
    abstract void addHeader(String name, String value);

    abstract Map<String, String> getHeaders();

    abstract byte[] getContent();

    abstract void setStatus(Object status);

    abstract Object getStatus();
}
