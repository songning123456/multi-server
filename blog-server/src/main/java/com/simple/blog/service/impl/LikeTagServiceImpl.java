package com.simple.blog.service.impl;

import com.simple.blog.dto.LikeTagDTO;
import com.simple.blog.entity.LikeTag;
import com.simple.blog.jpql.JpqlDao;
import com.simple.blog.repository.LikeTagRepository;
import com.simple.blog.service.LikeTagService;
import com.sn.common.util.ClassConvertUtil;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.LikeTagVO;
import com.sn.common.constant.HttpStatus;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songning
 * @date 2019/9/26
 * description
 */
@Service
public class LikeTagServiceImpl implements LikeTagService {

    @Autowired
    private LikeTagRepository likeTagRepository;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;
    @Autowired
    private JpqlDao jpqlDao;

    @Override
    public CommonDTO<LikeTagDTO> getTag(CommonVO<LikeTagVO> commonVO) {
        CommonDTO<LikeTagDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        String articleId = commonVO.getCondition().getArticleId();
        LikeTagDTO likeTagDTO = new LikeTagDTO();
        LikeTag likeTag = likeTagRepository.getNative(username, articleId);
        if (likeTag == null) {
            likeTag = new LikeTag();
            likeTag.setArticleId(articleId);
            likeTag.setHasRead(0);
            likeTag.setLove(0);
            likeTag.setUsername(username);
            likeTagRepository.save(likeTag);
        }
        ClassConvertUtil.populate(likeTag, likeTagDTO);
        Map<String, Object> dataExt = likeTagRepository.sumByArticleIdNative(articleId);
        commonDTO.setDataExt(dataExt);
        commonDTO.setData(Collections.singletonList(likeTagDTO));
        return commonDTO;
    }


    @Override
    public CommonDTO<LikeTagDTO> updateTag(CommonVO<LikeTagVO> commonVO) {
        CommonDTO<LikeTagDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        String articleId = commonVO.getCondition().getArticleId();
        Integer love = commonVO.getCondition().getLove();
        Integer hasRead = commonVO.getCondition().getHasRead();
        LikeTagDTO likeTagDTO = new LikeTagDTO();
        // 更新 love 按钮； 否則更新是否已读按钮
        Map<String, Object> params = new HashMap<>(2);
        if (love != null) {
            params.put("love", love + 10);
        }
        if (hasRead != null) {
            params.put("hasRead", hasRead + 10);
        }
        params.put("username", username);
        params.put("articleId", articleId);
        jpqlDao.update("likeTagJPQL.updateLikeTag", params);
        if (love != null) {
            LikeTag likeTag = likeTagRepository.getNative(username, articleId);
            likeTagDTO.setLove(likeTag.getLove());
            Map<String, Object> dataExt = likeTagRepository.sumByArticleIdNative(articleId);
            commonDTO.setDataExt(dataExt);
        }
        if (hasRead != null) {
            LikeTag likeTag = likeTagRepository.getNative(username, articleId);
            likeTagDTO.setHasRead(likeTag.getHasRead());
            likeTag.setHasRead(likeTag.getHasRead());
        }
        commonDTO.setData(Collections.singletonList(likeTagDTO));
        return commonDTO;
    }
}
