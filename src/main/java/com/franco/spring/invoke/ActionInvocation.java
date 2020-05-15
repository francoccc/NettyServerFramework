package com.franco.spring.invoke;

import com.franco.spring.adaptor.HttpAdaptor;
import com.franco.spring.adaptor.PairAdaptor;
import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;
import com.franco.spring.servlet.ServletContext;
import com.franco.spring.interceptor.Interceptor;
import com.franco.spring.view.NullView;
import com.franco.spring.view.ResponseView;
import com.franco.spring.view.ViewManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * Invocation的实现类
 *
 * @author franco
 */
public class ActionInvocation implements Invocation {

    private ServletContext context;
    private Object action;
    private Method method;
    private ViewManager vm;
    private HttpAdaptor adaptor;
    private String actionName;
    private String methodName;


    public ActionInvocation(ServletContext context, ViewManager vm, Object action, Method method) {
        this.context = context;
        this.action = action;
        this.method = method;
        this.vm = vm;
        this.actionName = action.getClass().getName();
        this.methodName = method.getName();
    }

    public void init() {
        initAdaptor();
    }

    private void initAdaptor() {
        adaptor = new PairAdaptor();
        adaptor.init(context, method);
    }

    public Object invoke(Iterator<Interceptor> interceptor, Request request, Response response)
            throws IllegalAccessException, InvocationTargetException {
        // 可以进行同步操作
        // 关键
        return _invoke(interceptor, request, response);
    }

    public Object _invoke(Iterator<Interceptor> interceptor, Request request, Response response)
            throws IllegalAccessException, InvocationTargetException {
        if(interceptor.hasNext()) {
            interceptor.next().intercept(this, interceptor, request, response);
        }
        Object[] params = adaptor.adapt(context, request, response);
        request.setRequestArgs(params);
        return method.invoke(action, params);
    }

    public void render(Result<?> result, Request request, Response response) {
        ResponseView view = getView(result);
        view.render(result, request, response);
    }

    public ResponseView getView(Result<?> result) {
        String viewName = result.getViewName();
        return vm.getView(viewName) != null ? vm.getView(viewName) : new NullView();
    }

    public String getActionName() {
        return actionName;
    }
}
