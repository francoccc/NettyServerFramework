package com.franco.netty.decoder;

import com.franco.netty.message.RequestMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 类型信息解码器
 *
 * @author franco
 */
public class TcpMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        int requestId = byteBuf.readInt();
        int commandLen = byteBuf.readInt();
        byte[] commandBytes = new byte[commandLen];
        byteBuf.readBytes(commandBytes);
        String command = new String(commandBytes, StandardCharsets.UTF_8);
        byte[] content = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(content);
        RequestMessage msg = new RequestMessage(command, requestId, content);
        out.add(msg);
    }
}
