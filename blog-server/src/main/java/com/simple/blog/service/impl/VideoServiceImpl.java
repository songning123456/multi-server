package com.simple.blog.service.impl;

import com.simple.blog.constant.CommonConstant;
import com.simple.blog.dto.VideoDTO;
import com.simple.blog.entity.Video;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.repository.VideoRepository;
import com.simple.blog.service.VideoService;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.VideoVO;
import com.sn.common.constant.HttpStatus;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.FileUtil;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public CommonDTO<VideoDTO> isExist(String md5) {
        CommonDTO<VideoDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String dirPath = System.getProperty("user.home") + File.separator + path + File.separator + CommonConstant.VIDEO + File.separator + username + File.separator + md5;
        File file = new File(dirPath);
        if (file.exists()) {
            commonDTO.setStatus(HttpStatus.HTTP_OK);
            commonDTO.setMessage("文件已上传");
        } else {
            commonDTO.setStatus(HttpStatus.HTTP_UNAVAILABLE);
            commonDTO.setMessage("文件未上传");
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<VideoDTO> shardMerge(String md5, String filename) {
        CommonDTO<VideoDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String name = filename.split("\\.")[0];
        String dirPath = System.getProperty("user.home") + File.separator + path + File.separator + CommonConstant.VIDEO + File.separator + username + File.separator + md5;
        String chunkFolder = dirPath + File.separator + name + CommonConstant.CHUNK_SUFFIX;
        File chunkFileFolder = new File(chunkFolder);
        File[] files = chunkFileFolder.listFiles();
        File mergeFile = new File(dirPath + File.separator + filename);
        List<File> fileList = Arrays.asList(files);
        FileUtil.shardMerge(fileList, mergeFile);
        FileUtil.delFile(chunkFileFolder);
        // 生成封面图片
        String userId = usersRepository.findUserIdByNameNative(username);
        String coverName = name + ".jpg";
        String videoSrc = dirPath + File.separator + filename;
        String coverSrc = dirPath + File.separator + coverName;
        String type = filename.split("\\.")[1];
        Video video = Video.builder().name(name).src(videoSrc).cover(coverSrc).type(type).userId(userId).username(username).build();
        videoRepository.save(video);
        try {
            FileUtil.fetchFrame(videoSrc, coverSrc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Video> videos = videoRepository.findVideoByUsernameNative(username);
        List<VideoDTO> list = this.entity2DTO(videos);
        commonDTO.setData(list);
        commonDTO.setTotal((long) list.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<VideoDTO> shardUpload(MultipartFile multipartFile, String md5, String filename, Integer currentChunk) {
        CommonDTO<VideoDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        if (currentChunk < 0) {
            String userId = usersRepository.findUserIdByNameNative(username);
            Map<String, String> videoMap = this.saveVideo(multipartFile);
            String type = multipartFile.getContentType();
            Video video = Video.builder().name(videoMap.get("name")).src(videoMap.get("videoSrc")).cover(videoMap.get("coverSrc")).type(type).userId(userId).username(username).build();
            videoRepository.save(video);
            List<Video> videos = videoRepository.findVideoByUsernameNative(username);
            List<VideoDTO> list = this.entity2DTO(videos);
            commonDTO.setData(list);
            commonDTO.setTotal((long) list.size());
        } else {
            String name = filename.split("\\.")[0];
            String dirPath = System.getProperty("user.home") + File.separator + path + File.separator + CommonConstant.VIDEO + File.separator + username + File.separator + md5;
            if (FileUtil.mkDirs(dirPath)) {
                File tmpFolder = new File(dirPath + File.separator + name + CommonConstant.CHUNK_SUFFIX);
                if (!tmpFolder.exists()) {
                    tmpFolder.mkdirs();
                }
                File chunkFile = new File(dirPath + File.separator + name + CommonConstant.CHUNK_SUFFIX + File.separator + currentChunk);
                if (!chunkFile.exists()) {
                    try {
                        multipartFile.transferTo(chunkFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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

    private Map<String, String> saveVideo(MultipartFile multipartFile) {
        Map<String, String> result = new HashMap<>(2);
        String username = httpServletRequestUtil.getUsername();
        // xxx.mp4
        String videoName = multipartFile.getOriginalFilename();
        // xxx
        String name = videoName.split("\\.")[0];
        // xxx.jpg
        String coverName = name + ".jpg";
        String dirPath = System.getProperty("user.home") + File.separator + path + File.separator + CommonConstant.VIDEO + File.separator + username + File.separator + name;
        String videoSrc = dirPath + File.separator + videoName;
        String coverSrc = dirPath + File.separator + coverName;
        try {
            if (FileUtil.mkDirs(dirPath)) {
                // 使用此方法保存必须要绝对路径且文件夹必须已存在,否则报错
                multipartFile.transferTo(new File(videoSrc));
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
