package com.simple.blog.controller;

import com.sn.common.annotation.ControllerAspectAnnotation;
import com.simple.blog.dto.LikeTagDTO;
import com.simple.blog.service.LikeTagService;
import com.simple.blog.vo.LikeTagVO;
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
 * @date 2019/9/26
 * description
 */
@RestController
@Slf4j
@RequestMapping(value = "/tag")
public class LikeTagController {

    @Autowired
    private LikeTagService likeTagService;

    @PostMapping("/getTag")
    @ControllerAspectAnnotation(description = "获取标签")
    public CommonDTO<LikeTagDTO> getTags(@RequestBody CommonVO<LikeTagVO> commonVO) {
        CommonDTO<LikeTagDTO> commonDTO = likeTagService.getTag(commonVO);
        return commonDTO;
    }

    @PostMapping("/updateTag")
    @ControllerAspectAnnotation(description = "修改标签")
    public CommonDTO<LikeTagDTO> updateTags(@RequestBody CommonVO<LikeTagVO> commonVO) {
        CommonDTO<LikeTagDTO> commonDTO = likeTagService.updateTag(commonVO);
        return commonDTO;
    }
}
