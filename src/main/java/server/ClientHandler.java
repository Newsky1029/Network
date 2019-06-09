package server; /**
 * 客户端消息处理的类
 * 当服务端监听到一个客户端的请求之后,利用这个类处理请求
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler extends Thread {
    private Socket client;
    private int port;

    public ClientHandler(Socket socket, int port){
        this.client = socket;
        this.port = port;
    }

    @Override
    public void run(){
        super.run();
            try {
                //从客户端获取输入流和输出流
                InputStream inputStream = client.getInputStream();
                OutputStream outputStream = client.getOutputStream();

                //实现长连接
                while (!client.isClosed()) {
                    if (inputStream.available() > 0) {
                        int lenInput = -1;
                        String request = null;
                        Thread.sleep(2000);
                        while (lenInput <= 0) {
                            byte inputData[] = new byte[inputStream.available()];   //准备一个缓存数组
                            lenInput = inputStream.read(inputData);
                            request = new String(inputData, 0, lenInput);  //将输入的字节数组转化为可操作的字符串
                        }

                        if (request != null) {
                            //解析请求报文
                            RequestMessage requestMessage = analysisResquestMessage(request);

                            /**
                             * 根据请求报文得到响应报文
                             */
                            ResponseMessage responseMessage = new ResponseMessage(requestMessage);
                            String response = responseMessage.makeResponseMessage();
                            System.out.println(response);
                            outputStream.write(response.getBytes()); //将响应报文内容写入
                            outputStream.flush();
                        }
                    }
                }
                    //关闭客户端和服务端的流
                inputStream.close();
                outputStream.close();
                client.close();
            } catch(IOException e){
                e.printStackTrace();
            } catch(InterruptedException e){
                e.printStackTrace();
            }
    }

    public RequestMessage analysisResquestMessage(String request){

        System.out.println(request);
        //先将报文头与实体（可有可无）分开,约定用两个换行符（"\n\n"）作为分割符
        String[] requestHead = request.split("\n\n")[0].split("\n|\r");
        String requestEntity = request.split("\n\n").length > 1 ? request.split("\n\n")[1] : null;

        String[] firstLine = requestHead[0].split(" ");

        String requestMethod = firstLine[0], url = firstLine[1], httpVersion = firstLine[2];

        Map<String, String> message = new HashMap<String, String>();
        for(int i = 1; i < requestHead.length; i ++){
            int splitPosition = requestHead[i].indexOf(":");
            if(splitPosition == -1)break;
            message.put(requestHead[i].substring(0, splitPosition), requestHead[i].substring(splitPosition+1));
        }
        return new RequestMessage(port, requestMethod, url, httpVersion, message, requestEntity);
    }

}