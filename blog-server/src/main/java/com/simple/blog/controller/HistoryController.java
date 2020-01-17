package com.simple.blog.controller;

import com.sn.common.annotation.ControllerAspectAnnotation;
import com.simple.blog.dto.HistoryDTO;
import com.simple.blog.service.HistoryService;
import com.simple.blog.vo.HistoryVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songning
 * @date 2019/10/22
 * description
 */
@RestController
@Slf4j
@RequestMapping(value = "/history")
public class HistoryController {

    @Autowired
    private HistoryService historyService;

    @PostMapping("/insert")
    @ControllerAspectAnnotation(description = "插入记录信息")
    public CommonDTO<HistoryDTO> inserts(@RequestBody CommonVO<HistoryVO> commonVO) {
        CommonDTO<HistoryDTO> commonDTO = historyService.insertHistory(commonVO);
        return commonDTO;
    }

    @PostMapping("/get")
    @ControllerAspectAnnotation(description = "获取个人信息")
    public CommonDTO<HistoryDTO> getHistoryInfo(@RequestBody CommonVO<HistoryVO> commonVO) {
        CommonDTO<HistoryDTO> commonDTO = historyService.getHistory(commonVO);
        return commonDTO;
    }
}
