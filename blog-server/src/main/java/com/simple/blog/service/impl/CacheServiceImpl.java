package com.simple.blog.service.impl;

import com.simple.blog.constant.CommonConstant;
import com.simple.blog.dto.LabelDTO;
import com.simple.blog.entity.LabelConfig;
import com.simple.blog.entity.SystemConfig;
import com.simple.blog.repository.LabelConfigRepository;
import com.simple.blog.repository.LabelRelationRepository;
import com.simple.blog.repository.SystemConfigRepository;
import com.simple.blog.service.CacheService;
import com.simple.blog.service.MemoryService;
import com.simple.blog.util.DataBaseUtil;
import com.simple.blog.util.HttpServletRequestUtil;
import com.simple.blog.vo.LabelVO;
import com.sn.common.vo.CommonVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: songning
 * @date: 2019/11/29 21:11
 */
@Service
@Slf4j
public class CacheServiceImpl implements CacheService {

    @Autowired
    private SystemConfigRepository systemConfigRepository;
    @Autowired
    private LabelConfigRepository labelConfigRepository;
    @Autowired
    private MemoryService memoryService;
    @Autowired
    private DataBaseUtil dataBaseUtil;
    @Autowired
    private LabelRelationRepository labelRelationRepository;
    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;

    @Override
    public void refreshLabelConfig() {
        List<LabelConfig> labelConfigList;
        labelConfigList = labelConfigRepository.findAll();
        Map<String, Object> labelConfigMap = memoryService.getValues(CommonConstant.MEMORY_CACHE, CommonConstant.ALL_LABEL);
        if (!labelConfigMap.isEmpty()) {
            memoryService.deleteValues(CommonConstant.MEMORY_CACHE, CommonConstant.ALL_LABEL);
        }
        this.cacheLabel(labelConfigList);
        log.info("~~~刷新LabelConfig缓存~~~");
    }

    @Override
    public void refreshSystemConfig(String username) {
        Map<String, Object> systemConfigMap = memoryService.getValues(CommonConstant.MEMORY_CACHE + CommonConstant.SYSTEM_CONFIG + username);
        if (!systemConfigMap.isEmpty()) {
            memoryService.deleteValues(CommonConstant.MEMORY_CACHE, CommonConstant.SYSTEM_CONFIG, username);
        }
        List<SystemConfig> systemConfigList = systemConfigRepository.getSystemConfigByUsername(username);
        for (SystemConfig systemConfig : systemConfigList) {
            memoryService.setValue(CommonConstant.MEMORY_CACHE + CommonConstant.SYSTEM_CONFIG + username + ":" + systemConfig.getConfigKey(), systemConfig);
        }
        log.info("~~~user:刷新SystemConfig缓存~~~");
    }

    @Override
    public void refreshPersonAttentionLabel(String username) {
        String person = memoryService.getValue(CommonConstant.MEMORY_CACHE, CommonConstant.PERSON_ATTENTION_LABEL, username).toString();
        if (!StringUtils.isEmpty(person)) {
            memoryService.deleteValue(CommonConstant.MEMORY_CACHE + CommonConstant.PERSON_ATTENTION_LABEL + username);
        }
        List labelNames = labelRelationRepository.findLabelNameByUsernameAndSelectedNative(username, 1);
        memoryService.setValue(CommonConstant.MEMORY_CACHE + CommonConstant.PERSON_ATTENTION_LABEL + username, labelNames);
        log.info("~~~user:刷新PersonAttentionLabel缓存~~~");
    }

    @Override
    public List getPersonAttentionLabelCache() throws Exception {
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new Exception("token无效,请重新登陆");
        }
        Object person = memoryService.getValue(CommonConstant.MEMORY_CACHE + CommonConstant.PERSON_ATTENTION_LABEL + username);
        List labelNames;
        if (StringUtils.isEmpty(person)) {
            labelNames = labelRelationRepository.findLabelNameByUsernameAndSelectedNative(username, 1);
            memoryService.setValue(CommonConstant.MEMORY_CACHE + CommonConstant.PERSON_ATTENTION_LABEL + username, labelNames);
        } else {
            labelNames = (List) person;
        }
        return labelNames;
    }

    @Override
    public Map<String, Object> getLabelConfigCache() {
        Map<String, Object> labelConfigMap = memoryService.getValues(CommonConstant.MEMORY_CACHE + CommonConstant.ALL_LABEL);
        if (labelConfigMap.isEmpty()) {
            List<LabelConfig> labelConfigList = labelConfigRepository.findAll();
            this.cacheLabel(labelConfigList);
            labelConfigMap = memoryService.getValues(CommonConstant.MEMORY_CACHE + CommonConstant.ALL_LABEL);
        }
        return labelConfigMap;
    }

    @Override
    public Map<String, Object> getSystemConfigCache() throws Exception {
        String username = httpServletRequestUtil.getUsername();
        if (StringUtils.isEmpty(username)) {
            throw new Exception("token无效,请重新登陆");
        }
        Map<String, Object> systemConfigMap = memoryService.getValues(CommonConstant.MEMORY_CACHE + CommonConstant.SYSTEM_CONFIG + username);
        if (systemConfigMap.isEmpty()) {
            List<SystemConfig> systemConfigList = systemConfigRepository.getSystemConfigByUsername(username);
            for (SystemConfig systemConfig : systemConfigList) {
                memoryService.setValue(CommonConstant.MEMORY_CACHE + CommonConstant.SYSTEM_CONFIG + username + ":" + systemConfig.getConfigKey(), systemConfig);
            }
            systemConfigMap = memoryService.getValues(CommonConstant.MEMORY_CACHE + CommonConstant.SYSTEM_CONFIG + username);
        }
        return systemConfigMap;
    }

    /**
     * 缓存标签labelConfig到memory
     *
     * @param labelConfigList
     */
    private void cacheLabel(List<LabelConfig> labelConfigList) {
        CommonVO<LabelVO> commonVO = new CommonVO<>();
        LabelVO labelVO = new LabelVO();
        Map<String, Object> countMap;
        for (LabelConfig labelConfig : labelConfigList) {
            // 统计关注数
            countMap = labelRelationRepository.countAttentionNative(labelConfig.getLabelName());
            long numOfAttention = 0L;
            if (!countMap.isEmpty() && countMap.get("total") != null) {
                numOfAttention = ((BigDecimal) countMap.get("total")).longValue();
            }
            // 统计文章总数
            labelVO.setLabelName(labelConfig.getLabelName());
            commonVO.setCondition(labelVO);
            Long numOfArticle = dataBaseUtil.getDataBase().statisticLabel(commonVO);
            // 缓存到memory
            LabelDTO labelDTO = LabelDTO.builder().id(labelConfig.getId()).labelName(labelConfig.getLabelName()).labelPhoto(labelConfig.getLabelPhoto()).numOfArticle(numOfArticle).numOfAttention(numOfAttention).build();
            memoryService.setValue(CommonConstant.MEMORY_CACHE + CommonConstant.ALL_LABEL + labelConfig.getLabelName(), labelDTO);
        }
    }
}
