package cn.chennan.nio2;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Scanner;

/**
 * DataGramChannel
 * @author ChenNan
 * @date 2019-11-05 下午1:45
 **/
public class TestNIO2 {

    @Test
    public void send() throws Exception{
        DatagramChannel dc = DatagramChannel.open();

        //Scanner sc = new Scanner(System.in);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        dc.configureBlocking(false);
        int i = 0;
        while(i < 10){
            String str = ""+i++;
            buf.put((LocalDateTime.now().toString()+"\n"+str).getBytes());
            buf.flip();
            dc.send(buf, new InetSocketAddress("127.0.0.1", 9999));
            buf.clear();
        }

    }

    @Test
    public void receive() throws Exception{
        DatagramChannel dc = DatagramChannel.open();

        dc.bind(new InetSocketAddress(9999));

        dc.configureBlocking(false);

        Selector selector = Selector.open();

        dc.register(selector, SelectionKey.OP_READ);

        while(selector.select() > 0){
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while(it.hasNext()){
                SelectionKey sk = it.next();
                if(sk.isReadable()){
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    dc.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                }
            }
            it.remove();
        }
    }

}
