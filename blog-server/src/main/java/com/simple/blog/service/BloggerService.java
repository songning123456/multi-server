package com.simple.blog.service;

import com.simple.blog.dto.BloggerDTO;
import com.simple.blog.vo.BloggerVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author sn
 */
public interface BloggerService {

    /**
     * 获取登陆信息
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BloggerDTO> getBlogger(CommonVO<BloggerVO> commonVO);

    CommonDTO<BloggerDTO> updateBlogger(CommonVO<BloggerVO> commonVO);
}
