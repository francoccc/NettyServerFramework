package com.franco.spring.adaptor.injector;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

/**
 * Response注入器将Response注入到action
 *
 * @author franco
 */
public class ResponseInjector implements ParamInjector {

    public ResponseInjector() {

    }

    public Object get(ServletContext context, Request request, Response response) {
        return response;
    }
}
