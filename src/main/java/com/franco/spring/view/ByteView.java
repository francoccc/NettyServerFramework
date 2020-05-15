package com.franco.spring.view;

import com.franco.common.ServerProtocol;
import com.franco.spring.core.Request;
import com.franco.spring.core.Response;
import com.franco.spring.core.Result;
import com.franco.exception.ViewNotMatchException;

/**
 * ByteView
 *
 * @author franco
 */
public class ByteView extends DefaultView {

    @Override
    public void preRender() {
        super.preRender();
    }

    @Override
    public void doRender(Result<?> result, Request request, Response response) {
        if(!"byte".equals(result.getViewName())) {
            throw new ViewNotMatchException("result not match response view");
        }
        if(ServerProtocol.TCP.equals(request.getServerProtocol())) {
            response.write(convert(request, (byte[]) result.getResult()));
        }
    }
}
