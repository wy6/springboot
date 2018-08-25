package com.example.IO.Netty.ServerClient;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018-08-17 13:44
 * @Modified By：
 */

public class NettyServer {

    public static void main(String[] args) {

        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        // 创建两个线程组，boosGroup 用于监听端口创建连接，
        // workGroup 用于处理每条连接的数据读写

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 创建引导类

        // 启动 Netty 服务端，需指定三类属性：
        // 1.线程模型
        // 2.IO模型
        // 3.连续读写处理逻辑
        serverBootstrap.group(boosGroup, workerGroup)
                // 为引导类配置两个线程组
                .channel(NioServerSocketChannel.class)
                // 指定服务端IO模型为NIO模型
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    // 调用childHandler() 方法给引导类创建 ChannelInitializer 通道初始化类
                    // childHandler() 用于指定处理新连接数据的读写处理逻辑
                    // handler() 方法用于指定在服务端启动过程中的一些逻辑

                    protected void initChannel(NioSocketChannel channel) {
                    }
                });

//        serverBootstrap.bind(8000);
        // 绑定监听端口

        bind(serverBootstrap, 8000);
        // 定义自动绑定递增端口

    }

    private static void bind(final ServerBootstrap sbs, final int port){
        sbs.bind(port).addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("NettyServer.operationComplete.57:" + "端口【" + port + "】绑定成功！");
                } else {
                    System.out.println("NettyServer.operationComplete.59:" + "端口【" + port + "】绑定失败！");
                    bind(sbs, port + 1);
                }
            }
        });
    }
}
