package com.simple.blog.service;

import com.simple.blog.dto.FileDTO;
import com.simple.blog.vo.FileVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author songning
 * @date 2020/2/24
 * description
 */
public interface FileService {

    /**
     * @param fileType music,album,video
     * @param md5
     * @return
     */
    CommonDTO<Integer> isExist(String fileType, String md5);

    CommonDTO<FileDTO> getFile(CommonVO<FileVO> commonVO);

    CommonDTO<FileDTO> shardUpload(MultipartFile multipartFile, String fileType, String md5, String fileName, Integer currentChunk);

    CommonDTO<FileDTO> shardMerge(String fileType, String md5, String fileName);

    CommonDTO<FileDTO> directUpload(MultipartFile multipartFile, String fileType);
}
