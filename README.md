要求：基于Java Socket API搭建简单的HTTP客户端和服务器端程序

说明： 

1. 不允许基于netty等框架，完全基于Java Socket API进行编写

2. 不分区使用的IO模型，BIO、NIO和AIO都可以

3. 实现基础的HTTP请求、响应功能，具体要求如下：

​    3.1 HTTP客户端可以发送请求报文、呈现响应报文（命令行和GUI都可以）

​    3.2 HTTP客户端对301、302、304的状态码做相应的处理

​    3.3 HTTP服务器端支持GET和POST请求

​    3.4 HTTP服务器端支持200、301、302、304、404、405、500的状态码

​    3.5 HTTP服务器端实现长连接

​    3.6 MIME至少支持三种类型，包含一种非文本类型



参考资料：

1. https://docs.oracle.com/javase/tutorial/networking/sockets/index.html

2. http://www.runoob.com/java/java-networking.html

3. https://tools.ietf.org/html/rfc2616


请求报文的设计：

命令格式：
GET /0606/01.php HTTP/1.1  请求行
host:localhost 请求头信息
(空行：标志头信息结束，主体信息开始)
主体信息

POST /0606/02.php HTTP/1.1
Host:localhost
Content-Length:23
Content-Type:application/x-www-form-urlencoded

主体信息：username = zhangsan&age=23


Post请求必须标明Content-Length和Content-Type

请求：

1. 请求行

（1）请求方法(本次作业只有Get或者Post)
（2）请求的路径
（3）所用的协议（在本次作业中限定为HTTP1.1，只有这种协议，就不用专门设计信息来存）



2. 请求头信息(key:value)
Host:必有
Content-type:Post有
Content-length:Post有




3. 请求主体信息(可选)





响应：





1. 响应行



（1）协议版本

（2）状态码

（3）状态文字：对状态码的解释





2. 响应头信息(key:value)



（1）Date:具体时间

（2）Server:服务器的名字

（3）Content-Length:内容长度

（4）Content-Type:内容的类型



(空一行来区分头信息和主体)



3. 响应主体信息

​	请求的内容



状态码详解：http://tomcat.apache.org/tomcat-5.5-doc/servletapi/constant-values.html#javax.servlet.http.HttpServletResponse.SC_NOT_MODIFIED
服务端参考：https://www.cnblogs.com/xzmblog/p/4505237.html