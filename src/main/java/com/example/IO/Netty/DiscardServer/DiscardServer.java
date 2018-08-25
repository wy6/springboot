package com.example.IO.Netty.DiscardServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.jupiter.api.Test;

/**
 * @Description: Discards any incoming data.
 * @Author: WangY
 * @Date: Created in 2018-08-15 9:45
 * @Modified By：
 */

public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        // NioEventLoopGroup() 是用来处理I/O操作的多线程事件循环器，
        // IO 提供多种不同的 EventLoopGroup 的实现来处理不同传输协议。
        // 本例中实现一个服务端应用，需要两个 NioEventloopGroup 对象，
        // 一个用来接收进来的请求连接，被叫做“boss”，一个用来处理接收到的连接，被叫做“worker”。

        ServerBootstrap b = new ServerBootstrap();
        // ServerBootstrap 是一个启动 Nio 服务的辅助启动类，在此服务中可以直接使用 Channel，

        try {
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 指定使用 NioServerSocketChannel 类来使 channel 接收进来的连接

                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // childHandler 事件处理器被用来处理最近接收到的 channel，
                        // ChannelInitializer 是一个特殊的处理类，可以为使用者配置一个新的 Channel

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 设置指定的通道实现的配置参数，在TCP/IP协议的服务端，可以设置 socket 参数如tcpNoDelay、keepAlive
                    // iption() 是提供给 NioServerSocketChannel 用来接收进来的连接。

                    .childOption(ChannelOption.SO_KEEPALIVE, true);
                    // childOption 是提供给父通道 ServerChannel 接收到的连接。

            ChannelFuture f = b.bind(port).sync();
            // 绑定端口，启动服务

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Test
    public static void main(String[] args) throws InterruptedException {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new DiscardServer(port).run();
    }
}
