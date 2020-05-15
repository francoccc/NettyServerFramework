package com.franco.spring.adaptor.injector;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

/**
 * RequestInjector
 *
 * @author franco
 */
public class RequestInjector implements ParamInjector{

    public RequestInjector() {

    }

    public Object get(ServletContext context, Request request, Response response) {
        return request;
    }
}
