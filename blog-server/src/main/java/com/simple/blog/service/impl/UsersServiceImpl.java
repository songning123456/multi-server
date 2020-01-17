package com.simple.blog.service.impl;

import com.simple.blog.dto.UsersDTO;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.service.UsersService;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.UsersVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @author songning
 * @date 2019/10/24
 * description
 */
@Service
public class UsersServiceImpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;

    @Override
    public CommonDTO<UsersDTO> isExist(CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String username = commonVO.getCondition().getUsername();
        String name = usersRepository.findUsernameByNameNative(username);
        UsersDTO usersDTO;
        if (StringUtils.isEmpty(name)) {
            usersDTO = UsersDTO.builder().isExist(false).build();
        } else {
            usersDTO = UsersDTO.builder().isExist(true).build();
        }
        commonDTO.setData(Collections.singletonList(usersDTO));
        commonDTO.setTotal(1L);
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersDTO> modifyPassword(CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String oldPassword = commonVO.getCondition().getOldPassword();
        String password = commonVO.getCondition().getPassword();
        String username = httpServletRequestUtil.getUsername();
        Map<String, Object> db = usersRepository.findPasswordAndRoleByNameNative(username);
        if (db.get("password").toString().equals(oldPassword)) {
            usersRepository.updatePasswordNative(username, password);
        } else {
            commonDTO.setStatus(300);
            commonDTO.setMessage("原始密码错误");
        }
        return commonDTO;
    }

    @Override
    public CommonDTO<UsersDTO> getPermission(CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        String permission = usersRepository.getPermissionByUsername(username);
        UsersDTO usersDTO = UsersDTO.builder().permission(permission).build();
        commonDTO.setData(Collections.singletonList(usersDTO));
        commonDTO.setTotal(1L);
        return commonDTO;
    }
}
