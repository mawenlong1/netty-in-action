package nia.chapter4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author mawenlong
 * @date 2018/09/19
 * describe:未使用 Netty 的异步网络编程
 */
public class PlainNioServer {
    public void serve(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        ServerSocket socket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        //将服务器绑定到选定的端口
        socket.bind(address);
        //打开Selector来处理Channel
        Selector selector = Selector.open();
        //将serverSocketChannel注册到打开Selector来处理Channel以接受连接
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi\r\n".getBytes());
        while (true) {
            try {
                //等待需要处理的新事件；阻塞将一直持续到下一个传入事件
                selector.select();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            //获取所有接受事件的SelectionKey实例
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    //检查事件是否是一个新的已经就绪可以被接受的连接
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        //接受客户端，并将它注册到选择器
                        client.register(selector, SelectionKey.OP_WRITE |
                                SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accept connection from " + client);
                    }
                    //检查套接字是否已经准备好写数据
                    if (key.isWritable()) {
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {
                            //将数据写入已连接的客户端
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                    }
                } catch (IOException e) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException e1) {

                    }
                }
            }
        }
    }
}