# Socket网络编程实验报告

*  组长：罗民胜&ensp; (171250670)
*  组员：  
&ensp;&ensp;&ensp;&ensp;&ensp;安&ensp;&ensp;皓&ensp;(171250648)   
&ensp;&ensp;&ensp;&ensp;&ensp;谢岷轩&ensp;(171250669)  
&ensp;&ensp;&ensp;&ensp;&ensp;杨&ensp;&ensp;洋&ensp;(171250671)  
&ensp;&ensp;&ensp;&ensp;&ensp;李晔华&ensp;(171250672)  


### 1 实验目的、实验原理和内容

#### 1.1实验目的：
基于Java Socket API搭建简单的HTTP客户端和服务器端程序
- 1 不允许基于netty等框架，完全基于Java Socket API进行编写

- 2 不分区使用的IO模型，BIO、NIO和AIO都可以

-  3 实现基础的HTTP请求、响应功能，具体要求如下：
>3.1 HTTP客户端可以发送请求报文、呈现响应报文（命令行和GUI都可以）
3.2 HTTP客户端对301、302、304的状态码做相应的处
3.3 HTTP服务器端支持GET和POST请求
3.4 HTTP服务器端支持200、301、302、304、404、405、500的状态码
3.5 HTTP服务器端实现长连接
​3.6 MIME至少支持三种类型，包含一种非文本类型

#### 1.2 实验原理和内容
- TCP（Transmission Control Protocol 传输控制协议）是一种面向连接的、可靠的、基于字节流的传输层通信协议，由IETF的RFC 793定义。实验基于TCP协议的三次握手通信模式，利用Socket网络编程技术实现客服端向服务端请求报文，服务端响应报文。
<img src="https://img-blog.csdn.net/20180615162943210?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L20wXzM4MDY4MjI5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70" width="89%">
- 实验涉及到的状态码说明：

|状态码|含义|
|:-------------:|--------------------|
|200("OK")|请求已成功，请求所希望的响应头或数据体将随此响应返回。|
|301("Moved Permanently")|被请求的资源已永久移动到新位置，并且将来任何对此资源的引用都应该使用本响应返回的若干个 URI 之一。|
|302("Moved Temporarily")|  请求的资源现在临时从不同的 URI 响应请求。|
|304("Not Modified")|如果客户端发送了一个带条件的 GET 请求且该请求已被允许，而文档的内容（自上次访问以来或者根据请求的条件）并没有改变，则服务器应当返回这个状态码。|
|404("Not Found")|请求失败，请求所希望得到的资源未被在服务器上发现。|
|405("Methed Not Allowed")|请求行中指定的请求方法不能被用于请求相应的资源。|
|500("Internal Server Error")|服务器遇到了一个未曾预料的状况，导致了它无法完成对请求的处理。一般来说，这个问题都会在服务器的程序码出错时出现。|

- 实验的MINE支持三种类型，分别时txt, html以及png，其中png为非文本格式

### 2 实验过程
实验实现的客户端与服务端分为两个程序，服务端启动后，一直处于监听状态，等待客户端的的连接，当有客户端连接上以后构建该客户端的异步线程，并且连接过程始终处于长连接状态。

#### 2.1 客户端与服务端建立连接

* 服务端的代码实现：
```
public class Server {  
    
    private static int port = 8080; //设置监听端口号  
    
    public static void main(String[] args) throws IOException {  
        
        ServerSocket serverSocket = new ServerSocket(port);  
        
        while(true){   //使用永真循环实现服务端持续监听 
            //得到客户端  
            Socket client = serverSocket.accept();  
            
            //控制台输出表示有客户端连接
            System.out.println("新客户端连接"+client.getInetAddress()+":"+client.getPort());  
            
            //为客户端构建异步线程  
            ClientHandler clientHandler = new ClientHandler(client, port);  
            clientHandler.start();  
        }  
    }  
}
```
* 客户端通过使用下面的代码语句与服务端建立连接，然后进行报文的传输，服务端对客户端的的请求报文作出响应并发送相应报文给客户端就是这个实验的关键流程。
```
client.connector = new Connector(port,serverAddress);  
client.connector.connect();
```
#### 2.2 客户端向服务端发送请求报文
- 请求报文格式：
以下为标准的请求报文格式，由于实验要求并没有需要全部使用，所以实际应用时只是写了部分
```
// 请求报文
GET http://www.jianshu.com/u/d97a1dec9e2d   HTTP/1.1
// 请求首部字段
Host: www.jianshu.com
Proxy-Connection: keep-alive
Accept: application/json
Chrome/57.0.2979.2 Safari/537.36
Referer: http://www.jianshu.com/u/d97a1dec9e2d
Accept-Encoding: gzip, deflate, sdch
Accept-Language: zh-CN,zh;q=0.8
Cookie: ...

// 内容实体
name=binjiang&age=100
```
如上述实例，客户端发送这样格式的请求报文给服务端，服务端提供的请求方式有GET和POST

#### 2.3 服务端响应请求报文
- 响应报文格式：
```
HTTP/1.1    200    OK
// 响应首部字段
Date: Mon, 13 Mar 2017 04:36:45 GMT
Content-Length: 362
Content-Type: text/html

// 主体
<html>
...
```
服务端得到客户端的请求报文之后进行解析，然后发送上述格式的报文给客户端。



### 3 结果演示
#### 3.1 项目功能
##### 3.1.1 客户端功能实现

1. 利用Socket和服务器端建立通信
2. 利用javaGUI展示请求报文窗口和响应报文窗口
3. 检查请求报文的报文格式是否正确，关于报文格式的说明参见2.2请求报文格式
4. 利用Socket发送请求报文、接收响应报文，在窗口中展示
5. 对301、302、304的状态码做相应处理，详情参见4.1.3功能演示，关于状态码说明参见1.2实验原理和内容

##### 3.1.2 服务器端功能实现

1. 利用Socket和客户端建立通信
2. 利用Socket接收响应报文，发送请求报文，关于响应报文格式参见2.3响应报文格式
3. 与客户端建立长连接
4. 利用多线程实现与多个客户端建立连接
5. 支持200、301、302、304、404、405、500的状态码，关于状态码说明参见1.2实验原理和内容

##### 3.1.3 功能演示

​	基本流程如图。

[![基本流程.png](https://i.loli.net/2019/06/07/5cf9ed0b7447438999.png)](https://i.loli.net/2019/06/07/5cf9ed0b7447438999.png)

​	真实流程实例，以状态码为301为例。

 1. 用户输入服务器端IP地址和端口号(不输入时默认为localhost:8080)，点击确认时客户端向服务器端建立连接，点击取消则程序停止，参见下图

    <img src="https://i.loli.net/2019/06/07/5cf9f04802c0011144.png" alt="Screenshot 2019-06-07 at 12.56.11.png" title="Screenshot 2019-06-07 at 12.56.11.png" width="90%"/>

 2. 连接成功后，呈现响应报文窗口，用户输入，输入完成后可以选择检查报文，报文格式正确时提示报文检查通过，否则提示报文检查失败，参见下图

 <center class="half">
    <img src="https://i.loli.net/2019/06/07/5cf9f0482912630934.png" alt="Screenshot 2019-06-07 at 12.59.44.png" title="Screenshot 2019-06-07 at 12.59.44.png"  height="50%" width="40%"/>
    <img src="https://i.loli.net/2019/06/07/5cf9f501bfa7710689.png" alt="Screenshot 2019-06-07 at 12.59.54.png" title="Screenshot 2019-06-07 at 12.59.54.png" height="50%" width="40%"/>
</center>



3. 点击发送报文，系统呈现响应窗口，当呈现的状态码是301时，请求报文窗口检测到重定向，点击ok，参见下图

<center class="half">
    <img src="https://i.loli.net/2019/06/07/5cf9f0483d4c051131.png" alt="Screenshot 2019-06-07 at 13.00.06.png" title="Screenshot 2019-06-07 at 13.00.06.png"  height="50%" width="40%"/>
    <img src="https://i.loli.net/2019/06/07/5cf9f6607adae79763.png" alt="Screenshot 2019-06-07 at 13.00.12.png" title="Screenshot 2019-06-07 at 13.00.12.png" height="50%" width="40%" />
</center>
4. 检测到重定向后，请求报文更新URL，并再次向服务器端发出请求，服务器端响应200，流程结束，下次发送报文时点击响应报文窗口的刷新即可

<center class="half">
    <img src="https://i.loli.net/2019/06/07/5cf9f0484588590956.png" alt="Screenshot 2019-06-07 at 13.00.22.png" title="Screenshot 2019-06-07 at 13.00.22.png" height="50%" width="40%"/>
    <img src="https://i.loli.net/2019/06/07/5cf9f0484df8960709.png" alt="Screenshot 2019-06-07 at 13.00.30.png" title="Screenshot 2019-06-07 at 13.00.30.png" height="50%" width="40%"/>
</center>

### 4 实验总结

​	通过完成实验，学习了简单的socket编程，了解到一些基础的客户端与服务端网络传输用到的协议，更重要的是对于平时我们都会遇到的部分状态码有了更深刻地理解，不在是表面上含义的理解，而是在原理层次上的理解。当然，这次实验也遇到了一些困难，其中最麻烦的就是传输MINE类型为图片处理。

​	实验要求实现三种MINE类型的报文传输，我们选择的时html/txt/png，其中html和txt格式的文件最容易传输，在实验中两者区别并不大，如果时相应报文的话，直接将String类型的文本转换为byte,然后塞到输出流中即可，客户端读取可以直接从输入流之中得到；当然，png格式也是这样，但是直接将png格式转为byte,然后通过输出流输出，最后客服端读入，其实读入的是一堆乱码，没法转换为正常png格式的图片。通过学习，了解到一种类似公钥和私钥的的处理办法；在服务端首先将图片读入到Byte类型的data中，然后Base64.encode(data)进行加密，就可以直接传输了；到达客户端以后，客服端再使用Base64.decode(data)进行解密，之后输出为图片就可以了，这样就确保传输过后图片可以正常得到。

### 5 组员分工
- 服务端实现：罗民胜
- 客户端实现：安皓
- 实验报告书写：谢岷轩、杨洋、李晔华