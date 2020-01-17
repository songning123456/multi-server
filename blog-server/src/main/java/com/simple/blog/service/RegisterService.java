package com.simple.blog.service;

import com.simple.blog.dto.RegisterDTO;
import com.simple.blog.vo.RegisterVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author songning
 * @date 2019/11/1
 * description
 */
public interface RegisterService {

    CommonDTO<RegisterDTO> registerAll(CommonVO<RegisterVO> commonVO);
}
