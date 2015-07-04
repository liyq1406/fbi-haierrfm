package rfm.ta.gateway.hfnb.codec;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by XIANGYANG on 2015-7-1.
 * ±¨ÎÄ½âÂë
 */

public class LFixedLengthMsgDecoder extends FrameDecoder {
    public static final int LENGTH = 12;
    private static Logger logger = LoggerFactory.getLogger(LFixedLengthMsgDecoder.class);


    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() < LENGTH) {
            return null;
        }
        byte[] msgBytes = new byte[buffer.readableBytes()];
        buffer.readBytes(msgBytes);
        return msgBytes;
    }

}
