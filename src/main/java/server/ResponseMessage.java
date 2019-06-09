package server;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import javax.imageio.stream.FileImageOutputStream;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class ResponseMessage {
    private RequestMessage requestMessage;

    private String path = System.getProperty("user.dir"); //获取当前工作目录

    private Map<String,String> Check = new HashMap<String, String>();


    public ResponseMessage(RequestMessage requestMessage){
        this.requestMessage = requestMessage;
    }

    /**
     * 此方法通过对requestMessage处理之后选择正确的响应报文并返回
     * @return String
     */

    public String makeResponseMessage(){

        //先检查应该使用哪种响应，然后调用相应的响应之后得到报文
        try {
            File checkFile = new File(path + "/existFiles" + requestMessage.getUrl() + ".txt");
            if(checkFile.exists()){
                //将检查信息读入
                BufferedReader input = new BufferedReader(new FileReader(checkFile));
                String line;
                while((line = input.readLine()) != null){
                    int firstPosition = line.indexOf(":");
                    Check.put(line.substring(0, firstPosition), line.substring(firstPosition+1));
                }
                //如果客户端之前访问过（客户端有了缓存）并且服务端没有对客户端访问的东西进行修改
                if(requestMessage.getMessage().containsKey("If_Modified_Since") &&
                        Check.containsKey("Last_Modified") &&
                        compare(requestMessage.getMessage().get("If_Modified_Since"), Check.get("Last_Modified"))){
                    return Response304();
                } else if(Check.get("allow").contains(requestMessage.getRequsetMethod())){  //检查该文件是否允许客户端的请求方式

                    if(Check.get("redirect").equals("true")){  //检查该文件是否被重定向
                        if(Check.get("moved").equals("permanently")){ //检查该文件是暂时重定向还是永久重定向
                            return Response301();
                        }else return Response302();   //暂时重定向
                    }else return Response200();       //没有被重定向
                }else return Response405();    //不允许客户端的请求方式
            }else return Response404();        //没有找到目标文件
        } catch(Exception e){
            return Response500();             //服务端内部错误
        }
    }

    /**
     *
     * @param If_Modified_Since
     * @param Last_Modified
     * @return
     * return true 表示客户端之前访问并保存的东西服务端没有改变过
     * return false 表示客户端之前访问并保存的东西服务端改变过
     */
    public boolean compare(String If_Modified_Since, String Last_Modified){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date IfModifiedSince = simpleDateFormat.parse(If_Modified_Since);
            Date LastModified = simpleDateFormat.parse(Last_Modified);
            if(IfModifiedSince.getTime() >= LastModified.getTime())return true;
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String Response200(){

        StringBuilder response = new StringBuilder();
        StatusCode statusCode = StatusCode.SUCCESS;
        response.append(requestMessage.getHttpVersion()+" "+statusCode.code+" "+statusCode.description+"\n");
        response.append("Server:"+"Ubuntu18.04"+"\n");
        response.append("Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n");
        response.append("Content_Type:"+requestMessage.getRequestType()+"\n");

        if("POST".equals(requestMessage.getRequsetMethod())){
            try {
                //解密
                byte[] data = Base64.decode(requestMessage.getRequestEntity());
                //处理数据
                for (int i = 0; i < data.length; i++) {
                    if (data[i] < 0) data[i] += 256;
                }
                FileOutputStream fileOutputStream = new FileOutputStream(path + requestMessage.getUrl());
                fileOutputStream.write(data);
                fileOutputStream.flush();
                fileOutputStream.close();
            }catch (IOException e){
                e.printStackTrace();
                return Response500();
            }
        }


        try{
            //将文件中的数据（客户端请求的内容）读入
            FileInputStream fileInputStream = new FileInputStream(path + requestMessage.getUrl());
            byte[] data = new byte[fileInputStream.available()];
            int contentLength = fileInputStream.read(data);
            response.append("Content_Length:"+contentLength + "\n\n");  //响应报文头部和实体用"\n\n"分隔开

            if("image/png".equals(requestMessage.getRequestType())){
                response.append(Base64.encode(data));
            }else response.append(new String(data, 0, contentLength));
        } catch (IOException e){
            return Response500();
        }
        return response.toString();
    }
    public String Response301(){
        StringBuilder response = new StringBuilder();
        StatusCode statusCode = StatusCode.MOVED_PERMANENTLY;
        response.append(requestMessage.getHttpVersion()+" "+statusCode.code+" "+statusCode.description+"\n");
        response.append("Location:"+ Check.get("location") + "\n");
        response.append("Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n\n");
        return response.toString();
    }

    public String Response302(){
        StringBuilder response = new StringBuilder();
        StatusCode statusCode = StatusCode.MOVED_TEMPORARILY;
        response.append(requestMessage.getHttpVersion()+" "+statusCode.code+" "+statusCode.description+"\n");
        response.append("Location:"+Check.get("location")+"\n");
        response.append("Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n\n");
        return response.toString();
    }

    public String Response304(){
        StringBuilder response = new StringBuilder();
        StatusCode statusCode = StatusCode.NOT_MODIFIED;
        response.append(requestMessage.getHttpVersion() +" "+statusCode.code+" "+statusCode.description + "\n");
        response.append("Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n\n");
        return response.toString();
    }

    public String Response404(){
        StringBuilder response = new StringBuilder();
        StatusCode statusCode = StatusCode.NOT_FOUND;
        response.append(requestMessage.getHttpVersion() +" "+statusCode.code+" "+statusCode.description + "\n");
        response.append("Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n");


        try{
            FileInputStream fileInputStream = new FileInputStream(path+"/404.html");
            byte[] data = new byte[fileInputStream.available()];
            int contentLength = fileInputStream.read(data);
            response.append("Content_Type:text/html" + "\n");
            response.append("Content_Length:"+contentLength + "\n\n");  //响应报文头部和实体用"\n\n"分隔开
            response.append(new String(data, 0, contentLength));

        } catch(IOException e){
            e.printStackTrace();
        }

        return response.toString();
    }

    public String Response405(){
        StringBuilder response = new StringBuilder();
        StatusCode statusCode = StatusCode.METHOD_NOT_ALLOWED;
        response.append(requestMessage.getHttpVersion() +" "+statusCode.code+" "+statusCode.description + "\n");
        response.append("Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n");


        try{
            FileInputStream fileInputStream = new FileInputStream(path+"/405.html");
            byte[] data = new byte[fileInputStream.available()];
            int contentLength = fileInputStream.read(data);
            response.append("Content_Type:text/html" + "\n");
            response.append("Content_Length:"+contentLength + "\n\n");  //响应报文头部和实体用"\n\n"分隔开
            response.append(new String(data, 0, contentLength));

        } catch(IOException e){
            e.printStackTrace();
        }

        return response.toString();
    }

    public String Response500(){
        StringBuilder response = new StringBuilder();
        StatusCode statusCode = StatusCode.INTERNAL_SERVER_ERROR;
        response.append(requestMessage.getHttpVersion() +" "+statusCode.code+" "+statusCode.description + "\n");
        response.append("Date:"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"\n");


        try{
            FileInputStream fileInputStream = new FileInputStream(path+"/500.html");
            byte[] data = new byte[fileInputStream.available()];
            int contentLength = fileInputStream.read(data);
            response.append("Content_Type:text/html" + "\n");
            response.append("Content_Length:"+contentLength + "\n\n");  //响应报文头部和实体用"\n\n"分隔开
            response.append(new String(data, 0, contentLength));

        } catch(IOException e){
            e.printStackTrace();
        }

        return response.toString();
    }
}
