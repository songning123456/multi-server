package com.simple.blog.service.impl;

import com.simple.blog.constant.CommonConstant;
import com.sn.common.constant.HttpStatus;
import com.simple.blog.dto.StatisticDTO;
import com.simple.blog.feign.ElasticSearchFeignClient;
import com.simple.blog.repository.BlogRepository;
import com.simple.blog.repository.SystemConfigRepository;
import com.simple.blog.service.StatisticService;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.StatisticVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author songning
 * @date 2019/8/27
 * description
 */
@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private ElasticSearchFeignClient elasticSearchFeignClient;
    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;

    @Override
    public CommonDTO<StatisticDTO> getStatisticResult(CommonVO<StatisticVO> commonVO) {
        CommonDTO<StatisticDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        String dataBase = systemConfigRepository.findConfigValueByUsernameAndConfigKeyNative(username, "dataBase");
        if (CommonConstant.DATABASE_ES.equals(dataBase)) {
            // es 服务
            commonDTO = elasticSearchFeignClient.esStatistic(commonVO);
            return commonDTO;
        } else {
            // 本地mysql
            commonDTO = this.statisticResult(commonVO);
            return commonDTO;
        }

    }

    private CommonDTO<StatisticDTO> statisticResult(CommonVO<StatisticVO> commonVO) {
        CommonDTO<StatisticDTO> commonDTO = new CommonDTO<>();
        String type = commonVO.getCondition().getType();
        String startTime = commonVO.getCondition().getStartTime();
        String endTime = commonVO.getCondition().getEndTime();
        List<Map<String, Object>> list = null;
        if ("kinds".equals(type)) {
            list = blogRepository.statisticKinds(startTime, endTime);
        } else if ("author".equals(type)) {
            list = blogRepository.statisticAuthor(startTime, endTime);
        }
        List<StatisticDTO> statisticDTOList = new ArrayList<>();
        list.forEach(item -> {
            StatisticDTO statisticDTO = new StatisticDTO();
            try {
                statisticDTO.setXAxis((String) item.get("xAxis"));
                statisticDTO.setYAxis(String.valueOf(item.get("yAxis")));
                statisticDTOList.add(statisticDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        commonDTO.setData(statisticDTOList);
        commonDTO.setTotal((long) statisticDTOList.size());
        return commonDTO;
    }

}
