package com.example.IO.Nio;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Description:
 * @Author: WangY
 * @Date: Created in 2018-08-10 15:18
 * @Modified By：
 */

public class Nio {

    // Channel和传统IO中的Stream很相似
    // 主要区别为：通道是双向的，通过一个Channel既可以进行读，也可以进行写；而Stream只能进行单向操作，通过一个Stream只能进行读或者写
    // 常用几种通道：FileChannel、SocketChanel、ServerSocketChannel、DatagramChannel
    //     通过使用FileChannel可以从文件读或者向文件写入数据；通过SocketChannel，以TCP来向网络连接的两端读写数据；
    //     通过ServerSocketChanel能够监听客户端发起的TCP连接，并为每个TCP连接创建一个新的SocketChannel来进行数据读写；
    //     通过DatagramChannel，以UDP协议来向网络连接的两端读写数据。

    /**
     * FileChannel 文件读写通道测试
     *
     * @throws Exception
     */
    @Test
    public void FileChannelTest() throws Exception {
        // 定义文件名及输出内容
        String fileName = "outFile.text";
        String str = "This is a Test text.";
        // 获取文件
        File file = new File(fileName);
        // 获取文件输出流
        FileOutputStream outputStream = new FileOutputStream(file);
        // 获取输出通道
        FileChannel channel = outputStream.getChannel();
        // 创建缓冲区用于存放需要放入通道内的字节
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 将定义的字符串放入缓冲区
        buffer.put(str.getBytes());
        // 调用flip()方法切换到可读状态（将limit置position位置，将postion置开始位置）
        buffer.flip();
        // 将输出通道中的数据写入到缓冲区
        channel.write(buffer);
        // 关闭通道
        channel.close();
        // 关闭输出流
        outputStream.close();
    }
}
