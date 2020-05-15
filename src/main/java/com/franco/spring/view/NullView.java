package com.franco.spring.view;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;

/**
 * NullView
 *
 * @author franco
 */
public class NullView extends DefaultView {

    public NullView() {

    }

    @Override
    public void preRender() {
        super.preRender();
    }

    @Override
    public void doRender(Result<?> result, Request request, Response response) {
        super.doRender(result, request, response);
    }
}
