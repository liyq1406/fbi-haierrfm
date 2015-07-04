package rfm.ta.gateway.hfnb.server;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import rfm.ta.gateway.hfnb.codec.LFixedLengthMsgDecoder;
import rfm.ta.gateway.hfnb.codec.LFixedLengthMsgEncoder;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created by XIANGYANG on 2015-7-1.
 * 本地服务端,接收网银（hfnb）报文
 */

public class LocalServerBootstrap {

    private int port;

    public LocalServerBootstrap(int port) {
        this.port = port;
    }

    public void start() {
        ChannelFactory factory = new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
        ServerBootstrap bootstrap = new ServerBootstrap(factory);
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder", new LFixedLengthMsgDecoder());
                pipeline.addLast("encoder", new LFixedLengthMsgEncoder());
                pipeline.addLast("serverChannelHandler", new ServerChannelHandler());
                return pipeline;
            }
        });
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(port));
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
