package com.franco.spring.servlet;

import java.util.List;
import java.util.Map;

/**
 * ServletConfig配置
 *
 * @author franco
 */
public interface ServletConfig {

    /**
     * 获取Servlet名字
     * @return
     */
    String getServletName();

    /**
     * 获取Servlet的实现类
     * @return
     */
    Class<? extends Servlet> getServletClass();

    /**
     * 获取所有的监听
     * @return
     */
    List<Class<?>> getListeners();

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

    long getSessionTimeOutMillis();

    long getSessionInvalidateMillis();

    long getSessionEmptyTimeOutMillis();
    
    long getSessionTickTime();

    String SCAN_PACKAGE = "scanPackages";
    String ACTION_PACKAGE = "actionPackages";
    String HISTORY_MESSAGE_LEN = "historyMsgLen";
    String SERVER_NAME = "serverName";
}