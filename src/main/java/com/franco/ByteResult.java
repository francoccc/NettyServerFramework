package com.franco;

import com.franco.spring.core.Result;

/**
 * ByteResult
 *
 * @author franco
 */
public class ByteResult implements Result<byte[]> {

    private byte[] bytes;

    public ByteResult(byte[] result) {
        this.bytes = result;
    }

    public String getViewName() {
        return "byte";
    }

    public byte[] getResult() {
        return bytes;
    }

    public int getBytesLength() {
        return bytes.length;
    }
}
