package com.franco.exception;

/**
 * 视图不匹配异常
 *
 * @author franco
 */
public class ViewNotMatchException extends RuntimeException {

    public ViewNotMatchException(String message) {
        super(message);
    }

    public ViewNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
