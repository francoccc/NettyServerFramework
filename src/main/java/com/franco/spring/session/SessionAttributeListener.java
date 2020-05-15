package com.franco.spring.session;

/**
 * Session属性的监听器
 *
 * @author franco
 */
public interface SessionAttributeListener {

    void onSessionAttributeAdd(SessionAttributeEvent event);

    void onSessionAttributeReplace(SessionAttributeEvent event);

    void onSessionAttributeRemove(SessionAttributeEvent event);
}
