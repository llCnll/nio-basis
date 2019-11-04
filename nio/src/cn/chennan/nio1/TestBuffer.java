package cn.chennan.nio1;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * @author ChenNan
 * @date 2019-10-30 下午2:59
 *
 * 缓冲区 buffer, 在Java Nio 中负责数据的存取 缓冲区就是数组, 用于存储不同数据类型的数据
 * 根据数据类型的不同 提供响应的类型的缓冲区(初boolean)
 * ByteBuffer CharBuffer IntBuffer LongBuffer FloatBuffer DoubleBuffer
 *
 * 上述缓冲区的管理方式几乎一直. 通过 allocate() 获取缓冲区
 *
 * put 存入
 *
 * get 取出
 *
 * 缓冲区四个核心属性
 *      private int mark = -1; 表示记录当前position的位置, 可以通过reset()恢复到mark的位置
 *     private int position = 0;缓冲区正在操作数据的位置 0 <= mark <= postition <= limit <= capacity
 *     private int limit;     缓冲区可以操作数据的大小 limit后面的数据不能进行读写
 *     private int capacity; 缓冲区最大存储的容量 一旦声明不能改变
 *
 * 直接缓冲区与非直接缓冲区
 *      非直接缓冲区 通过allocate方法分配缓冲区, 犟缓冲区建立在JVM的内存中
 *      直接缓冲区 通过allocateDirect方法分配缓冲区, 将缓冲区建立在物理内存中 可以提高效率
 **/
public class TestBuffer {
    @Test
    public void mark(){
        String str = "abcde";
        //1. 分配置顶的大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        buf.put(str.getBytes());

        buf.flip();

        System.out.println(buf.position());
        byte[] dst = new byte[1024];
        buf.get(dst, 0, 2);
        System.out.println(new String(dst, 0, 2));
        System.out.println(buf.position());
        buf.mark();
        buf.get(dst, 0, 2);
        System.out.println(new String(dst, 0, 2));
        System.out.println(buf.position());

        buf.reset();
        System.out.println(buf.position());

        if(buf.remaining() >0 ){
            System.out.println(buf.remaining());
        }

    }
    @Test
    public void test1(){
        String str = "abcde";
        //1. 分配置顶的大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println("-----allocate-----------");
        System.out.println(buf.position());//0
        System.out.println(buf.limit());//1024
        System.out.println(buf.capacity());//1024

        System.out.println("-----put-----------");
        //put存入数据
        buf.put(str.getBytes());
        System.out.println(buf.position());//5
        System.out.println(buf.limit());//1024
        System.out.println(buf.capacity());//1024

        //切换度模式. flip()
        buf.flip();
        System.out.println("------flip----------");
        System.out.println(buf.position());//0
        System.out.println(buf.limit());//5
        System.out.println(buf.capacity());//1024

        //读取缓冲区数据
        byte[] dst = new byte[buf.limit()];
        buf.get(dst);
        System.out.println(new String(dst, 0, dst.length));
        System.out.println("------get----------");
        System.out.println(buf.position());//5
        System.out.println(buf.limit());//5
        System.out.println(buf.capacity());//1024

        //rewind 可重复读
        buf.rewind();
        System.out.println("------rewind----------");
        System.out.println(buf.position());//0
        System.out.println(buf.limit());//1024
        System.out.println(buf.capacity());//1024

        //clear 但是数据还存在, 只是重置指针位置
        buf.clear();
        System.out.println("------clear----------");
        System.out.println(buf.position());//0
        System.out.println(buf.limit());//5
        System.out.println(buf.capacity());//1024
    }
}
