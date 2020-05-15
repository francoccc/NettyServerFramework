package com.franco.common;

import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 常量
 *
 * @author franco
 */
public class NettyConstants {

    public static Logger NETTY_LOG = LoggerFactory.getLogger("netty framework -");

    public static AttributeKey<String> SESSON_ID = AttributeKey.newInstance("sessionId");
}
