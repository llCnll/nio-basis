package cn.chennan.nio2;

import org.junit.Test;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author ChenNan
 * @date 2019-11-04 下午4:50
 **/
public class TestBlockIO2 {

    @Test
    public void client() throws Exception{

        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9999));
        FileChannel inChannel = FileChannel.open(Paths.get("/home/chennan02/demo.png"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while(inChannel.read(buf) != -1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }
        System.out.println("发送数据完毕");

        sChannel.shutdownOutput();

        int len = 0;
        while((len = sChannel.read(buf)) != -1){
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }

        inChannel.close();
        sChannel.close();

    }

    @Test
    public void server() throws Exception{

        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.bind(new InetSocketAddress(9999));

        FileChannel outChannel = FileChannel.open(Paths.get("/home/chennan02/demo2.png"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        SocketChannel sChannel = ssChannel.accept();

        ByteBuffer buf = ByteBuffer.allocate(1024);

        while(sChannel.read(buf) != -1){
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
        System.out.println("接收数据完毕");
        //往回写数据
        buf.clear();
        String str = "服务端收到数据!";
        buf.put(str.getBytes());
        buf.flip();
        sChannel.write(buf);

        sChannel.close();
        outChannel.close();
        ssChannel.close();

    }

}
