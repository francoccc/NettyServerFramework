package com.franco.util;

/**
 * 通用工具
 *
 * @author franco
 */
public class MyGeneralUtil {

    /**
     * 将32位int的数拆成4个8字节的数据类型
     * 按照低位数组中存储高位字节
     * @param a
     * @return
     */
    public static byte[] intToBytes(int a) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((a >> 24) & 0xff);
        bytes[1] = (byte) ((a >> 16) & 0xff);
        bytes[2] = (byte) ((a >> 8) & 0xff);
        bytes[3] = (byte) (a & 0xff);
        return bytes;
    }
}
