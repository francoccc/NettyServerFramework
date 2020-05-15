package com.franco.spring.session;

import com.franco.common.Tuple;
import com.franco.spring.core.Push;
import com.franco.spring.servlet.ServletConfig;
import com.franco.spring.core.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 应用内的Session
 *
 * @author franco
 */
public class StandardSession implements Session {

    private enum Type {
        CREATE,
        DESTORY,
        ADD,
        REPLACE,
        REMOVE
    }

    /** SessionId */
    private String sessionId;

    private Map<String, Object> map = new ConcurrentHashMap<String, Object>();

    /** 是否丢弃 */
    private volatile boolean discard;

    private volatile boolean isValid;

    private volatile boolean expire;

    private long createTime;

    private volatile long lastAccessTime;

    private volatile Push push;

    private List<SessionListener> sessionListeners;

    private List<SessionAttributeListener> sessionAttributeListeners;

    private ServletConfig sc;

    private long sessionTimeOutMillis;

    private long sessionEmptyTimeOutMillis;

    private long sessionInvalidateMillis;

    private List<Object> historyMsg = null;
    private ReentrantLock lock  = new ReentrantLock();

    /**
     * Default Constructor
     */
    public StandardSession() {

    }

    public StandardSession(String sessionId,
                           List<SessionListener> sessionListeners,
                           List<SessionAttributeListener> sessionAttributeListeners,
                           ServletConfig sc) {
        this.sessionId = sessionId;
        this.sessionListeners = sessionListeners;
        this.sessionAttributeListeners = sessionAttributeListeners;
        this.sc = sc;
        this.sessionListeners = sessionListeners;
        this.sessionAttributeListeners = sessionAttributeListeners;

        this.sessionTimeOutMillis = sc.getSessionTimeOutMillis();
        this.sessionEmptyTimeOutMillis = sc.getSessionEmptyTimeOutMillis();
        this.sessionInvalidateMillis = sc.getSessionInvalidateMillis();

        this.createTime = System.currentTimeMillis();
        this.lastAccessTime = createTime;

        // 创建Session 需通知SessionListener
        notifyListener(sessionListeners, Type.CREATE);

    }

    public String getId() {
        return sessionId;
    }

    public void setAttribute(String key, Object value) {

        notifyListener(sessionAttributeListeners,
                new SessionAttributeEvent(this, key, value),
                map.containsKey(key) ? Type.REPLACE : Type.ADD);

        map.put(key, value);
    }

    public Object getAttribute(String key) {
        return map.get(key);
    }

    public boolean removeAttribute(String key) {
        Object value = map.remove(key);
        notifyListener(sessionAttributeListeners,
                new SessionAttributeEvent(this, key, value), Type.REMOVE);
        return map.remove(key) != null;
    }

    public void invalidate() {
        if(discard) {
            discard();
            return;
        }

        if(null != push) {
            push.clear();
        }

        if(map.size() == 0 || !isValid) {
            // remove from SessionManager
            SessionManager.getInstance().sessionMap.remove(this.getId());
        }
    }

    private void discard() {
        historyMsg.clear();

        notifyListener(sessionListeners, Type.DESTORY);

        if(null != push) {
            push.clear();
        }

        if(null != historyMsg) {
            historyMsg.clear();
            historyMsg = null;
        }

        map.clear();
        map = null;
        // remove from SessionMananger
        SessionManager.getInstance().sessionMap.remove(this.getId());
    }

    public void destory() {
        map.clear();
        map = null;
        historyMsg.clear();
        historyMsg = null;
    }

    public void markDiscard() {
        this.discard = true;
    }

    public void setValid(boolean valid) {
        this.isValid = valid;
    }

    public void access() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    /**
     * push is active
     * @return
     */
    public boolean isActive() {
        return getPush() != null && getPush().isPushable();
    }

    public boolean isValid() {
        if(expire) {
            return false;
        }
        if(discard) {
            return false;
        }
        if(map.isEmpty()
                && (System.currentTimeMillis() - lastAccessTime) < sessionEmptyTimeOutMillis) {
            return true;
        }
        else if((System.currentTimeMillis() - lastAccessTime) < sessionTimeOutMillis) {
            return true;
        } else {
            expire = true;
        }
        return false;
    }

    public boolean isInvalidate() {
        if(!expire) {
            return false;
        }
        return System.currentTimeMillis() - lastAccessTime > sessionInvalidateMillis;
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean isExpire() {
        return expire;
    }

    public void reActive() {

    }

    public void expire() {
        invalidate();
    }

    public synchronized void setPush(Push push) {
        if (null != push) {
            push.clear();
        }
        this.push = push;
    }

    public Push getPush() {
        return push;
    }

    public void push(String command, byte[] body) {
        if(push.isPushable()) {
            push.push(command, body);
            saveMsgToList(command, body);
        }
    }

    public void push(Object buffer) {
        if(push.isPushable()) {
            push.push(this, buffer);
            saveMsgToList(buffer);
        }
    }

    private void saveMsgToList(String command, byte[] body) {
        addMsgToList(new Tuple<String, Object>(command, body));
    }

    private void saveMsgToList(Object object) {
        addMsgToList(new Tuple<String, Object>(null, object));
    }

    private void addMsgToList(Object object) {
        lock.lock();
        try {
            if(null == historyMsg) {
                historyMsg = new ArrayList<Object>();
            }
            historyMsg.add(object);
        } finally {
            lock.unlock();
        }
    }

    public void notifyListener(List<SessionListener> sessionListeners,
                               Type type) {
        for(SessionListener sessionListener : sessionListeners) {
            switch (type) {
                case CREATE:
                    sessionListener.onSessionCreate(new SessionEvent(this));
                case DESTORY:
                    sessionListener.onSessionDestory(new SessionEvent(this));
            }
        }
    }

    public void notifyListener(List<SessionAttributeListener> sessionAttributeListeners,
                               SessionAttributeEvent event,
                               Type type) {
        for(SessionAttributeListener sessionAttributeListener : sessionAttributeListeners) {
            switch (type) {
                case ADD:
                    sessionAttributeListener.onSessionAttributeAdd(event);
                case REPLACE:
                    sessionAttributeListener.onSessionAttributeReplace(event);
                case REMOVE:
                    sessionAttributeListener.onSessionAttributeRemove(event);
            }
        }
    }
}
