package com.franco.spring.core;

/**
 * Session
 *
 * @author franco
 */
public interface Session {

    String getId();

    void setAttribute(String key, Object value);

    Object getAttribute(String key);

    boolean removeAttribute(String key);

    void invalidate();

    void destory();

    void markDiscard();

    void setValid(boolean valid);

    void access();

    boolean isActive();

    boolean isValid();

    boolean isExpire();

    boolean isInvalidate();

    boolean isEmpty();

    void reActive();

    void expire();

    void setPush(Push push);

    Push getPush();

    void push(String command, byte[] body);

    void push(Object buffer);
}
