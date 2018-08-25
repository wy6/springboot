package com.example.IO.Netty.DiscardServer;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * @Description: DISCARD 服务(丢弃服务，指的是会忽略所有接收的数据的一种协议)
 * @Author: WangY
 * @Date: Created in 2018-08-15 9:02
 * @Modified By：
 */

public class DiscardServerHandler extends ChannelHandlerAdapter {
    // DiscardServerHandler 继承自 ChannelHandlerAdapter，
    // ChannelHandlerAdapter 类实现了 ChannelHandler 接口，
    // ChannelHandler 类提供了许多事件处理的接口方法。

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 重写 channelReade() 事件处理方法，该方法在服务端接收到数据时被调用

        ctx.write("return" + msg);
        // ChannelHandlerContext 对象提供了许多操作，write() 方法用于逐字将接收到的消息写入缓冲区
        // 且写入时Netty自动释放该消息。

        ctx.flush();
        // flush() 方法将缓冲区中的数据强行输出，或直接调用 writeAndFlush() 方法。

//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) {
//                System.out.println("DiscardServerHandler.channelRead.25:" + (char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg);
//        }

//        ((ByteBuf) msg).release();
        // 本例中，接收的消息是 ByteBuf 类型，该类型是一个引用计数对象，
        // 该对象必须显式调用 release() 方法来释放
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // ExcelptionCaught() 事件处理方法是当出现Throwable对象时会被调用，
        // 即当Netty出现IO错误或处理事件抛出异常时。
        // 多数情况下，捕获的异常应该被记录下来，并把关联的 channel 关闭。

        cause.printStackTrace();
        ctx.close();
    }
}
