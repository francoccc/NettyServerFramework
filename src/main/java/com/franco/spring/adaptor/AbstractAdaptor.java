package com.franco.spring.adaptor;

import com.franco.spring.adaptor.injector.ParamInjector;
import com.franco.spring.adaptor.injector.RequestInjector;
import com.franco.spring.adaptor.injector.ResponseInjector;
import com.franco.spring.adaptor.injector.SessionInjector;
import com.franco.spring.annotation.RequestParam;
import com.franco.spring.annotation.SessionParam;
import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 适配器
 *
 * @author franco
 */
public abstract class AbstractAdaptor implements HttpAdaptor {

    /** 参数解调器 */
    protected ParamInjector[] injectors;

    public void init(ServletContext context, Method method) {
        Class<?>[] types = method.getParameterTypes();
        Annotation[][] parameterAnns = method.getParameterAnnotations();
        injectors = new ParamInjector[parameterAnns.length];
        for(int i = 0; i < parameterAnns.length; i++) {
            SessionParam sessionParam = null;
            RequestParam requestParam = null;
            for(int j = 0; j < parameterAnns[i].length; j++) {
                if(parameterAnns[i][j] instanceof SessionParam) {
                    sessionParam = (SessionParam) parameterAnns[i][j];
                    break;
                }
                if(parameterAnns[i][j] instanceof RequestParam) {
                    requestParam = (RequestParam) parameterAnns[i][j];
                    break;
                }
            }
            if(null != sessionParam) {
                injectors[i] = new SessionInjector(sessionParam.value());
            }
            if(null != injectors[i]) {
                continue;
            }
            injectors[i] = evalInjectorByType(types[i]);
            if(null != injectors[i]) {
                continue;
            }
            injectors[i] = evalInjector(types[i], requestParam);
        }
    }

    public Object[] adapt(ServletContext context, Request request, Response response) {
        Object[] args = new Object[injectors.length];
        for(int i = 0; i < injectors.length; i++) {
            args[i] = injectors[i].get(context, request, response);
        }
        return args;
    }

    private ParamInjector evalInjectorByType(Class<?> clazz) {
        if(Request.class.isAssignableFrom(clazz)) {
            return new RequestInjector();
        }
        else if(Response.class.isAssignableFrom(clazz)) {
            return new ResponseInjector();
        }
        return null;
    }

    protected abstract ParamInjector evalInjector(Class<?> clazz, RequestParam requestParam);
}
