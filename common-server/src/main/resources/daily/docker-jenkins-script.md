### window 重新 刷新dist文件
```
 cd D:\WebStormProject\blog-front
 npm run build
 tar -zcvf dist.tar.gz dist
```

### linux 部署blog-front
```
 rm -rf dist.tar.gz
 rm -rf dist
 rz 
 tar -zxvf dist.tar.gz
 docker stop blog-front_container
 docker rm blog-front_container
 docker rmi blog-front_image
 docker build -t blog-front_image -f dockerfile-blog-front .
 docker run --name blog-front_container -d -p 8070:80 --link blog-server_container blog-front_image
```


### window 重新打jar包
`mvn clean install -DskipTests`


### linux 重新部署blog-server
```
 rm -rf blog-0.0.1-SNAPSHOT.jar
 rz xxx
 docker stop blog-server_container
 docker rm blog-server_container
 docker rmi blog-server_image
 docker build -t blog-server_image -f dockerfile-blog-server .
 docker run --name blog-server_container -d -p 8072:8072 -v /etc/localtime:/etc/localtime -v /usr/share/fonts:/usr/share/fonts -v /root/simple-blog/blog-server-tomcat/images:/root/simple-blog/avatar --link mysql_container:localhost blog-server_image
```

### jenkins shell 执行脚本 blog-server
```
 mvn clean install -DskipTests
 mv ./target/blog-0.0.1-SNAPSHOT.jar ./
 docker stop blog-server_container
 docker rm blog-server_container
 docker rmi blog-server_image
 docker build -t blog-server_image .
 docker run --name blog-server_container -d -p 8072:8072 -v /etc/localtime:/etc/localtime -v /usr/share/fonts:/usr/share/fonts -v /root/simple-blog/blog-server-tomcat/images:/root/simple-blog/avatar --link mysql_container:localhost --link redis_container blog-server_image
```

### jenkins shell 执行脚本 blog-front
```
 #!/bin/bash
 BUILD_ID=dontKillMe
 npm config set registry https://registry.npm.taobao.org
 npm install --unsafe-perm=true --allow-root
 npm run build
 docker stop blog-front_container
 docker rm blog-front_container
 docker rmi blog-front_image
 docker build -t blog-front_image .
 docker run --name blog-front_container -d -p 8070:80 --link blog-server_container blog-front_image
```