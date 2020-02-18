package com.simple.blog.service.impl;

import com.simple.blog.constant.CommonConstant;
import com.simple.blog.jpql.JpqlDao;
import com.sn.common.constant.HttpStatus;
import com.simple.blog.dto.SystemConfigDTO;
import com.simple.blog.entity.SystemConfig;
import com.simple.blog.repository.SystemConfigRepository;
import com.simple.blog.service.MemoryService;
import com.simple.blog.service.SystemConfigService;
import com.sn.common.util.ClassConvertUtil;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.SystemConfigVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import com.sn.jpql.JpqlParser;
import com.sn.jpql.ParserParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2019/10/7 12:00
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;

    @Autowired
    private MemoryService memoryService;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;
    @Autowired
    private JpqlParser jpqlParser;
    @Autowired
    private JpqlDao jpqlDao;
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public CommonDTO<SystemConfigDTO> getSystemConfig(CommonVO<SystemConfigVO> commonVO) {
        CommonDTO<SystemConfigDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        String configKey = commonVO.getCondition().getConfigKey();
        String configValue = commonVO.getCondition().getConfigValue();
        String valueDescription = commonVO.getCondition().getValueDescription();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        Map<String, Object> params = new HashMap<>(2);
        params.put("offset", recordStartNo * pageRecordNum);
        params.put("pageRecordNum", pageRecordNum);
        params.put("configKey", configKey);
        params.put("configValue", configValue);
        params.put("valueDescription", valueDescription);
        params.put("username", username);
        List<SystemConfig> src = jpqlDao.query("systemConfigJpql.getSystemConfig", params, SystemConfig.class);
        long total = jpqlDao.count("systemConfigJpql.countSystemConfig", params);
        List<SystemConfigDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, SystemConfigDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal(total);
        return commonDTO;
    }

    @Override
    public <T> CommonDTO<T> updateSystemConfig(CommonVO<SystemConfigVO> commonVO) {
        CommonDTO<T> commonDTO = new CommonDTO<>();
        String configKey = commonVO.getCondition().getConfigKey();
        String configValue = commonVO.getCondition().getConfigValue();
        String valueDescription = commonVO.getCondition().getValueDescription();
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            commonDTO.setStatus(HttpStatus.HTTP_UNAUTHORIZED);
            commonDTO.setMessage("token无效,请重新登陆");
            return commonDTO;
        }
        SystemConfig systemConfig = SystemConfig.builder().configKey(configKey).configValue(configValue).username(username).valueDescription(valueDescription).build();
        systemConfigRepository.updateSystemConfig(username, configKey, configValue, valueDescription);
        memoryService.setValue(CommonConstant.MEMORY_CACHE + CommonConstant.SYSTEM_CONFIG + username + ":" + configKey, systemConfig);
        return new CommonDTO<>();
    }
}
