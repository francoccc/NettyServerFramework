package com.franco.spring.view;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;

/**
 *
 *
 */
public class NoActionView extends DefaultView {

    @Override
    public void preRender() {
        super.preRender();
    }

    @Override
    public void doRender(Result<?> result, Request request, Response response) {
        super.doRender(result, request, response);
    }
}
