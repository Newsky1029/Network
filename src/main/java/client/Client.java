package client;

import GUI.ConnectWindow;
import GUI.RequestWindow;
import GUI.ResponseWindow;

import javax.swing.*;

public class Client {
    private String serverAddress;
    private int port;
    private Cache cache;
    private RequestWindow requestWindow;
    private ResponseWindow responseWindow;
    private ConnectWindow connectWindow;
    private Validator validator;
    private Connector connector;
    private Processor processor;
    private String[]request;

    public Client(){
        this("localhost",8080);
    }

    public Client(String serverAddress,int port){
        this.serverAddress=serverAddress;
        this.port=port;
        cache = new Cache();
    }


    public void go(){
        try {
            requestWindow = new RequestWindow(cache);
            requestWindow.init("请求报文");
            String headText = requestWindow.getHeadText().getText();
            requestWindow.getHeadText().setText(headText+"\n"+"Host:"+serverAddress);
            request = requestWindow.getRequest();
            validator = new Validator();
            if(!validator.isPassed(request)){
                //检查出错就刷新窗口重新来
                requestWindow.setVisible(false);
                return;
            }
            else{
                String[]requestLine = request[0].split(" ");
                String url = requestLine[1];
                responseWindow = new ResponseWindow();
                responseWindow.init("响应报文");
                String[]result = this.setResponseWindow();
                //处理响应报文
                processor = new Processor(url,cache);
                int status=processor.deal(result[0],result[1],result[2]);

                if(status==1){
                    //重定向情况
                    this.setRequestWindow();
                    result=this.setResponseWindow();
                    //需要再次处理报文（需要往cache里加东西），但不用再次检测重定向
                    processor.setURL(processor.getRedirectURL());
                    processor.deal(result[0],result[1],result[2]);
                }
                else if(status==2){
                    //缓存情况
                    responseWindow.getBodyText().setText("这是从缓存里取的:\n"+cache.getContent(url));
                }
                responseWindow.flush();
                requestWindow.setVisible(false);
                responseWindow.setVisible(false);
                return;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
            System.out.println("报文格式出错，请检查报文格式");
            requestWindow.setVisible(false);
            return ;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("未能与服务器端建立连接，请检查服务器地址和端口号");
            requestWindow.setVisible(false);
            return;
        }

    }

    private String[] setResponseWindow(){
        String[]result = new String[3];
        String text=connector.getResponseMessage(request[0]+"\n"+request[1]+"\n\n"+request[2]);
        String []response = text.split("\n\n");
        String []response_lh = response[0].split("\n");
        //设置请求行
        String line = response_lh[0];
        responseWindow.getLineText().setText(line);
        //设置请求头
        String head="";
        for(int i=1;i<response_lh.length;i++){
            head=head+response_lh[i];
            if(i!=response_lh.length-1)head+="\n";
        }
        responseWindow.getHeadText().setText(head);
        if(response.length==2){
            responseWindow.getBodyText().setText(response[1]);
            result[2] = response[1];
        }
        result[0] = line;
        result[1] = head;
        return result;
    }

    private void setRequestWindow(){
        String[]requestLine = request[0].split(" ");
        //需要更新请求行内的内容
        String redirectURL = processor.getRedirectURL();
        String newLine = requestLine[0]+" "+redirectURL+" "+requestLine[2];
        //自动触发
        JOptionPane.showMessageDialog(requestWindow.getjPanel(), "检测到重定向，点击刷新报文", "提示信息",JOptionPane.PLAIN_MESSAGE);
        requestWindow.getLineText().setText(newLine);
        requestWindow.getButton2().doClick();
        request = requestWindow.getRequest();
    }

    public static void main(String[] args){
        String serverAddress = "localhost";
        int port = 8080;

        ConnectWindow connectWindow = new ConnectWindow();
        String []result = connectWindow.getAddressAndPort();

        try{
            if(result!=null) {
                if(result[0]!=null)serverAddress = result[0];
                if(result[1]!=null)port = Integer.parseInt(result[1]);
            }
        }catch (Exception e){
            System.out.println("输入地址或端口号错误");
        }

        Client client = new Client(serverAddress,port);
        client.connector = new Connector(client.port,client.serverAddress);
        client.connector.connect();
        connectWindow.setVisible(false);
        while(true) {
            client.go();
        }
    }
}
