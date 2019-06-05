package client;

import java.util.regex.*;
public class Validator {
    private String errorMessage="";
    private boolean pass=false;
    public boolean isPassed(String[]request){
        try{
            String []requestLine = request[0].split(" ");
            String []requestHead = request[1].split("\n");

            //先检查请求行，请求方法由服务器端检查

            String url = requestLine[1];
            String protocol = requestLine[2];
            if(requestLine==null||requestLine.length!=3){
                errorMessage="请求行格式错误";
                return false;
            }
            if(!(url.endsWith(".txt")||url.endsWith(".html")||url.endsWith(".png"))){
                errorMessage="请求行内容错误，不支持的请求文件";
                return false;
            }
            if(!protocol.equals("HTTP/1.1")){
                errorMessage="不支持的请求协议，目前仅支持HTTP/1.1";
                return false;
            }

            //再检查请求头
            if(requestHead==null||requestHead.length==0) {
                errorMessage="请求头不能为空";
                return false;
            }
            String pattern = "(.+):(.+)";
            Pattern r = Pattern.compile(pattern);
            Matcher m;
            for(String s:requestHead){
                m = r.matcher(s);
                if(!m.find()){
                    errorMessage="请求头格式错误，请用键值对形式输入";
                    return false;
                }
                else{
                    if(m.group(1).equals("Host")){
                        if(m.group(2).equals("localhost"))continue;
                        String host = m.group(2);
                        Pattern r1 = Pattern.compile("(\\d[1-3]):(\\d[1-3]):(\\d[1-3]):(\\d[1-3])");
                        Matcher m1 = r1.matcher(host);
                        if(!m1.find()){
                            errorMessage="域名地址错误，请输入正确的域名";
                            return false;
                        }
                        int address[] = new int[4];
                        for(int i=0;i<4;i++){
                            address[i] = Integer.parseInt(m1.group(i));
                            if(address[i]<0||address[i]>255){
                                errorMessage="域名地址错误，请输入正确的域名";
                                return false;
                            }
                        }
                    }
                }
            }

            //请求体不做检查
            return true;
        }catch (ArrayIndexOutOfBoundsException e){
            errorMessage="请求报文格式错误";
            return false;
        }catch (Exception e){
            errorMessage="操作错误";
            return false;
        }
    }

    public String getMessage(){
        return errorMessage;
    }
}
