package cn.chennan.nio2;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author ChenNan
 * @date 2019-10-30 下午7:21
 *
 * NIO完成通信的三个核心
 * 1. 通道(Channel): 负责连接
 *
 * 2. 缓冲区(Buffer): 负责数据的存储
 *      java.nio.channels.Channel 接口
 *          |-- SelectableChannel
 *              |-- SocketChannel
 *              |-- ServerSocketChannel
 *              |-- DatagramChannel
 *
 *              |--Pipe.SinkChannel
 *              |--Pipe.SourceChannel
 *
 * 3. 选择器(Selector): 是SelectableChannel的多路复用器, 用于监控SelectableChannel的IO状况
 *
 **/
public class TestBlockIO {

    @Test
    public void client() throws IOException{
        //1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        FileChannel inChannel = FileChannel.open(Paths.get("/home/chennan02/demo.png"), StandardOpenOption.READ);
        //2. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //3. 读取本地文件, 并发送给服务端
        while(inChannel.read(buf) != -1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        //4. 关闭通道
        inChannel.close();
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        //1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        FileChannel outChannel = FileChannel.open(Paths.get("/home/chennan02/demo2.png"),  StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        //2. 绑定连接
        ssChannel.bind(new InetSocketAddress(9999));
        //3. 获取客户端连接的通道
        SocketChannel sChannel = ssChannel.accept();
        //4. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //5. 接受客户端发送来的数据, 并保存本地
        while(sChannel.read(buf) != -1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
        //6. 关闭通道
        sChannel.close();
        outChannel.close();
        ssChannel.close();
    }

}
