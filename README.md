# nio-基础
NIO完成通信的三个核心
1. 通道(Channel): 负责连接
                                                                      
2. 缓冲区(Buffer): 负责数据的存储

     java.nio.channels.Channel 接口
     
         |-- SelectableChannel
             |-- SocketChannel
             |-- ServerSocketChannel
             |-- DatagramChannel
                                                                      
             |--Pipe.SinkChannel
             |--Pipe.SourceChannel
                                                                      
3. 选择器(Selector): 是SelectableChannel的多路复用器, 用于监控SelectableChannel的IO状况
