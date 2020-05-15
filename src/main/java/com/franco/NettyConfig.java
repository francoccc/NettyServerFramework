package com.franco;

import java.util.Map;

/**
 * NettyConfig
 *
 * @author franco
 */
public interface NettyConfig {

    /**
     * 获取初始化参数
     * @param key
     * @return
     */
    Object getInitParameter(String key);

    /**
     * 获取所有参数
     * @return
     */
    Map<String, Object> getInitParameters();
}
