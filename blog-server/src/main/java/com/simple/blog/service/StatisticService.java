package com.simple.blog.service;

import com.simple.blog.dto.StatisticDTO;
import com.simple.blog.vo.StatisticVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author songning
 * @date 2019/8/27
 * description
 */
public interface StatisticService {

    /**
     * 获取统计结果
     *
     * @param commonVO
     * @return
     */
    CommonDTO<StatisticDTO> getStatisticResult(CommonVO<StatisticVO> commonVO);
}
