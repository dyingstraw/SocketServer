# socket服务器代码

# 设计内容
工厂模式 + 反射  + socket + 线程池 


## Main 
测试代码
## SocketSever 
服务器监听代码
## SocketThreadImpl 
每一个连接会建立一个这样的线程，并放入线程池
## ThreadPool 
提供了在运行线程列表的线程池

## ConfigFactory
配置工程，可配置连接线程，线程池

## Util
工具类，解包、封包

todo：数据库存储

````
建立socket服务器的时候，先配置ConfigFactory
// 配置连接线程
ConfigFactory.setSocketThreadName("com.jike.socketServer.SocketThreadImpl");
````