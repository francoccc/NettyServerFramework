package com.franco.spring.adaptor.injector;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.servlet.ServletContext;

/**
 * <br>参数注入器</br>
 * <br>{@code AbstractAdaptor.adapt()}调用{@code ParamInjector.get()}获取参数来进行注入操作</br>
 * <br></br><br><b>主要是以下两个注入器完成对于{@code Action}的参数注入</b></br>
 * <br> {@link NameInjector} 其实现命名绑定参数的注入器 </br>
 * <br> {@link ArrayInjector} 其实现数组类型的参数注入器 </br>
 * @author franco
 */
public interface ParamInjector {

    /**
     * 获取参数
     * @param context
     * @param request
     * @param response
     * @return
     */
    Object get(ServletContext context, Request request, Response response);
}
