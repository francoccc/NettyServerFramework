package com.franco.spring.invoke;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;
import com.franco.spring.interceptor.Interceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public interface Invocation {

    void init();

    Object invoke(Iterator<Interceptor> iter, Request request, Response response) throws IllegalAccessException, InvocationTargetException;

    /**
     * 渲染
     * @param result
     * @param request
     * @param response
     */
    void render(Result<?> result, Request request, Response response);

    /**
     * 获取actionName
     * @return
     */
    String getActionName();
}
