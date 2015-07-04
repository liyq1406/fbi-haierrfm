package rfm.ta.gateway.hfnb.codec;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by XIANGYANG on 2015-7-1.
 * ±¨ÎÄ±àÂë
 */

public class LFixedLengthMsgEncoder extends OneToOneEncoder {
    private static Logger logger = LoggerFactory.getLogger(LFixedLengthMsgEncoder.class);

    @Override
    protected Object encode(ChannelHandlerContext channelHandlerContext, Channel channel, Object o) throws Exception {
        byte[] rtnBytes = (byte[])o;
        return ChannelBuffers.wrappedBuffer(rtnBytes);
    }
}
