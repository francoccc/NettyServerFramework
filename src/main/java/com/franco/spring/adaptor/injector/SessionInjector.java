package com.franco.spring.adaptor.injector;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

/**
 * Session注入器将Session注入到action参数
 *
 * @author franco
 */
public class SessionInjector implements ParamInjector {

    private String name;

    public SessionInjector(String name) {
        this.name = name;
    }

    public Object get(ServletContext context, Request request, Response response) {
        return request.getSession().getAttribute(name);
    }
}
