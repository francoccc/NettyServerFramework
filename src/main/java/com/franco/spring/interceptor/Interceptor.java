package com.franco.spring.interceptor;

import com.franco.spring.invoke.ActionInvocation;
import com.franco.spring.core.Request;
import com.franco.spring.core.Response;

import java.util.Iterator;

/**
 * 拦截器
 *
 * @author franco
 */
public interface Interceptor {

    /**
     * 拦截动作
     * @param invocation
     * @param iter
     * @param request
     * @param response
     */
    void intercept(ActionInvocation invocation, Iterator<Interceptor> iter, Request request, Response response);
}
