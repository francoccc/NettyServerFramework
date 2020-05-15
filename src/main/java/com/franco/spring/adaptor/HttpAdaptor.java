package com.franco.spring.adaptor;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

import java.lang.reflect.Method;

/**
 * HttpAdaptor
 * <br></br>
 * 类似于完整Http响应的参数适配器,对于{@code Action}动作中具体逻辑代码method的适配，
 * 配合ParamInjector参数注解器来完成完整的调用方法
 *
 * @author franco
 */
public interface HttpAdaptor {

    /**
     * 根据方法注解来初始化每一个参数的注解器
     * @param context 容器上下文
     * @param method 将适配的方法
     */
    void init(ServletContext context, Method method);

    /**
     * 根据请求获取其中的参数来适配方法
     * @param context 容器上下文
     * @param request 请求
     * @param response 响应
     * @return
     */
    Object[] adapt(ServletContext context, Request request, Response response);
}