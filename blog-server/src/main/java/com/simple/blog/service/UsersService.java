package com.simple.blog.service;

import com.simple.blog.dto.UsersDTO;
import com.simple.blog.vo.UsersVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author songning
 * @date 2019/10/24
 * description
 */
public interface UsersService {

    CommonDTO<UsersDTO> isExist(CommonVO<UsersVO> commonVO);

    CommonDTO<UsersDTO> modifyPassword(CommonVO<UsersVO> commonVO);

    CommonDTO<UsersDTO> getPermission(CommonVO<UsersVO> commonVO);
}
