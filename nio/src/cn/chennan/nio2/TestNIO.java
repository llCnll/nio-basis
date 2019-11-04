package cn.chennan.nio2;

import org.junit.Test;

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
public class TestNIO {

    @Test
    public void server(){
        //1. 获取通道

        //2. 分配指定大小的缓冲区

        //3. 读取本地文件, 并发送给服务端

        //4. 关闭通道
    }

    @Test
    public void client(){
        //1. 获取通道

        //2. 绑定连接

        //3. 获取客户端连接的通道

        //4. 分配指定大小的缓冲区

        //5. 接受客户端发送来的数据, 并保存本地

        //6. 关闭通道

    }

}
