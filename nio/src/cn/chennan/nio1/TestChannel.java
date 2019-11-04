package cn.chennan.nio1;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author ChenNan
 * @date 2019-10-30 下午4:53
 *
 * 通道 channel 用于源节点于目标节点的连接 在JavaNio 中负责缓冲区中数据的传输, channel本身不存储数据. 因此需要配合缓冲区进行传输
 *
 * 通道的主要实现类
 * java.nio.channels接口
 *      FileChannel
 *      SocketChannel
 *      ServerSocketChannel
 *      DatagramChannel
 *
 * 获取通道
 *  1.Java针对支持通道的类提供getChannel()方法
 *      本地IO
 *      FIleInputStream/FileOutputStream
 *      RandomAccessFile
 *
 *      网络IO
 *      Socket
 *      ServerSocket
 *      DatagramSoket
 *  2. JDK1.7 中的nio2 针对各个通道提供静态方法open()
 *  3. JDK1.7 中的nio2 的FIles工具类的newByteChannel()
 *
 * 通道间的数据传输
 *  transferFrom()
 *  transferTo
 *
 * 分散(Scatter)与聚集(Gather)
 *  分散读取(Scantter reads): 将通道中的数据分散到多个缓冲区去
 *  聚集写入(Gather writes): 将多个缓冲区中的数据 聚集到通道中
 *
 * 字符集 Charset
 *  编码 字符串 --> 字节数组
 *  解码 字节数组 --> 字符串
 **/
public class TestChannel {

    //5. 字符集
    @Test
    public void code(){
        Charset gbk = Charset.forName("GBK");
        //解码器
        CharsetDecoder decoder = gbk.newDecoder();
        // 编码器
        CharsetEncoder encoder = gbk.newEncoder();

        CharBuffer buf = CharBuffer.allocate(1024);
        buf.put("chennan");

        buf.flip();

        ByteBuffer byteBuffer = gbk.encode(buf);

        CharBuffer decode = gbk.decode(byteBuffer);

        System.out.println(decode.toString());

    }

    @Test
    public void charset(){
        SortedMap<String, Charset> map = Charset.availableCharsets();
        for(Map.Entry<String, Charset> entry : map.entrySet()){
            System.out.println(entry.getKey() + "==" + entry.getValue());
        }
    }

    //4. 分散和聚集
    @Test
    public void copyByChannel4() throws Exception{
        RandomAccessFile raf1 = new RandomAccessFile("/home/chennan02/tokens.txt", "rw");

        // 获取通道
        FileChannel channel = raf1.getChannel();
        // 分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(1024);
        ByteBuffer buf2 = ByteBuffer.allocate(2048);

        // 分散读取
        ByteBuffer[] bufs = {buf1, buf2};
        channel.read(bufs);
        // 聚集写入
        for (ByteBuffer buf : bufs) {
           buf.flip();
        }
        RandomAccessFile raf2 = new RandomAccessFile("/home/chennan02/token2.txt", "rw");
        FileChannel channel1 = raf2.getChannel();

        channel1.write(bufs);
    }

    //3. 通达之间的数据传输(直接缓冲区)
    @Test
    public void copyByChannel3() throws Exception{

        FileChannel inChannel = FileChannel.open(Paths.get("/home/chennan02/dubbo-file.zip"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("/home/chennan02/dubbo-file2.zip"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);

        inChannel.transferTo(0, inChannel.size(), outChannel);

        outChannel.close();
        inChannel.close();
    }

    //2. 使用直接缓冲区完成文件的复制(内存映射文件)
    @Test
    public void copyByChannel2() throws Exception{
        FileChannel inChannel = FileChannel.open(Paths.get("/home/chennan02/dubbo-file.zip"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("/home/chennan02/dubbo-file2.zip"), StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE_NEW);
        //内存映射文件
        MappedByteBuffer inMappeBuf = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMappeBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());

        //直接对缓冲区进行数据的读写
        byte[] dst = new byte[inMappeBuf.limit()];
        inMappeBuf.get(dst);
        outMappeBuf.put(dst);

        outChannel.close();
        inChannel.close();


    }

    //1. 利用通道完成文件的复制 非直接缓冲区
    @Test
    public void copyByChannel() throws Exception{
        FileInputStream fis = new FileInputStream("/home/chennan02/demo.png");
        FileOutputStream fos = new FileOutputStream("/home/chennan02/demo2.png");
        
        // 获取通道
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        //分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //将通道中的数据写入缓冲区
        while(inChannel.read(buf) != -1){
            //将缓冲区的数据写入通道
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        //关闭通道. 关闭文件
        outChannel.close();
        inChannel.close();
        fos.close();
        fis.close();
    }

}
