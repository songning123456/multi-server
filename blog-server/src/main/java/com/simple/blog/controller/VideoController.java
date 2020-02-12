package com.simple.blog.controller;

import com.simple.blog.dto.VideoDTO;
import com.simple.blog.service.VideoService;
import com.simple.blog.vo.VideoVO;
import com.sn.common.annotation.ControllerAspectAnnotation;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
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

    @PostMapping("/getVideo")
    @ControllerAspectAnnotation(description = "获取视频")
    public CommonDTO<VideoDTO> getVideos(@RequestBody CommonVO<VideoVO> commonVO) {
        CommonDTO<VideoDTO> commonDTO = videoService.getVideo(commonVO);
        return commonDTO;
    }

    @GetMapping("/isExist")
    @ControllerAspectAnnotation(description = "根据文件名判断是否存在")
    public CommonDTO<VideoDTO> isExists(@RequestParam("md5") String md5) {
        CommonDTO<VideoDTO> commonDTO = videoService.isExist(md5);
        return commonDTO;
    }

    @GetMapping("/shardMerge")
    @ControllerAspectAnnotation(description = "合并分片")
    public CommonDTO<VideoDTO> shardMerges(@RequestParam("md5") String md5, @RequestParam("filename") String filename) {
        CommonDTO<VideoDTO> commonDTO = videoService.shardMerge(md5, filename);
        return commonDTO;
    }

    @PostMapping("/shardUpload")
    @ControllerAspectAnnotation(description = "分片上传")
    public CommonDTO<VideoDTO> shardUploads(@RequestParam("file") MultipartFile multipartFile,
                                            @RequestParam("md5") String md5,
                                            @RequestParam("filename") String filename,
                                            @RequestParam(name = "currentChunk", defaultValue = "-1") Integer currentChunk) {
        CommonDTO<VideoDTO> commonDTO = videoService.shardUpload(multipartFile, md5, filename, currentChunk);
        return commonDTO;
    }

    @GetMapping("/original")
    public void originalVideo(HttpServletResponse response, @RequestParam("url") String url) {
        BufferedInputStream bufferedInputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            if (url != null) {
                response.addHeader("Accept-Ranges", "bytes");
                response.addHeader("Content-Type", "audio/mpeg;charset=UTF-8");
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
