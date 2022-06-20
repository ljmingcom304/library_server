# Docker使用

## Dokcer容器间通信
```
//创建新的bridge网络
//查看现在网络
docker network ls    
//创建自定义bridge网络
docker network create -d bridge work
//在work网络下创建zookeeper容器
docker run -itd --network work --name zookeeper -p 2181:2181 -p 2888:2888 -p 3888:3888 zookeeper
//查看zookeeper的地址
docker inspect zookeeper
//在work网络下创建dubbo-admin容器
docker run -itd --network work --name dubbo-admin -p 8081:8080 -e admin.registry.address=zookeeper://172.18.0.2:2181 apache/dubbo-admin
```

## Dockerfile构建
```
# 获取基础镜像
FROM java:8

#挂载容器/tmp到host主机上的/var/lib/docker/volumes里面的目录
VOLUME /tmp

#将当前目录里面的jar文件添加到镜像，以app.jar命名
ADD *jar app.jar

#执行命令，java -jar /app.jar
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","app.jar"]

#打包镜像，默认Dockerfile文件当前目录，末尾点号指当前目录
# docker build -t hello:v1.0.0 .
```

## 注意事项
```
由于存在容器间通信隔离的问题，尽量避免将微服务和Zookeeper部署于同一个Docker服务中
方案一：使用host网络，Docker 容器将会和宿主主机共享网络，但是当存在多个虚拟网卡时会存在问题
方案二：使用环境变量Dubbo_IP_TO_REGISTRY，当次环境变量存在时，Dubbo 会使用次环境变量的值去注册。
docker run --name demo-server -d -p 8080:8080 -p 28880:28880 --restart=always -e Dubbo_IP_TO_REGISTRY=192.168.2.2  demo-image
    Dubbo_IP_TO_REGISTRY --- 注册到注册中心的IP地址
    Dubbo_PORT_TO_REGISTRY --- 注册到注册中心的端口
    Dubbo_IP_TO_BIND --- 监听IP地址
    Dubbo_PORT_TO_BIND --- 监听端口
 --restart=always表示启动Docker时自动启动相关容器
```

# 本地依赖问题
直接打包server_parent工程即可

