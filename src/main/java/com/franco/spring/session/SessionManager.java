package com.franco.spring.session;

import com.franco.spring.core.Push;
import com.franco.spring.servlet.ServletConfig;
import com.franco.spring.core.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Session管理器
 * Session的生成：
 * Session的监视和管理： scheduledThreadPoolExecutor按时调度监视
 *
 * @author franco
 */
public class SessionManager {

    protected Random random;

    /** 熵 */
    protected String entropy;

    protected volatile MessageDigest digest;

    private static final Logger log = LoggerFactory.getLogger(SessionManager.class);

    private static final SessionManager instance = new SessionManager();

    /** 重复的Session个数 */
    protected int duplicates = 0;

    private List<SessionListener> sessionListeners = new ArrayList<SessionListener>();

    private List<SessionAttributeListener> sessionAttributeListeners = new ArrayList<SessionAttributeListener>();

    public final Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    private boolean checkThreadStarted;

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    private ServletConfig sc;

    public static int HISTORY_MSG_LEN = 100;

    private SessionManager() { }

    public static SessionManager getInstance() {
        return instance;
    }

    public void startCheckThread() {
        if(checkThreadStarted) {
            return;
        }

        synchronized (this) {
            if(!checkThreadStarted) {
                checkThreadStarted = true;

                scheduledThreadPoolExecutor =  new ScheduledThreadPoolExecutor(1);
                scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {

                    public void run() {
                        for(Map.Entry<String, Session> entry
                                : sessionMap.entrySet()) {
                            Session session = entry.getValue();
                            try {
                                if(session.isInvalidate()) {
                                    session.destory();
                                } else if(!session.isExpire() && !session.isValid()) {
                                    log.debug("SessionManager invalidate session : {}", session.getId());
                                    session.invalidate();
                                } else if(session.isValid()) {
                                    Push push = session.getPush();
                                    if(push != null) {
                                        push.heartbeat();
                                    }
                                }
                            } catch (Throwable e) {
                                log.error("SessionManager#checkSession fail", e);
                                session.invalidate();
                            }
                        }
                    }
                }, sc.getSessionTickTime(), sc.getSessionTickTime(), TimeUnit.SECONDS);
            }
            log.info("SessionManager - startCheckThread : sessionTickTime {}", sc.getSessionTickTime());
        }
    }

    public void access(String sessionId) {
        Session session = getSession(sessionId);
        if(session != null) {
            session.access();
        }
    }

    public Session getSession(String sessionId) {
        return getSession(sessionId, false);
    }

    public Session getSession(String sessionId, boolean allowCreate) {
        if(allowCreate) {
            Session session = _getSession(sessionId);
            if (session == null) {
                if(!checkThreadStarted) {
                    throw new RuntimeException("CheckThread not start");
                }
                session = new StandardSession(generateSessionId(), sessionListeners, sessionAttributeListeners, sc);
                sessionMap.put(session.getId(), session);
                sessionId = session.getId();
            }
        }
        return _getSession(sessionId);
    }

    private Session _getSession(String sessionId) {
        return sessionId == null? null : sessionMap.get(sessionId);
    }

    public String generateSessionId() {
        byte[] random = new byte[16];
        int resultByteLen = 0;
        String result = null;

        do {
            StringBuffer builder = new StringBuffer();
            if(result != null) {
                duplicates++;
            }
            while(resultByteLen < 16) {
                getRandomBytes(random);
                random = getMessageDigest().digest(random);
                for (int i = 0; i < random.length && resultByteLen < 16; i++) {
                    byte b1 = (byte) ((random[i] & 0xf0) >> 4);
                    byte b2 = (byte) (random[i] & 0x0f);
                    if(b1 < 10) {
                        builder.append((char) ('0' + b1));
                    } else {
                        builder.append((char) ('A' + (b1 - 10)));
                    }
                    if(b2 < 10) {
                        builder.append((char) ('0' + b2));
                    } else {
                        builder.append((char) ('A' + (b2 - 10)));
                    }
                    resultByteLen++;
                }
            }
            result = builder.toString();
        } while (sessionMap.containsKey(result));
        return result;
    }

    private void getRandomBytes(byte[] bytes) {
        if(random == null) {
            // random初始对于种子进行一次乱序
            long seed = System.currentTimeMillis();
            char[] entropy = getEntropy().toCharArray();
            for(int i = 0; i < entropy.length; i++) {
                long update = ((byte) entropy[i]) << (i % 8 * 8);
                seed ^= update;
            }
            random = new Random();
            random.setSeed(seed);
        }
        this.random.nextBytes(bytes);
    }

    private MessageDigest getMessageDigest() {
        if(digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
                return digest;
            } catch (NoSuchAlgorithmException e) {

            }
        }
        return digest;
    }

    private String getEntropy() {
        if(entropy == null) {
            entropy = this.toString();
        }
        return entropy;
    }

    public void setServletConfig(ServletConfig sc) {
        this.sc = sc;

        HISTORY_MSG_LEN = (Integer) sc.getInitParameter(ServletConfig.HISTORY_MESSAGE_LEN);
    }

    public void addSessionAttributeListener(SessionAttributeListener sessionAttributeListener) {
        this.sessionAttributeListeners.add(sessionAttributeListener);
    }

    public void addSessionListener(SessionListener sessionListener) {
        this.sessionListeners.add(sessionListener);
    }
}
