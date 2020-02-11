package com.simple.blog.service;

import com.simple.blog.dto.VideoDTO;
import com.sn.common.dto.CommonDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: songning
 * @date: 2020/2/11 10:58
 */
public interface VideoService {

    CommonDTO<VideoDTO> operateVideo(MultipartFile multipartFile, String dir);
}
