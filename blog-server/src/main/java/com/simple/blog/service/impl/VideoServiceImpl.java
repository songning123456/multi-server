package com.simple.blog.service.impl;

import com.simple.blog.dto.VideoDTO;
import com.simple.blog.entity.Video;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.repository.VideoRepository;
import com.simple.blog.service.VideoService;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.VideoVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.FileUtil;
import com.sn.common.vo.CommonVO;
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
    public CommonDTO<VideoDTO> getVideo(CommonVO<VideoVO> commonVO) {
        CommonDTO<VideoDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        List<Video> videos = videoRepository.findVideoByUsernameNative(username);
        List<VideoDTO> list = this.entity2DTO(videos);
        commonDTO.setData(list);
        commonDTO.setTotal((long) list.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<VideoDTO> operateVideo(MultipartFile multipartFile, String dir) {
        CommonDTO<VideoDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String userId = usersRepository.findUserIdByNameNative(username);
        synchronized (object) {
            Map<String, String> videoMap = this.saveVideo(multipartFile, dir);
            String type = multipartFile.getContentType();
            Video video = Video.builder().name(videoMap.get("name")).src(videoMap.get("videoSrc")).cover(videoMap.get("coverSrc")).type(type).userId(userId).username(username).build();
            videoRepository.save(video);
        }
        List<Video> videos = videoRepository.findVideoByUsernameNative(username);
        List<VideoDTO> list = this.entity2DTO(videos);
        commonDTO.setData(list);
        commonDTO.setTotal((long) list.size());
        return commonDTO;
    }

    private List<VideoDTO> entity2DTO(List<Video> entityList) {
        List<VideoDTO> dtoList = new ArrayList<>();
        VideoDTO videoDTO;
        for (Video video : entityList) {
            String src = video.getSrc();
            String type = video.getType();
            String cover = video.getCover();
            String name = video.getName();
            videoDTO = VideoDTO.builder().src(src).type(type).cover(cover).name(name).build();
            dtoList.add(videoDTO);
        }
        return dtoList;
    }

    private Map<String, String> saveVideo(MultipartFile multipartFile, String dir) {
        Map<String, String> result = new HashMap<>(2);
        String username = httpServletRequestUtil.getUsername();
        // xxx.mp4
        String videoName = multipartFile.getOriginalFilename();
        // xxx
        String name = videoName.split("\\.")[0];
        // xxx.jpg
        String coverName = name + ".jpg";
        String videoFile = System.getProperty("user.home") + File.separator + path + File.separator + dir + File.separator + username + File.separator + UUID.randomUUID().toString().substring(0, 9) + name;
        String videoSrc = videoFile + File.separator + videoName;
        String coverSrc = videoFile + File.separator + coverName;
        try {
            InputStream inputStream = multipartFile.getInputStream();
            if (FileUtil.mkDirs(videoFile)) {
                FileUtil.streamToImage(videoSrc, inputStream);
                try {
                    FileUtil.fetchFrame(videoSrc, coverSrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("videoSrc", videoSrc);
        result.put("coverSrc", coverSrc);
        result.put("name", name);
        return result;
    }
}
