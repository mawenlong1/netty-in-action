package nia.chapter4;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author mawenlong
 * @date 2018/09/19
 * describe:未使用netty的阻塞网络编程
 */
public class PlainOioServer {
    public void serve(int port) throws IOException {
        //将服务器绑定到指定端口
        final ServerSocket socket = new ServerSocket(port);
        try {
            while (true) {
                //接受连接
                final Socket clientSocket = socket.accept();
                System.out.println("Accept connection from :" + clientSocket);
                //创建一个新的线程来处理该连接
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try{
                            out = clientSocket.getOutputStream();
                            //将消息写给已连接的客户端
                            out.write("Hi!\r\n".getBytes());
                            out.flush();
                            clientSocket.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }finally {
                            try{
                                clientSocket.close();
                            }catch (IOException e){

                            }
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
