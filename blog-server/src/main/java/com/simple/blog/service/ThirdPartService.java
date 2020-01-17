package com.simple.blog.service;

import com.simple.blog.dto.ThirdPartDTO;
import com.simple.blog.vo.ThirdPartVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author songning
 * @date 2019/12/23
 * description
 */
public interface ThirdPartService {
    CommonDTO<ThirdPartDTO> getGitHub(CommonVO<ThirdPartVO> commonVO);
}
