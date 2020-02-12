package com.simple.blog.service.impl;

import com.simple.blog.dto.ImageDTO;
import com.simple.blog.entity.Image;
import com.simple.blog.repository.ImageRepository;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.service.ImageService;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.FileUtil;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.ImageVO;
import com.sn.common.vo.CommonVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author songning
 * @date 2019/10/28
 * description
 */
@Service
public class ImageServiceImpl implements ImageService {

    @Value("${blog.image.path}")
    private String path;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private UsersRepository usersRepository;
    private final Object object = new Object();

    @Override
    public CommonDTO<ImageDTO> saveImage(MultipartFile multipartFile, String dir) {
        CommonDTO<ImageDTO> commonDTO = new CommonDTO<>();
        String imageSrc = this.savePicture(multipartFile, dir);
        ImageDTO imageDTO = ImageDTO.builder().imageSrc(imageSrc).build();
        commonDTO.setData(Collections.singletonList(imageDTO));
        commonDTO.setTotal(1L);
        return commonDTO;
    }

    @Override
    public CommonDTO<ImageDTO> operateAlbum(MultipartFile multipartFile, String dir) {
        CommonDTO<ImageDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String userId = usersRepository.findUserIdByNameNative(username);
        synchronized (object) {
            String imageSrc = this.savePicture(multipartFile, dir);
            Image image = Image.builder().imageSrc(imageSrc).username(username).userId(userId).build();
            imageRepository.save(image);
        }
        List<String> images = imageRepository.findImageSrcByUsernameNative(username);
        List<ImageDTO> list = new ArrayList<>();
        ImageDTO imageDTO;
        for (String src : images) {
            imageDTO = ImageDTO.builder().imageSrc(src).build();
            list.add(imageDTO);
        }
        commonDTO.setData(list);
        commonDTO.setTotal((long) list.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<ImageDTO> getAlbum(CommonVO<ImageVO> commonVO) {
        CommonDTO<ImageDTO> commonDTO = new CommonDTO<>();
        List<String> images;
        // 获取个人相册
        if (StringUtils.isEmpty(commonVO.getCondition().getUserId())) {
            String username = httpServletRequestUtil.getUsername();
            images = imageRepository.findImageSrcByUsernameNative(username);
        } else {
            // 查看作者信息时，获取的他人相册
            String userId = commonVO.getCondition().getUserId();
            images = imageRepository.findImageSrcByUserIdNative(userId);
        }
        List<ImageDTO> list = new ArrayList<>();
        ImageDTO imageDTO;
        for (String src : images) {
            imageDTO = ImageDTO.builder().imageSrc(src).build();
            list.add(imageDTO);
        }
        commonDTO.setData(list);
        commonDTO.setTotal((long) list.size());
        return commonDTO;
    }

    @Override
    public <T> CommonDTO<T> deleteImage(CommonVO<ImageVO> commonVO) {
        CommonDTO<T> commonDTO = new CommonDTO<>();
        String imageName = commonVO.getCondition().getImageName();
        String dir = commonVO.getCondition().getDir();
        String imagePath = System.getProperty("user.home") + File.separator + path + File.separator + dir;
        if (!"avatar".equals(dir)) {
            String username = httpServletRequestUtil.getUsername();
            imagePath = imagePath + File.separator + username;
        }
        String filename = imagePath + File.separator + imageName;
        File file = new File(filename);
        if (file.exists()) {
            boolean result = file.delete();
            if (!result) {
                try {
                    throw new Exception("删除失败");
                } catch (Exception e) {
                    e.printStackTrace();
                    commonDTO.setStatus(300);
                    commonDTO.setMessage("删除失败");
                }
            }
        } else {
            try {
                throw new Exception("文件不存在");
            } catch (Exception e) {
                e.printStackTrace();
                commonDTO.setStatus(300);
                commonDTO.setMessage("文件不存在");
            }
        }
        return commonDTO;
    }

    private String savePicture(MultipartFile multipartFile, String dir) {
        if (StringUtils.isEmpty(dir)) {
            dir = "other";
        }
        String imageFile = System.getProperty("user.home") + File.separator + path + File.separator + dir;
        // 目前 主要三处 注册/修改信息 -> avatar; 写文章 -> article; 相册 -> album;(头像信息不需要username,当时还未注册)
        if (!"avatar".equals(dir)) {
            String username = httpServletRequestUtil.getUsername();
            imageFile = imageFile + File.separator + username;
        }
        String imageName = UUID.randomUUID() + "." + Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.")[1];
        String imageSrc = imageFile + File.separator + imageName;
        try {
            if (FileUtil.mkDirs(imageFile)) {
                // 使用此方法保存必须要绝对路径且文件夹必须已存在,否则报错
                multipartFile.transferTo(new File(imageSrc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageSrc;
    }
}
