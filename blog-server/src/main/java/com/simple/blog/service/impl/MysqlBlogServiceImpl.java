package com.simple.blog.service.impl;

import com.sn.common.constant.HttpStatus;
import com.simple.blog.dto.BlogDTO;
import com.simple.blog.entity.Blog;
import com.simple.blog.repository.BlogRepository;
import com.simple.blog.repository.HistoryRepository;
import com.simple.blog.repository.LikeTagRepository;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.service.BlogService;
import com.simple.blog.util.HttpServletRequestUtil;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.MapConvertEntityUtil;
import com.simple.blog.vo.BlogVO;
import com.simple.blog.vo.LabelVO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author sn
 */
@Service
public class MysqlBlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private LikeTagRepository likeTagRepository;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;

    @Override
    public CommonDTO<BlogDTO> saveArticle(CommonVO<BlogVO> commonVO) {
        String title = commonVO.getCondition().getTitle();
        String content = commonVO.getCondition().getContent();
        String kinds = commonVO.getCondition().getKinds();
        Integer readTimes = commonVO.getCondition().getReadTimes();
        String author = commonVO.getCondition().getAuthor();
        Blog blog = Blog.builder().title(title).kinds(kinds).author(author).content(content).updateTime(new Date()).readTimes(readTimes).build();
        blogRepository.save(blog);
        return new CommonDTO<>();
    }

    @Override
    public CommonDTO<BlogDTO> getAbstract(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        String kinds = commonVO.getCondition().getKinds();
        Sort sort = Sort.by(Sort.Direction.DESC, "update_time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Page<Map<String, Object>> blogPage = blogRepository.findAbstract(kinds, pageable);
        List<Map<String, Object>> src = blogPage.getContent();
        Long total = blogPage.getTotalElements();
        List<BlogDTO> target = new ArrayList<>();
        for (Map<String, Object> map : src) {
            try {
                BlogDTO blogDTO = (BlogDTO) MapConvertEntityUtil.mapToEntity(BlogDTO.class, map);
                target.add(blogDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        commonDTO.setData(target);
        commonDTO.setTotal(total);
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getByAuthor(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        String userId = usersRepository.findUserIdByNameNative(username);
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        Sort sort = Sort.by(Sort.Direction.DESC, "update_time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Page<Map<String, Object>> blogPage = blogRepository.findByUserIdNative(userId, pageable);
        List<Map<String, Object>> src = blogPage.getContent();
        Long total = blogPage.getTotalElements();
        List<BlogDTO> target = new ArrayList<>();
        for (Map<String, Object> map : src) {
            try {
                BlogDTO blogDTO = (BlogDTO) MapConvertEntityUtil.mapToEntity(BlogDTO.class, map);
                target.add(blogDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        commonDTO.setData(target);
        commonDTO.setTotal(total);
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getByLove(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        List<String> articleIds = likeTagRepository.getArticleIdByUserNameAndLoveNative(username);
        if (articleIds.size() == 0) {
            commonDTO.setStatus(HttpStatus.HTTP_INTERNAL_ERROR);
            commonDTO.setMessage("您还未点赞过文章");
            return commonDTO;
        }
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        Sort sort = Sort.by(Sort.Direction.DESC, "update_time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Page<Map<String, Object>> blogPage = blogRepository.findByIdNative(articleIds, pageable);
        List<Map<String, Object>> src = blogPage.getContent();
        Long total = blogPage.getTotalElements();
        List<BlogDTO> target = new ArrayList<>();
        for (Map<String, Object> map : src) {
            try {
                BlogDTO blogDTO = (BlogDTO) MapConvertEntityUtil.mapToEntity(BlogDTO.class, map);
                target.add(blogDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        commonDTO.setData(target);
        commonDTO.setTotal(total);
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getContent(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String id = commonVO.getCondition().getId();
        Map<String, Object> blog = blogRepository.findByIdNative(id);
        Integer readTimes = (Integer) blog.get("readTimes");
        blogRepository.updateReadTimes(id, ++readTimes);
        BlogDTO blogDTO = null;
        try {
            blogDTO = (BlogDTO) MapConvertEntityUtil.mapToEntity(BlogDTO.class, blog);
        } catch (Exception e) {
            e.printStackTrace();
        }
        commonDTO.setData(Collections.singletonList(blogDTO));
        commonDTO.setTotal(1L);
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getHotArticle(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String kinds = commonVO.getCondition().getKinds();
        List<Map<String, Object>> list = blogRepository.findHotArticle(kinds);
        List<BlogDTO> blogDTOS = new ArrayList<>();
        list.forEach(item -> {
            try {
                BlogDTO blogDTO = (BlogDTO) MapConvertEntityUtil.mapToEntity(BlogDTO.class, item);
                blogDTOS.add(blogDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        commonDTO.setData(blogDTOS);
        return commonDTO;
    }

    @Override
    public CommonDTO<BlogDTO> getHighlightArticle(CommonVO<BlogVO> commonVO) {
        CommonDTO<BlogDTO> commonDTO = new CommonDTO<>();
        String content = commonVO.getCondition().getContent();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        Sort sort = Sort.by(Sort.Direction.DESC, "update_time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        List<Map<String, Object>> list = blogRepository.findByLikeContentNative(content, pageable);
        List<BlogDTO> blogDTOS = new ArrayList<>();
        list.forEach(item -> {
            String id = (String) item.get("id");
            String title = (String) item.get("title");
            String author = (String) item.get("author");
            Date updateTime = (Date) item.get("updateTime");
            String txt = (String) item.get("content");
            String userId = (String) item.get("userId");
            List<String> searchResult = this.matchPattern(txt, content);
            BlogDTO blogDTO = BlogDTO.builder().id(id).title(title).author(author).updateTime(updateTime).content(txt).searchResult(searchResult).userId(userId).build();
            blogDTOS.add(blogDTO);
        });
        commonDTO.setData(blogDTOS);
        commonDTO.setTotal((long) blogDTOS.size());
        return commonDTO;
    }

    @Override
    public Long statisticLabel(CommonVO<LabelVO> vo) {
        String labelName = vo.getCondition().getLabelName();
        Long articleTotal = blogRepository.countAllByKinds(labelName);
        return articleTotal;
    }

    private List<String> matchPattern(String content, String regex) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        int len = content.length();
        while (matcher.find()) {
            StringBuilder stringBuilder = new StringBuilder();
            int start = matcher.start();
            int end = matcher.end();
            int startBefore, endAfter;
            if (start > 8) {
                startBefore = start - 8;
            } else {
                startBefore = 0;
            }
            if (end + 8 < len) {
                endAfter = end + 8;
            } else {
                endAfter = len;
            }
            String text1 = content.substring(startBefore, start);
            String text2 = "<span style='color: #ffa500;font-weight: bold;font-size: 16px;'>" + matcher.group() + "</span>";
            String text3 = content.substring(end + 1, endAfter);
            result.add(stringBuilder.append("...").append(text1).append(text2).append(text3).append("...").toString());
        }
        return result;
    }

    @Override
    public <T> CommonDTO<T> deleteWrittenBlog(CommonVO<BlogVO> vo) {
        CommonDTO<T> commonDTO = new CommonDTO<>();
        String id = vo.getCondition().getId();
        synchronized (this) {
            blogRepository.deleteById(id);
            historyRepository.deleteByArticleId(id);
            likeTagRepository.deleteByArticleId(id);
        }
        return commonDTO;
    }
}