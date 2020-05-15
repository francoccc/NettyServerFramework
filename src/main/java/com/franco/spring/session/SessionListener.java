package com.franco.spring.session;

/**
 * Session的监听器
 *
 * @author franco
 */
public interface SessionListener {

    /**
     * Session的创建
     *
     * @param event
     */
    void onSessionCreate(SessionEvent event);

    /**
     * Session的摧毁
     *
     * @param event
     */
    void onSessionDestory(SessionEvent event);
}
