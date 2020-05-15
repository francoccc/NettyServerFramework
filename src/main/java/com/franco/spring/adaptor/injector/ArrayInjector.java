package com.franco.spring.adaptor.injector;

import com.franco.Lang;
import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

/**
 * 数组类型参数注入器
 *
 * @author franco
 */
public class ArrayInjector implements ParamInjector {

    private String name;
    private Class<?> type;


    public ArrayInjector(String name, Class<?> type) {
        this.name = name;
        this.type = type;
    }

    public Object get(ServletContext context, Request request, Response response) {
        String[] params = request.getParameterValues(name);
        return Lang.castTo(params, type);
    }
}
