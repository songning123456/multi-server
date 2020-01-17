#!/bin/sh
rm -rf blog-0.0.1-SNAPSHOT.jar
rz
docker stop blog-server_container
docker rm blog-server_container
docker rmi blog-server_image
docker build -t blog-server_image -f dockerfile-blog-server .
docker run --name blog-server_container -d -p 8072:8072 -v /etc/localtime:/etc/localtime -v /usr/share/fonts:/usr/share/fonts -v /root/simple-blog/blog-server-tomcat/images:/root/simple-blog/avatar --link mysql_container:localhost --link redis_container blog-server_image