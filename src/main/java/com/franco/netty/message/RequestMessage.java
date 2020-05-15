package com.franco.netty.message;

import com.franco.util.MyGeneralUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 请求信息
 *
 * @author franco
 */
public class RequestMessage {

    /** 协议 */
    private String command;
    /** sessionId */
    private String sessionId;
    /** 请求id */
    private int requestId;
    /** 文本内容 */
    private byte[] content;
    /** 全局的value值 */
    private Object globalKeyValue;
    /** 创建时间  */
    private long createTime;

    public RequestMessage() {
        this.createTime = System.currentTimeMillis();
    }

    public RequestMessage(String command, int requestId, byte[] content) {
        this();
        this.command = command;
        this.requestId = requestId;
        this.content = content;
    }

    /**
     * 调用此方法来获取RequestMessage的字节流
     *
     * @return 包装后的字节流
     */
    public byte[] toByte() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] commandBytes = command.getBytes();
        try {
            baos.write(MyGeneralUtil.intToBytes(requestId));
            baos.write(MyGeneralUtil.intToBytes(commandBytes.length));
            baos.write(commandBytes);
            baos.write(content);
        } catch (IOException e) {

        }
        return baos.toByteArray();
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public Object getGlobalKeyValue() {
        return globalKeyValue;
    }

    public void setGlobalKeyValue(Object globalKeyValue) {
        this.globalKeyValue = globalKeyValue;
    }

    public long getCreateTime() {
        return createTime;
    }
}
