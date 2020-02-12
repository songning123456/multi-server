package com.simple.blog.service;

import com.simple.blog.dto.VideoDTO;
import com.simple.blog.vo.VideoVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author: songning
 * @date: 2020/2/11 10:58
 */
public interface VideoService {

    CommonDTO<VideoDTO> getVideo(CommonVO<VideoVO> commonVO);

    CommonDTO<VideoDTO> isExist(String md5);

    CommonDTO<VideoDTO> shardMerge(String md5, String filename);

    CommonDTO<VideoDTO> shardUpload(MultipartFile multipartFile, String md5, String filename, Integer currentChunk);
}
