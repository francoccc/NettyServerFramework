package com.franco.wrapper;

/**
 * 包装器工具类
 *
 * @author franco
 */
public class WrapperUtil {

    /** 应用使用的全局包装器 */
    private static Wrapper wrapper;

    public static void setWrapper(Wrapper newWrapper) {
        if(wrapper != null) {
            throw new RuntimeException("wrapper already set");
        }
        wrapper = newWrapper;
    }

    public static Object wrapper(int requestId, String command, byte[] body) {
        return wrapper.wrapper(command, requestId, body);
    }
}
