package com.franco.spring.core;

import java.util.Collection;
import java.util.Map;

/**
 * Cookies信息
 *
 * @author franco
 */
public interface Cookies {

    /**
     * 根据Name来获取cookie的实际值
     *
     * @param name
     * @return cookieValue
     */
    String getCookieValue(String name);

    /**
     * 获取一组Cookie
     * @return
     */
    Collection<?> getRemoteCookies();

    Map<String, Object> getCookies();

    void addCookie(Object cookie);
}
