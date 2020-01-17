###  配置
` cd /etc/nginx`
`vim nginx.conf`

```
 # include /etc/nginx/conf.d/*.conf; // 一定要注释掉默认的配置文件
 server {
         listen        80;
         server_name   localhost;
      location ^~ /server/ {
         proxy_pass   http://localhost:8072/;
         proxy_set_header X-Real-IP $remote_addr;
      }
      location / {
            proxy_pass  http://localhost:8070/;
      }
   }
```

`systemctl restart nginx.service` // 修改配置文件后重启nginx