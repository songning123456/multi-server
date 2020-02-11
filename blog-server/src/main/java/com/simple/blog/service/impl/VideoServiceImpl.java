package com.simple.blog.service.impl;

import com.simple.blog.dto.VideoDTO;
import com.simple.blog.entity.Video;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.repository.VideoRepository;
import com.simple.blog.service.VideoService;
import com.simple.blog.util.HttpServletRequestUtil;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author: songning
 * @date: 2020/2/11 10:59
 */
@Service
public class VideoServiceImpl implements VideoService {

    @Value("${blog.video.path}")
    private String path;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private VideoRepository videoRepository;
    private final Object object = new Object();

    @Override
    public CommonDTO<VideoDTO> operateVideo(MultipartFile multipartFile, String dir) {
        CommonDTO<VideoDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String userId = usersRepository.findUserIdByNameNative(username);
        synchronized (object) {
            String videoSrc = this.saveVideo(multipartFile, dir);
            Video video = Video.builder().src(videoSrc).type(multipartFile.getContentType()).userId(userId).username(username).build();
            videoRepository.save(video);
        }
        List<Map<String, Object>> videos = videoRepository.findVideoByUsernameNative(username);
        List<VideoDTO> list = new ArrayList<>();
        VideoDTO videoDTO;
        for (Map<String, Object> video : videos) {
            String src = video.get("src").toString();
            String type = video.get("type").toString();
            videoDTO = VideoDTO.builder().src(src).type(type).build();
            list.add(videoDTO);
        }
        commonDTO.setData(list);
        commonDTO.setTotal((long) list.size());
        return commonDTO;
    }

    private String saveVideo(MultipartFile multipartFile, String dir) {
        String username = httpServletRequestUtil.getUsername();
        String videoFile = System.getProperty("user.home") + File.separator + path + File.separator + dir + File.separator + username;
        String videoName = UUID.randomUUID() + "." + Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.")[1];
        String videoSrc = videoFile + File.separator + videoName;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            if (FileUtil.mkDirs(videoFile)) {
                FileUtil.streamToImage(videoSrc, inputStream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return videoSrc;
    }
}
