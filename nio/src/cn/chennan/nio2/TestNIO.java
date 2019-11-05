package cn.chennan.nio2;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * @author ChenNan
 * @date 2019-11-04 下午4:31
 **/
public class TestNIO {
    @Test
    public void client() throws Exception{
        //1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        //FileChannel inChannel = FileChannel.open(Paths.get("/home/chennan02/demo.png"), StandardOpenOption.READ);

        // 切换非阻塞模式
        sChannel.configureBlocking(false);

        //2. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //3. 读取本地文件, 并发送给服务端
        //while(inChannel.read(buf) != -1){
           // buf.flip();
            buf.put(LocalDateTime.now().toString().getBytes());
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        //}

        //4. 关闭通道
        //inChannel.close();
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        //1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //FileChannel outChannel = FileChannel.open(Paths.get("/home/chennan02/demo2.png"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        //切换成非阻塞模式
        ssChannel.configureBlocking(false);

        //2. 绑定连接
        ssChannel.bind(new InetSocketAddress(9999));

        //获取选择器
        Selector selector = Selector.open();

        //将通道注册选择器上 指定接受事件
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);

        //轮询式的获取选择器上已经"准备就绪"的事件
        while(selector.select() > 0){
            // 获取当前选择其中所有的注册的 选择键 (已就绪的坚挺事件
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while(it.hasNext()){
                SelectionKey sk = it.next();

                // 判断具体是什么事件
                if(sk.isAcceptable()){
                    //3. 获取客户端连接的通道
                    SocketChannel sChannel = ssChannel.accept();

                    //切换非阻塞模式
                    sChannel.configureBlocking(false);

                    // 将通道注册到选择器上
                    sChannel.register(selector,SelectionKey.OP_READ);
                }else if(sk.isReadable()){
                    //获取当前选择器上 度就绪状态的通道
                    SocketChannel sChannel = (SocketChannel)sk.channel();
                    //读取数据

                    //4. 分配指定大小的缓冲区
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    //5. 接受客户端发送来的数据, 并保存本地
                    int len = 0;
                    while((len = sChannel.read(buf)) > 0){
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                    //System.out.println("接受完毕!");
                }
                //取消选择键
                it.remove();
                //System.out.println("取消了");
            }
        }
    }
}
