package com.franco.spring.session;

import com.franco.spring.core.Session;

/**
 * 会话属性事件
 *
 * @author franco
 */
public class SessionAttributeEvent extends SessionEvent {

    public String key;

    public Object object;

    public SessionAttributeEvent(Session session, String key, Object object) {
        super(session);
        this.key = key;
        this.object = object;
    }
}
