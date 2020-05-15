package com.franco.spring.core;

import com.franco.common.ServerProtocol;
import com.franco.spring.servlet.ServletContext;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;

/**
 * 应用外部的请求
 *
 * @author franco
 */
public interface Request {


    Map<String, String[]> getParameterMap();

    Object[] getRequestArgs();

    void setRequestArgs(Object[] args);

    String getCommand();

    Object getChannel();

    ServerProtocol getServerProtocol();

    ServletContext getServletContext();
    /**
     * 获取参数
     * 需转化
     * @param name
     * @return
     */
    String[] getParameterValues(String name);

    int getRequestId();

    /** Session */
    Session getSession();

    Session getSession(boolean allowCreate);

    Session getNewSession();

    /**
     * 是否属于http长链接
     * @return
     */
    boolean isHttpLong();

    /**
     * 获取远端地址
     */
    InetSocketAddress getRemoteAddress();

    byte[] getContent();

//    String getHeader(String key);

    void pushAndClose(Object buffer);

    String getIp();

    Push newPush();

    long createTime();
}
