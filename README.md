# chat-chat
一个基于Java语言开发的简单聊天室

分为两个模块：客户端[chat-chat-client]、服务端[chat-chat-server]

通过Socket进行通信



## 技术栈

客户端：原生Swing

服务端：SpringBoot、MySQL、MyBatis-plus、Druid、Lombok、Jbcrypt、Log4J



## 项目结构

### 客户端

![1689146759279](README.assets/1689146759279.png)

### 服务端

![1689146740126](README.assets/1689146740126.png)



## 项目展示

注意：这里的关闭服务暂时无法使用[没有去设置]

![1689146841970](README.assets/1689146841970.png)

![1689146858254](README.assets/1689146858254.png)

![1689146896541](README.assets/1689146896541.png)

![1689146912685](README.assets/1689146912685.png)

![1689146952588](README.assets/1689146952588.png)

注意：由于当时时间比较赶，这里的好友管理和聊天室管理中的管理申请存在BUG，只能在对方申请添加后，打开管理申请显示一次，后续申请添加的无法显示

![1689146967378](README.assets/1689146967378.png)

![1689147051818](README.assets/1689147051818.png)

![1689147057925](README.assets/1689147057925.png)

![1689147060662](README.assets/1689147060662.png)

![1689147182110](README.assets/1689147182110.png)


## 启动
导入chat-chat.sql数据库文件后，进入chat-chat-server/src/main/resources/application.yaml修改数据库配置，直接运行两个模块即可。

## 注意事项

除了好友管理和聊天室管理中的管理申请存在BUG以外，其它功能均无问题。由于制作该项目的时间不超过一个星期，有很多地方没有考虑到为，所以会存在一些BUG和代码不规范的问题。后续若有空，则再来进行修补。
