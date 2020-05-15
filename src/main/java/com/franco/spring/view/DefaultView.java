package com.franco.spring.view;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;
import com.franco.wrapper.WrapperUtil;

/**
 * DefaultView
 *
 * @author franco
 */
public abstract class DefaultView implements ResponseView {

    public void render(Result<?> result, Request request, Response response) {
        preRender();
        doRender(result, request, response);
    }

    public void preRender() { }

    public void doRender(Result<?> result, Request request, Response response) { }

    public Object convert(Request request, byte[] body) {
        return WrapperUtil.wrapper(request.getRequestId(), request.getCommand(), body);
    }
}
