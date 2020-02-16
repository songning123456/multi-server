package com.simple.blog.service.impl;

import com.simple.blog.constant.CommonConstant;
import com.simple.blog.entity.Blog;
import com.sn.common.constant.HttpStatus;
import com.simple.blog.dto.HistoryDTO;
import com.simple.blog.entity.History;
import com.simple.blog.repository.BlogRepository;
import com.simple.blog.repository.HistoryRepository;
import com.simple.blog.service.HistoryService;
import com.sn.common.util.ClassConvertUtil;
import com.sn.common.util.CssStyleUtil;
import com.sn.common.util.DateUtil;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.HistoryVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author songning
 * @date 2019/10/22
 * description
 */
@Service
public class HistoryServiceImpl implements HistoryService {

    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Override
    public CommonDTO<HistoryDTO> insertHistory(CommonVO<HistoryVO> commonVO) {
        CommonDTO<HistoryDTO> commonDTO = new CommonDTO<>();
        String title = commonVO.getCondition().getTitle();
        String username;
        if (StringUtils.isEmpty(commonVO.getCondition().getUsername())) {
            username = httpServletRequestUtil.getUsername();
        } else {
            username = commonVO.getCondition().getUsername();
        }
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        String time = DateUtil.dateToStr(new Date(), CommonConstant.DEFAULT_DATETIME_PATTERN);
        String description = CssStyleUtil.boldAndItalicFont(username) + " 提交于 " + CssStyleUtil.boldAndItalicFont(time);
        History history;
        if (CommonConstant.READ_ARTICLE.equals(title)) {
            String articleId = commonVO.getCondition().getArticleId();
            Blog blog = blogRepository.findByIdNative(articleId);
            title = CssStyleUtil.spans(title, " ", blog.getTitle());
            history = History.builder().title(title).articleId(articleId).username(username).time(time).description(description).build();
        } else {
            title = CssStyleUtil.spans(title);
            history = History.builder().title(title).username(username).time(time).description(description).build();
        }
        historyRepository.save(history);
        return commonDTO;
    }

    @Override
    public CommonDTO<HistoryDTO> getHistory(CommonVO<HistoryVO> commonVO) {
        CommonDTO<HistoryDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "time");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Page<History> historyPage = historyRepository.findHistoryNative(username, pageable);
        List<History> list = historyPage.getContent();
        List<HistoryDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(list, target, HistoryDTO.class);
        commonDTO.setTotal((long) target.size());
        commonDTO.setData(target);
        return commonDTO;
    }
}
