 FROM java:8
 COPY blog-server-1.0-SNAPSHOT.jar /blog-server/simple-blog-server.jar
 EXPOSE 8072
 CMD ["java", "-jar", "/blog-server/simple-blog-server.jar"]
 RUN echo "Asia/Shanghai" > /etc/timezone
 ENV LANG C.UTF-8