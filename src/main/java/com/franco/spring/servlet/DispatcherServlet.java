package com.franco.spring.servlet;

import com.franco.Lang;
import com.franco.spring.annotation.Action;
import com.franco.spring.annotation.Command;
import com.franco.spring.annotation.View;
import com.franco.spring.annotation.Views;
import com.franco.spring.invoke.ActionInvocation;
import com.franco.exception.ServletConfigException;
import com.franco.Scan;
import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;
import com.franco.spring.interceptor.Interceptor;
import com.franco.spring.view.NullView;
import com.franco.spring.view.ResponseView;
import com.franco.spring.view.ViewManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * 分发容器
 *
 * @author franco
 */
public class DispatcherServlet implements Servlet {

    private final static String TAG = "DispatcherServlet";
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ServletContext context;
    private ServletConfig config;
    private ApplicationContext applicationContext;
    private Map<String, ActionInvocation> handleMap = new HashMap<String, ActionInvocation>();
    private List<Interceptor> interceptors = new ArrayList<Interceptor>();
    private static final ResponseView DEFAULT_VIEW = new NullView();

    public DispatcherServlet(ServletContext context, ServletConfig config) {
        this.context = context;
        this.config = config;
        this.applicationContext = (ApplicationContext) context.getAttribute("applicationContext");
    }

    public void init() {
        String actionPackage = (String) config.getInitParameter(ACTION_SCAN_PATH);
        log.info("{} start scan action path:{}", TAG, actionPackage);
        initInterceptors();
        initHandleAction(actionPackage);
    }

    private void initInterceptors() {

    }

    private void initHandleAction(String pack) {
        Set<Class<?>> classes = Scan.getClassed(pack);
        for(Class<?> clazz : classes) {
            initHandleAction(clazz);
        }
    }

    private void initHandleAction(Class clazz) {
        if(Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
            return;
        }
        Action action = Lang.getAnnotation(clazz, Action.class);
        if(null != action) {
            ViewManager vm = new ViewManager(DEFAULT_VIEW);
            Views views = Lang.getAnnotation(clazz, Views.class);
            if(views != null) {
                for(View view : views.value()) {
                    String viewName = view.name();
                    Class<? extends ResponseView> type = view.type();
                    try {
                        ResponseView responseView = type.getDeclaredConstructor().newInstance();
                        vm.addView(viewName, responseView);
                    } catch (Throwable t) {
                        throw new ServletConfigException("config view error");
                    }
                }
            } else {
                View view = Lang.getAnnotation(clazz, View.class);
                if(view != null) {
                    String viewName = view.name();
                    Class<? extends ResponseView> type = view.type();
                    try {
                        ResponseView responseView = type.getDeclaredConstructor().newInstance();
                        vm.addView(viewName, responseView);
                    } catch (Throwable t) {
                        throw new ServletConfigException("config view error");
                    }
                }
            }
            createActionInvocation(clazz, vm);
        }
    }

    private void createActionInvocation(Class<?> clazz, ViewManager vm) {
        Object action = null;
        try {
//           action = clazz.getDeclaredConstructor().newInstance();
            action = applicationContext.getBean(clazz);
        } catch (Throwable t) {
            log.error("can not found action of {}", clazz);
        }
        for(Method method : clazz.getMethods()) {
            Command command = Lang.getAnnotation(clazz, method, Command.class);
            if(command != null) {
                ActionInvocation ai = new ActionInvocation(context, vm, action, method);
                ai.init();
                handleMap.put(command.value(), ai);
                log.info("bind action : {}, with found command : {} ", ai.getActionName(), command.value());
            }
        }
    }

    private boolean stop = false;
    public void service(Request request, Response response) {
        if(stop) {
            return;
        }
        ActionInvocation ai = handleMap.get(request.getCommand());
        if (ai != null) {
            try {
                Result<?> result = (Result<?>) ai.invoke(interceptors.iterator(), request, response);
                ai.render(result, request, response);
            } catch (Exception e) {
                log.error("dipatcher servlet service error : ", e);
            }
        }
    }

    public void destroy() {
        stop = true;
    }

    public ServletConfig getServletConfig() {
        return config;
    }

    public ServletContext getServletContext() {
        return context;
    }
}
