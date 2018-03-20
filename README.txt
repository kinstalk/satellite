
# Satellite 接口稳定性监测平台

## 平台简介

Satellite主要定位于线上系统的接口稳定性监测，保持线上接口服务稳定。采用 manager + 多 agent 架构, 支持多项目, 多点监测; 发现接口异常实时邮件报警。


## 内置功能

* #### 权限管理  
    * 用户管理  
    * 权限管理(比较简单, 限制到页面级, 页面元素不做控制)  
    * 角色管理  

* #### 健康展示  
    * 当前状态：展示模块/接口实时健康曲线图,支持定时刷新  
    * 一周状态：按天展示最近一周接口健康状况  
    
* #### 服务监控
    * jmeter脚本上传及维护  
    * 定时任务管理  
    * 运行状态查询  
    * 监控端(Agent)管理  
    * 测试资源管理(手机号段)
    

## 为何选择Satellite

*  使用 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 协议，源代码完全开源，无商业限制。
*  使用目前主流的Java EE开发框架，简单易学，学习成本低。
*  基于 jmeter 测试框架, 界面化编辑测试脚本。可以完全复用原有 jmeter 测试脚本。


## 技术选型

* 核心框架：Spring Framework 4.1
* 通信框架：Netty 5.0.0.Alpha2
* 测试框架：Jmeter 2.2+
* 前端框架：Velocity 1.4


## 快速体验

* ####环境安装
    * 具备运行环境：JDK1.6+、Maven3.0+、MySql5+、Jmeter2.2+。
    * 导入 db/satellite.sql 表及数据
    
* #### 修改配置
    *  修改 pom.xml 文件中的对应环境的数据库参数(priest.jdbc.*)。
    *  把 script/runjmeter 文件放入 pom.xml 配置的文件夹中(jmeter.run.path)
    *  修改 runjmeter 中的变量配置, jmxpath 是 jmeter 生成的测试脚本存放路径, jmeterpath 是 jmeter 可执行文件路径
    
* #### 运行Manager

    *  运行 `cd PROJECT_HOME && mvn clean install && cd priest-satellite-manager && mvn jetty:run`
    *  管理后台 URL ： http://localhost:9000
    *  管理员登陆账号，用户名：admin 密码：admin
    
* #### 运行Agent
    *  IDE 环境，运行 priest-satellite-agent/src/test/java/com.satellite.agent/AgentDubboStartup.java main 函数
    *  Linux 环境, 执行`mvn clean install` 编译代码后, 解压 priest-satellite-agent/target/priest-satellite-agent-assembly.tar.gz , 执行 `sh bin/start.sh`

## 常见问题

1. jmeter默认内存512M, 如提示内存溢出，请调整JVM参数, 文件位置： JMETER_HOME/bin/jmeter, 找到相应内容后修改如下：
    HEAP="-Xms128m -Xmx128m"
    PERM="-XX:PermSize=10m -XX:MaxPermSize=30m"
    
2. 运行前请编译项目 `mvn clean install`

## 如何交流、反馈、参与贡献？

* QQ Group：701059309 &nbsp; 苹果版QQ若不能加入请使用手机QQ最新版
* GitHub：<https://github.com/digitzhang/Satellite>

一个人的个人能力再强，也无法战胜一个团队，希望兄弟姐妹的支持，能够贡献出自己的部分代码，参与进来共同完善它(^_^)。

怎么共享我的代码：[手把手教你如何加入到github的开源世界！](http://www.cnblogs.com/wenber/p/3630921.html)

## 版权声明

本软件使用 [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0) 协议，请严格遵照协议内容：

1. 需要给代码的用户一份Apache Licence。
2. 如果你修改了代码，需要在被修改的文件中说明。
3. **在延伸的代码中（修改和有源代码衍生的代码中）需要带有原来代码中的协议，商标，专利声明和其他原来作者规定需要包含的说明。**
4. 如果再发布的产品中包含一个Notice文件，则在Notice文件中需要带有Apache Licence。你可以在Notice中增加自己的许可，但不可以表现为对Apache Licence构成更改。
5. Apache Licence也是对商业应用友好的许可。使用者也可以在需要的时候修改代码来满足需要并作为开源或商业产品发布/销售
6. 你可以二次包装出售，**但还请保留文件中的版权和作者信息**，并在你的产品说明中注明Satellite。
7. 你可以以任何方式获得，你可以修改包名或类名，**但还请保留文件中的版权和作者信息**。
