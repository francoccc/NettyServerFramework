package com.franco.wrapper;

/**
 * 应用数据包装方法接口
 *
 * @author franco
 */
public interface Wrapper {

    Object wrapper(String command, int requestId, byte[] content);
}
