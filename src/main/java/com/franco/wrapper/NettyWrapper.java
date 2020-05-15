package com.franco.wrapper;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * NettyWrapper
 * 使用Unpooled去申请Buffer空间
 *
 * @author franco
 */
public class NettyWrapper implements Wrapper {

    @Override
    public Object wrapper(String command, int requestId, byte[] body) {
        ByteBuf heapBuffer = Unpooled.buffer(8);
        JSONObject json = new JSONObject();
        json.put("requestId", requestId);
        json.put("command", command);
        json.put("data", JSONObject.parse(new String(body)));
        heapBuffer.writeBytes(json.toString().getBytes());
        return heapBuffer;
    }
}
