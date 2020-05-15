package com.franco.spring.adaptor.injector;

import com.franco.Lang;
import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

/**
 * 空参数注入器
 *
 * @author franco
 */
public class NullInjector implements ParamInjector {

    private Class<?> type;

    public NullInjector(Class<?> type) {
        this.type = type;
    }

    public Object get(ServletContext context, Request request, Response response) {
        return Lang.getDefaultValue(type);
    }
}
