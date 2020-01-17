[TOC]

### linux环境安装docker
* docker 要求运行在centos 7以上，要求系统64位，系统内核版本3.10以上*
* `uname -an` //查看系统版本
* `yum -y install docker` //下载安装docker
* `service docker start` // 启动docker服务
* `docker version` // 检查docker是否安装成功
* `mkdir simple-blog`
* `cd simple-blog` // **以下所有操作全部在此文件夹下完成**

### docker-mysql开发环境安装
* `docker pull mysql` //拉取mysql镜像
* 创建mysql映射文件夹
  ```
  mkdir mysql-config
  mkdir mysql-logs
  mkdir mysql-data
  ```
* 启动镜像
  ```
  docker run --name mysql_container  
  -d -p 3306:3306 
  -v $PWD/mysql-config:/etc/mysql/conf.d 
  -v $PWD/mysql-logs:/logs 
  -v $PWD/mysql-data:/var/lib/mysql 
  -e MYSQL_ROOT_PASSWORD=123456 mysql:latest
  ```
* `docker exec mysql_container mysql -uroot -p123456 -e 'create database simple_blog;'` // mysql创建数据库

### docker-redis开发环境安装
* `docker pull redis` //拉取redis镜像
* 创建redis映射文件夹
  ```
  mkdir redis-config
  mkdir redis-data
  ```
* 启动镜像(*redis.conf本地上传到redis-config文件夹下*)
  ```
  docker run --name redis_container
  -d -p 6379:6379
  -v $PWD/redis-config/redis.conf:/usr/local/etc/redis/redis.conf 
  -v $PWD/redis-data:/data 
  redis redis-server   
  /usr/local/etc/redis/redis.conf 
  --appendonly yes
  ```

### docker-tomcat开发环境安装
* `rz xxx.jar` //上传jar包到simple-blog文件夹下
* 编辑dockerfile
  ```
  vim dockerfile-blog-server
    # Docker image for springboot file run
    # VERSION 0.1
    # Author: songning
    # 基础镜像使用java
    FROM java:8
    # 将jar包复制到容器中的/blog-server目录下，并更名为simple-blog-server.jar
    COPY xxx.jar /blog-server/simple-blog-server.jar
    #对外端口
    EXPOSE 8072
    #执行命令 java -jar simple-blog-server.jar
    CMD ["java", "-jar", "/blog-server/simple-blog-server.jar"] 
    RUN echo "Asia/Shanghai" > /etc/timezone
  ```
* 构建镜像
`docker build -t blog-server_image:0.1 -f dockerfile-blog-server .`
* 创建blog-server_container映射文件夹
  ```
  mkdir blog-server-tomcat
  cd blog-server-tomcat
  mkdir webapps
  cd ..
  ```
* 启动容器(**保证mysql中相关databse已经创建好 create database xxx**)
  ```
  docker run --name blog-server_container 
  -d -p 8072:8072
  --link mysql_container:localhost 
  --link redis_container
  -v /etc/localtime:/etc/localtime
  -v /root/simple-blog/blog-server-tomcat/images:/root/simple-blog/avatar
  -v /usr/share/fonts:/usr/share/fonts
  blog-server_image:0.1
  ```
* 注意事项
  ```
  在hosts文件中添加映射 localhost redis_container;
  applicationyml中redis.host:redis_container;
  ```

### docker-nginx开发环境安装
* `tar -zcvf dist.tar.gz dist` //本地压缩dist文件

* `rz dist.tar.gz` //上传文件

* `tar -zxvf dist.tar.gz` //解压文件

* 编辑dockerfile
  ```
  vim dockerfile-blog-front
    # 设置基础镜像
    FROM nginx
    # 定义作者
    MAINATNER songning
    # 将dist文件中的内容复制到 /usr/share/nginx/html/ 这个目录下面
    COPY dist/ /usr/share/nginx/html/
  ```
  
* 构建镜像
  `docker build -t blog-front_image:0.1 -f dockerfile-blog-front .`
  
* 启动容器
  ```
  docker run --name blog-front_container 
  -d -p 8070:80
  -v /etc/localtime:/etc/localtime
  --link blog-server_container
  blog-front_image:0.1
  ```
  
* 浏览器访问 http://服务器ip:8070/#/

### 重新部署
* 重新部署blog-server
  ```
  * rm -rf blog-0.0.1-SNAPSHOT.jar
  * maven clean install
  * rz blog-0.0.1-SNAPSHOT.jar
  * docker stop blog-server_container
  * docker rm container_id
  * 构建镜像，启动容器(镜像版本号依次增加)
  ```
* 重新部署blog-front 
  
  ```
  * rm -rf dist.tar.gz
  * rm -rf dist
  * npm run build
  * tar -zcvf dist.tar.gz dist
  * rz dist.tar.gz
  * tar -zxvf dist.tar.gz
  * docker stop blog-server_container
  * docker rm container_id
  * 构建镜像，启动容器(镜像版本号依次增加)
  ```

### jenkins启动关闭重启命令
* service jenkins start
* service jenkins restart
* service jenkins stop

### 常用命令
* docker ps //正在运行的docker容器
* docker ps -a //所有docker容器
* docker pull xxx_image // 拉去镜像
* docker images //所有docker镜像
* docker exec -it xxx_container bash //后台进入docker
* docker logs container_id //打印日志
* docker run -it xxx_image bash
* rm -rf xxx //删除
* tar -zcvf dist.tar.gz dist //压缩
* tar -zxvf dist.tar.gz //解压
* -t 构建的镜像名称
* --link 添加链接到另一个容器
* -p 配置端口映射 外部访问端口:容器内部端口
* -d 后台运行
* --name 给容器取名
* -f 指定构建文件

### 参考文章
* https://blog.csdn.net/stoneBridge1920/article/details/100894041
* https://blog.csdn.net/stoneBridge1920/article/details/102920169
* https://blog.csdn.net/cow5287687/article/details/80420010
* https://blog.csdn.net/jiangyu1013/article/details/84572582