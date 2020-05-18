**框架采用spring cloud搭建,集成了CAS统一认证，包含：eureka注册中心，zuul网关，feign服务调用，ribbon内部负载均衡。配置文件暂时是在各个服务里面，后期可以考虑spring cloud config搭建全局配置中心。**

**说明：**
common:框架公共包，包含一些公共的工具、异常捕获、返回数据格式等
eureka-server：注册中心，可搭建集群。分别使用peer1配置、peer2配置启动两个服务就是一个小集群。
server-cas（定义了一个/hello接口）、server-web（/hello、/hi两个接口，/hello是调用的server-cas的/hello接口）：两个微服务示例。都可以修改端口启动多个。
server-router:集成CAS统一认证的网关，对外统一的出入口。/web发到server-web，/cas发到server-cas。
server-oauth2:基于spring security oauth2的认证服务器
server-oauth2-router:集成server-oauth2认证的统一网关

**启动步骤：**
1、导入SQL文件，数据库名称platform_cas。数据库mysql5.7及以上。
数据库账号，密码配置：/cas/WEB-INF/classes/application.properties
cas.authn.jdbc.query[0].user=root
cas.authn.jdbc.query[0].password=root
2、启动CAS服务端。tomcat8以上。将cas.zip解压到webapps下，直接运行就好。
3、启动eureka-server。（可以只启动一个，也可以用集群的方式）
4、启动server-cas、server-web。（可以改端口启动多个）
5、启动server-router。(默认给的80端口)

**调用：**
第一次调用的时候需要登录。
用户名密码：admin/123456
用户名密码：test/123456
localhost/cas/hello
localhost/web/hi
localhost/web/hello

**注意事项：**
需要更改hosts文件：
127.0.0.1 localhost
127.0.0.1 peer1
127.0.0.1 peer2


