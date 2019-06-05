package server;

import java.util.Map;

public class RequestMessage {

    private int port;
    private String requsetMethod;
    private String url;
    private String httpVersion;
    //请求报文头信息
    private Map<String, String> message;
    private String requestEntity;
    private String requestType;

    public RequestMessage(int port, String requestMethod, String url, String httpVersion, Map<String, String> message, String requestEntity){
        this.port = port;
        this.requsetMethod = requestMethod;
        this.url = url;
        this.httpVersion = httpVersion;
        this.message = message;
        this.requestEntity = requestEntity;

        //设置需要返回的类型
        requsetTypeSet();

    }

    public void requsetTypeSet(){
        if(url.endsWith(".html")){
            requestType = "text/html";
        }else if(url.endsWith(".png")){
            requestType = "image/png";
        }else if(url.endsWith(".txt")){
            requestType = "text/txt";
        }
    }

    public int getPort(){return port; }

    public String getRequsetMethod(){return requsetMethod; }

    public String getUrl() { return url; }

    public String getHttpVersion(){return httpVersion; }

    public Map<String, String> getMessage(){return message; }

    public String getRequestType(){return requestType; }

    public String getRequestEntity(){return requestEntity; }

}
