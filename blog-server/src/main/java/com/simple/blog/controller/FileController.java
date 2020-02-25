package com.simple.blog.controller;

import com.simple.blog.constant.CommonConstant;
import com.simple.blog.dto.FileDTO;
import com.simple.blog.service.FileService;
import com.simple.blog.vo.FileVO;
import com.sn.common.annotation.AControllerAspect;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author songning
 * @date 2020/2/24
 * description
 */
@RestController
@Slf4j
@RequestMapping(value = "/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/isExist")
    @AControllerAspect(description = "根据文件名判断是否存在")
    public CommonDTO<Integer> isExists(@RequestParam("fileType") String fileType, @RequestParam("md5") String md5) {
        CommonDTO<Integer> commonDTO = fileService.isExist(fileType, md5);
        return commonDTO;
    }

    @PostMapping("/getFile")
    @AControllerAspect(description = "获取文件")
    public CommonDTO<FileDTO> getFiles(@RequestBody CommonVO<FileVO> commonVO) {
        CommonDTO<FileDTO> commonDTO = fileService.getFile(commonVO);
        return commonDTO;
    }

    @PostMapping("/shardUpload")
    @AControllerAspect(description = "分片上传")
    public CommonDTO<FileDTO> shardUploads(@RequestParam("file") MultipartFile multipartFile,
                                           @RequestParam("fileType") String fileType,
                                           @RequestParam("md5") String md5,
                                           @RequestParam("fileName") String fileName,
                                           @RequestParam(name = "currentChunk", defaultValue = "-1") Integer currentChunk) {
        CommonDTO<FileDTO> commonDTO = fileService.shardUpload(multipartFile, fileType, md5, fileName, currentChunk);
        return commonDTO;
    }

    @GetMapping("/shardMerge")
    @AControllerAspect(description = "分片合并")
    public CommonDTO<FileDTO> shardMerges(@RequestParam("fileType") String fileType, @RequestParam("md5") String md5, @RequestParam("fileName") String fileName) {
        CommonDTO<FileDTO> commonDTO = fileService.shardMerge(fileType, md5, fileName);
        return commonDTO;
    }

    @PostMapping("/directUpload")
    @AControllerAspect(description = "直接上传")
    public CommonDTO<FileDTO> directUploads(@RequestParam("file") MultipartFile multipartFile, @RequestParam("fileType") String fileType) {
        CommonDTO<FileDTO> commonDTO = fileService.directUpload(multipartFile, fileType);
        return commonDTO;
    }

    /**
     * video请求位置
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/stream/{fileType}")
    public void original(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileType") String fileType, String url) {
        if (CommonConstant.VIDEO.equals(fileType) || CommonConstant.MUSIC.equals(fileType)) {
            this.getVideoStream(request, response, url);
        } else if (CommonConstant.IMAGE.equals(fileType)) {
            this.getImageStream(request, response, url);
        }
    }

    private void getVideoStream(HttpServletRequest request, HttpServletResponse response, String url) {
        BufferedInputStream bufferedInputStream = null;
        log.info("开始获取视频流");
        try {
            File file = new File(url);
            if (file.exists()) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
                int rangeSwitch = 0;
                long fileLength;
                String rangBytes = "";
                fileLength = file.length();

                // get file content
                InputStream inputStream = new FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(inputStream);

                // tell the client to allow accept-ranges
                response.reset();
                response.setHeader("Accept-Ranges", "bytes");

                // client requests a file block download start byte
                String range = request.getHeader("Range");
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    // bytes=270000-
                    if (rangBytes.endsWith("-")) {
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                        contentLength = fileLength - p;
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        // 客户端请求的是 270000-320000 之间的字节
                        contentLength = toLength - p + 1;
                    }
                } else {
                    contentLength = fileLength;
                }

                // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
                // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
                response.setHeader("Content-Length", Long.toString(contentLength));

                // 断点开始
                // 响应的格式是:
                // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = "bytes " + p + "-" + (fileLength - 1) + "/" + fileLength;
                    response.setHeader("Content-Range", contentRange);
                    bufferedInputStream.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + Long.toString(fileLength);
                    response.setHeader("Content-Range", contentRange);
                    bufferedInputStream.skip(p);
                } else {
                    String contentRange = "bytes " + "0-" + (fileLength - 1) + "/" + fileLength;
                    response.setHeader("Content-Range", contentRange);
                }
                String fileName = file.getName();
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

                OutputStream outputStream = response.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bSize = 1024;
                byte[] bytes = new byte[bSize];
                if (rangeSwitch == 2) {
                    // 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bSize) {
                        n = bufferedInputStream.read(bytes);
                        readLength += n;
                        outputStream.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bufferedInputStream.read(bytes, 0, (int) (contentLength - readLength));
                        outputStream.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bufferedInputStream.read(bytes)) != -1) {
                        outputStream.write(bytes, 0, n);
                    }
                }
                outputStream.flush();
                outputStream.close();
                bufferedInputStream.close();
            }
        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getImageStream(HttpServletRequest request, HttpServletResponse response, String url) {
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
