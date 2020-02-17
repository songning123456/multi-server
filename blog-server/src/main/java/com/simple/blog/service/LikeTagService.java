package com.simple.blog.service;

import com.simple.blog.dto.LikeTagDTO;
import com.simple.blog.vo.LikeTagVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author songning
 * @date 2019/9/26
 * description
 */
public interface LikeTagService {

    CommonDTO<LikeTagDTO> updateTag(CommonVO<LikeTagVO> commonVO);
}
