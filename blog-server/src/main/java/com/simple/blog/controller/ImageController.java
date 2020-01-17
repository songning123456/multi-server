package com.simple.blog.controller;

import com.sn.common.annotation.ControllerAspectAnnotation;
import com.simple.blog.dto.ImageDTO;
import com.simple.blog.service.ImageService;
import com.simple.blog.vo.ImageVO;
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
 * @author songning
 * @date 2019/10/28
 * description
 */
@RestController
@Slf4j
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/save")
    @ControllerAspectAnnotation(description = "把上传的图片保存")
    public CommonDTO<ImageDTO> saveImages(@RequestParam("file") MultipartFile multipartFile, @RequestParam("dir") String dir) {
        CommonDTO<ImageDTO> commonDTO = imageService.saveImage(multipartFile, dir);
        return commonDTO;
    }

    @PostMapping("/operateAlbum")
    @ControllerAspectAnnotation(description = "上传图片 保存到数据库 返回数据库个人结果")
    public CommonDTO<ImageDTO> operateAlbums(@RequestParam("file") MultipartFile multipartFile, @RequestParam("dir") String dir) {
        CommonDTO<ImageDTO> commonDTO = imageService.operateAlbum(multipartFile, dir);
        return commonDTO;
    }

    @PostMapping("/getAlbum")
    @ControllerAspectAnnotation(description = "获取相册")
    public CommonDTO<ImageDTO> getAlbums(@RequestBody CommonVO<ImageVO> commonVO) {
        CommonDTO<ImageDTO> commonDTO = imageService.getAlbum(commonVO);
        return commonDTO;
    }

    @PostMapping("/delete")
    @ControllerAspectAnnotation(description = "删除指定位置图片")
    public <T> CommonDTO<T> deleteImages(@RequestBody CommonVO<ImageVO> commonVO) {
        CommonDTO<T> commonDTO = imageService.deleteImage(commonVO);
        return commonDTO;
    }

    /**
     * 原始图像1:1
     *
     * @param response
     * @param url
     */
    @GetMapping("/original")
    public void originalImage(HttpServletResponse response, @RequestParam("url") String url) {
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
