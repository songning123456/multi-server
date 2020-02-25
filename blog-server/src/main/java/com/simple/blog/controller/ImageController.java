package com.simple.blog.controller;

import com.sn.common.annotation.AControllerAspect;
import com.simple.blog.dto.ImageDTO;
import com.simple.blog.service.ImageService;
import com.simple.blog.vo.ImageVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @AControllerAspect(description = "把上传的图片保存")
    public CommonDTO<ImageDTO> saveImages(@RequestParam("file") MultipartFile multipartFile, @RequestParam("dir") String dir) {
        CommonDTO<ImageDTO> commonDTO = imageService.saveImage(multipartFile, dir);
        return commonDTO;
    }

    @PostMapping("/delete")
    @AControllerAspect(description = "删除指定位置图片")
    public <T> CommonDTO<T> deleteImages(@RequestBody CommonVO<ImageVO> commonVO) {
        CommonDTO<T> commonDTO = imageService.deleteImage(commonVO);
        return commonDTO;
    }
}
