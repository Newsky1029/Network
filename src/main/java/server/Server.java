package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private static int port = 8080;

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);

        while(true){
            //得到客户端
            Socket client = serverSocket.accept();

            System.out.println("新客户端连接"+client.getInetAddress()+":"+client.getPort());

            //为客户端构建异步线程
            ClientHandler clientHandler = new ClientHandler(client, port);
            clientHandler.start();
        }
    }
}
