package client;

import javafx.scene.chart.PieChart;

import java.net.*;
import java.io.*;
public class Connector {
    private int port;
    private String serverAddress;
    Socket client;
    public Connector(int port,String serverAddress){
        this.port=port;
        this.serverAddress=serverAddress;
    }
    public void connect(){
        try
        {
            //System.out.println("向主机地址：" + serverAddress + " 建立连接，端口号：" + port);
            client = new Socket(serverAddress, port);
            //System.out.println("远程主机地址：" + client.getRemoteSocketAddress());
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public String getResponseMessage(String request){
        try{
            OutputStream outputStream = client.getOutputStream();
            outputStream.write(request.getBytes());
            Thread.sleep(100);
            InputStream inputStream = client.getInputStream();
            byte inputData[] = new byte[inputStream.available()];   //准备一个缓存数组
            int lenInput = inputStream.read(inputData);
            String responseText = new String(inputData, 0, lenInput);  //将输入的字节数组转化为可操作的字符串
            return responseText;
        }catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }catch (IOException e){
            System.out.println("获取响应报文失败");
            return null;
        }
    }


}
