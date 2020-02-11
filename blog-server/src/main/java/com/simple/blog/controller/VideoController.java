package com.simple.blog.controller;

import com.simple.blog.dto.VideoDTO;
import com.simple.blog.service.VideoService;
import com.sn.common.annotation.ControllerAspectAnnotation;
import com.sn.common.dto.CommonDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author: songning
 * @date: 2020/2/11 10:58
 */
@RestController
@Slf4j
@RequestMapping(value = "/video")
public class VideoController {
    @Autowired
    private VideoService videoService;

    @PostMapping("/operateVideo")
    @ControllerAspectAnnotation(description = "上传视频 保存到数据库 返回数据库个人结果")
    public CommonDTO<VideoDTO> operateVideos(@RequestParam("file") MultipartFile multipartFile, @RequestParam("dir") String dir) {
        CommonDTO<VideoDTO> commonDTO = videoService.operateVideo(multipartFile, dir);
        return commonDTO;
    }

    @GetMapping("/original")
    public void originalVideo(HttpServletResponse response, @RequestParam("url") String url) {
        BufferedInputStream bufferedInputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            if (url != null) {
                response.setContentType("image/*");
                response.addHeader("Connection", "keep-alive");
                response.addHeader("Cache-Control", "max-age=604800");
                File file = new File(url);
                bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                servletOutputStream = response.getOutputStream();
                byte[] buffer = new byte[1024];
                while (bufferedInputStream.read(buffer) != -1) {
                    servletOutputStream.write(buffer);
                }
                servletOutputStream.flush();
            }
        } catch (Exception e) {
            log.error("获取图片失败！{} {}", e.getMessage(), url);
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (servletOutputStream != null) {
                try {
                    servletOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
