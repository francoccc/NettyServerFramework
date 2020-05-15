package com.franco.action;

import com.franco.ByteResult;
import com.franco.spring.annotation.Action;
import com.franco.spring.annotation.View;
import com.franco.spring.annotation.Views;
import com.franco.spring.core.Request;
import com.franco.spring.core.Session;
import com.franco.spring.view.ByteView;
import com.franco.spring.view.NoActionView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BaseAction
 *
 * @author franco
 */
@Action
@Views({
        @View(name = "byte", type = ByteView.class),
        @View(name = "noAction", type = NoActionView.class)
})
public abstract class BaseAction {

    private static final Logger log = LoggerFactory.getLogger(BaseAction.class);

    public ByteResult getResult(byte[] result, Request request) {
        Session session = request.getSession();
        session.access();
        return new ByteResult(result);
    }
}
