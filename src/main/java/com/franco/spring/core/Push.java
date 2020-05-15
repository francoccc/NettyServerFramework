package com.franco.spring.core;

import com.franco.common.ServerProtocol;

/**
 * 推送
 *
 * @author franco
 */
public interface Push {

    void push(Session session, String command, byte[] body);

    void push(Session session, Object buffer);

    void push(String command, byte[] body);

    boolean isPushable();

    /**
     * 清理Push通道
     */
    void clear();

    void discard();

    void heartbeat();

    ServerProtocol getPushProtocol();
}
