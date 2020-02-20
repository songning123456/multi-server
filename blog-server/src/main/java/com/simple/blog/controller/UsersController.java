package com.simple.blog.controller;

import com.sn.common.annotation.AControllerAspect;
import com.simple.blog.dto.UsersDTO;
import com.simple.blog.service.UsersService;
import com.simple.blog.vo.UsersVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author songning
 * @date 2019/10/24
 * description
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersService usersService;

    @PostMapping("/exist")
    @AControllerAspect(description = "获取标签")
    public CommonDTO<UsersDTO> judgeIsExist(@RequestBody CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = usersService.isExist(commonVO);
        return commonDTO;
    }

    @PostMapping("/modify")
    @AControllerAspect(description = "修改用户密码")
    public CommonDTO<UsersDTO> modifyPasswords(@RequestBody CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = usersService.modifyPassword(commonVO);
        return commonDTO;
    }

    @PostMapping("/getPermission")
    @AControllerAspect(description = "获取权限")
    public CommonDTO<UsersDTO> getPermissions(@RequestBody CommonVO<UsersVO> commonVO) {
        CommonDTO<UsersDTO> commonDTO = usersService.getPermission(commonVO);
        return commonDTO;
    }
}
