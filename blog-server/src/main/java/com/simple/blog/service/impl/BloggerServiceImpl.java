package com.simple.blog.service.impl;

import com.simple.blog.dto.BloggerDTO;
import com.simple.blog.entity.Blogger;
import com.simple.blog.repository.BloggerRepository;
import com.simple.blog.service.BloggerService;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.ClassConvertUtil;
import com.sn.common.util.FileUtil;
import com.simple.blog.util.HttpServletRequestUtil;
import com.sn.common.util.MapConvertEntityUtil;
import com.simple.blog.vo.BloggerVO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sn
 */
@Service
public class BloggerServiceImpl implements BloggerService {

    @Autowired
    private BloggerRepository bloggerRepository;

    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;

    @Override
    public CommonDTO<BloggerDTO> getBlogger(CommonVO<BloggerVO> commonVO) {
        CommonDTO<BloggerDTO> commonDTO = new CommonDTO<>();
        String userId = commonVO.getCondition().getUserId();
        String username = httpServletRequestUtil.getUsername();
        List<Map<String, Object>> list;
        if (!StringUtils.isEmpty(userId)) {
            // 其他用户 根据 作者 来查询 作者信息
            list = bloggerRepository.findByUserIdNative(userId);
        } else {
            // 登陆时根据用户名 查询 个人信息
            list = bloggerRepository.findByUsernameNative(username);
        }
        BloggerDTO bloggerDTO = new BloggerDTO();
        try {
            bloggerDTO = (BloggerDTO) MapConvertEntityUtil.mapToEntity(BloggerDTO.class, list.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        commonDTO.setData(Collections.singletonList(bloggerDTO));
        commonDTO.setTotal(1L);
        return commonDTO;
    }

    @Override
    public CommonDTO<BloggerDTO> updateBlogger(CommonVO<BloggerVO> commonVO) {
        CommonDTO<BloggerDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        Blogger blogger = new Blogger();
        List<Map<String, Object>> list;
        ClassConvertUtil.populate(commonVO.getCondition(), blogger);
        blogger.setUsername(username);
        blogger.setUpdateTime(new Date());
        if (!StringUtils.isEmpty(commonVO.getCondition().getHeadPortrait())) {
            // 如果是更新头像， 删除旧的头像
            String oldAvatar = bloggerRepository.findHeadPortraitNative(username);
            FileUtil.deleteImage(oldAvatar);
        }
        // 更新
        bloggerRepository.updateNative(blogger);
        // 再次查询并返回
        list = bloggerRepository.findByUsernameNative(username);
        BloggerDTO bloggerDTO = new BloggerDTO();
        try {
            bloggerDTO = (BloggerDTO) MapConvertEntityUtil.mapToEntity(BloggerDTO.class, list.get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        commonDTO.setData(Collections.singletonList(bloggerDTO));
        commonDTO.setTotal(1L);
        return commonDTO;
    }
}
