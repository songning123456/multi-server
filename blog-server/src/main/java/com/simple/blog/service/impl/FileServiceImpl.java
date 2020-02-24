package com.simple.blog.service.impl;

import com.simple.blog.constant.CommonConstant;
import com.simple.blog.dto.FileDTO;
import com.simple.blog.entity.File;
import com.simple.blog.repository.FileRepository;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.service.FileService;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.FileVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.ClassConvertUtil;
import com.sn.common.util.FileUtil;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author songning
 * @date 2020/2/24
 * description
 */
@Service
public class FileServiceImpl implements FileService {

    @Value("${blog.file.path}")
    private String path;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private FileRepository fileRepository;

    @Override
    public CommonDTO<Integer> isExist(String fileType, String md5) {
        CommonDTO<Integer> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String dirPath = System.getProperty("user.home") + java.io.File.separator + path + java.io.File.separator + fileType + java.io.File.separator + username + java.io.File.separator + md5;
        java.io.File dirFile = new java.io.File(dirPath);
        List<Integer> list = new ArrayList<>();
        if (dirFile.exists()) {
            java.io.File[] folders = dirFile.listFiles();
            for (java.io.File folder : folders) {
                // 判断是否存在 文件夹，即已经上传的部分
                if (folder.isDirectory()) {
                    java.io.File[] chunkFiles = folder.listFiles();
                    for (java.io.File file : chunkFiles) {
                        list.add(Integer.parseInt(file.getName()));
                    }
                }
            }
            if (list.size() == 0) {
                commonDTO.setMessage("文件已上传");
            } else {
                commonDTO.setData(list);
                commonDTO.setMessage("文件仅上传部分");
            }
        } else {
            commonDTO.setStatus(201);
            commonDTO.setMessage("文件未上传");
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<FileDTO> getFile(CommonVO<FileVO> commonVO) {
        CommonDTO<FileDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        String fileType = commonVO.getCondition().getFileType();
        Sort sort = Sort.by(Sort.Direction.DESC, "update_time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Page<File> filePage;
        String userId = commonVO.getCondition().getUserId();
        if (StringUtils.isEmpty(userId)) {
            // 作者本人查看自己信息
            String username = httpServletRequestUtil.getUsername();
            filePage = fileRepository.findByUsernameNative(username, fileType, pageable);
        } else {
            filePage = fileRepository.findByUserIdNative(userId, fileType, pageable);
        }
        List<File> src = filePage.getContent();
        List<FileDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, FileDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal(filePage.getTotalElements());
        return commonDTO;
    }

    @Override
    public CommonDTO<FileDTO> shardUpload(MultipartFile multipartFile, String fileType, String md5, String fileName, Integer currentChunk) {
        CommonDTO<FileDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String name = fileName.split("\\.")[0];
        String dirPath = System.getProperty("user.home") + java.io.File.separator + path + java.io.File.separator + fileType + java.io.File.separator + username + java.io.File.separator + md5;
        if (FileUtil.mkDirs(dirPath)) {
            java.io.File tmpFolder = new java.io.File(dirPath + java.io.File.separator + name + CommonConstant.CHUNK_SUFFIX);
            if (!tmpFolder.exists()) {
                tmpFolder.mkdirs();
            }
            java.io.File chunkFile = new java.io.File(dirPath + java.io.File.separator + name + CommonConstant.CHUNK_SUFFIX + java.io.File.separator + currentChunk);
            if (!chunkFile.exists()) {
                try {
                    multipartFile.transferTo(chunkFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<FileDTO> shardMerge(String fileType, String md5, String fileName) {
        CommonDTO<FileDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String name = fileName.split("\\.")[0];
        String dirPath = System.getProperty("user.home") + java.io.File.separator + path + java.io.File.separator + fileType + java.io.File.separator + username + java.io.File.separator + md5;
        String chunkFolder = dirPath + java.io.File.separator + name + CommonConstant.CHUNK_SUFFIX;
        java.io.File chunkFileFolder = new java.io.File(chunkFolder);
        java.io.File[] files = chunkFileFolder.listFiles();
        java.io.File mergeFile = new java.io.File(dirPath + java.io.File.separator + fileName);
        List<java.io.File> fileList = Arrays.asList(files);
        FileUtil.shardMerge(fileList, mergeFile);
        FileUtil.delFile(chunkFileFolder);
        if (CommonConstant.VIDEO.equals(fileType)) {
            // 生成封面图片
            String userId = usersRepository.findUserIdByNameNative(username);
            String coverName = name + ".jpg";
            String fileSrc = dirPath + java.io.File.separator + fileName;
            String coverSrc = dirPath + java.io.File.separator + coverName;
            try {
                FileUtil.fetchFrame(fileSrc, coverSrc);
            } catch (Exception e) {
                e.printStackTrace();
                commonDTO.setMessage("封面图片生成失败");
            }
            Date updateTime = new Date(mergeFile.lastModified());
            File file = File.builder().fileType(fileType).fileName(fileName).fileSrc(fileSrc).coverSrc(coverSrc).updateTime(updateTime).userId(userId).username(username).build();
            fileRepository.save(file);
        } else if (CommonConstant.MUSIC.equals(fileType)) {
            String userId = usersRepository.findUserIdByNameNative(username);
            Date updateTime = new Date(mergeFile.lastModified());
            File file = File.builder().fileType(fileType).fileName(fileName).updateTime(updateTime).userId(userId).username(username).build();
            fileRepository.save(file);
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<FileDTO> directUpload(MultipartFile multipartFile, String fileType) {
        CommonDTO<FileDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String userId = usersRepository.findUserIdByNameNative(username);
        if (CommonConstant.Image.equals(fileType)) {
            String fileSrc = this.savePicture(multipartFile, fileType, username);
            String fileName = multipartFile.getOriginalFilename().split("\\.")[0];
            Date updateTime = new Date(new java.io.File(fileSrc).lastModified());
            File file = File.builder().fileType(fileType).fileSrc(fileSrc).fileName(fileName).updateTime(updateTime).userId(userId).username(username).build();
            fileRepository.save(file);
        }
        return commonDTO;
    }

    private String savePicture(MultipartFile multipartFile, String fileType, String username) {
        String dirPath = System.getProperty("user.home") + java.io.File.separator + path + java.io.File.separator + fileType + java.io.File.separator + username;
        String imageName = UUID.randomUUID() + "." + Objects.requireNonNull(multipartFile.getOriginalFilename()).split("\\.")[1];
        String imageSrc = dirPath + java.io.File.separator + imageName;
        try {
            if (FileUtil.mkDirs(dirPath)) {
                // 使用此方法保存必须要绝对路径且文件夹必须已存在,否则报错
                multipartFile.transferTo(new java.io.File(imageSrc));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageSrc;
    }
}
