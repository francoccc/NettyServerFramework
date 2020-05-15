package com.franco.exception;

/**
 * 服务器配置异常
 *
 * @author franco
 */
public class ServletConfigException extends RuntimeException {

    public ServletConfigException(String message) {
        super(message);
    }

    public ServletConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
