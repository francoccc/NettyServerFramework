package com.franco.spring.servlet;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;

import java.io.Serializable;

/**
 * Servlet 容器
 *
 * @author franco
 */
public interface Servlet extends Serializable  {

    /**
     * 初始化容器
     */
    void init();

    /**
     * 执行对应的服务逻辑
     *
     * @param req
     * @param res
     */
    void service(Request req, Response res);

    /**
     * 摧毁servlet的时候调用的相关逻辑
     */
    void destroy();

    /**
     * 获取Servlet配置
     */
    ServletConfig getServletConfig();

    /**
     * 获取Servlet上下文
     */
    ServletContext getServletContext();

    /**
     * 扫描Action的路径
     */
    String ACTION_SCAN_PATH = "actionPackages";

    String ACTION_INTERCEPTOR = "actionInterceptor";
}
