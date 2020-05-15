package com.franco.spring.session;

import com.franco.spring.core.Session;

/**
 * 会话事件
 *
 * @author franco
 */
public class SessionEvent {

    /** session */
    public Session session;

    public SessionEvent(Session session) {
        this.session = session;
    }
}
