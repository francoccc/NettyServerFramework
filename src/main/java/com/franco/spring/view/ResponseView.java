package com.franco.spring.view;

import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;

/**
 * ResponseView
 *
 * @author franco
 */
public interface ResponseView {

    void render(Result<?> result, Request request, Response response);
}
