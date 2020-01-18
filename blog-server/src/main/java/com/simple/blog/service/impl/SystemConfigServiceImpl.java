package com.simple.blog.service.impl;

import com.simple.blog.constant.CommonConstant;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

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
        Sort sort = Sort.by(Sort.Direction.ASC, "configKey");
        Pageable pageable = PageRequest.of(recordStartNo, pageRecordNum, sort);
        Specification specification = (Specification) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (!StringUtils.isEmpty(configKey)) {
                predicateList.add(criteriaBuilder.like(root.get("configKey"), "%" + configKey + "%"));
            }
            if (!StringUtils.isEmpty(configValue)) {
                predicateList.add(criteriaBuilder.like(root.get("configValue"), "%" + configValue + "%"));
            }
            if (!StringUtils.isEmpty(valueDescription)) {
                predicateList.add(criteriaBuilder.like(root.get("valueDescription"), "%" + valueDescription + "%"));
            }
            predicateList.add(criteriaBuilder.equal(root.get("username"), username));
            return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
        };
        Page<SystemConfig> systemConfigPage = systemConfigRepository.findAll(specification, pageable);
        List<SystemConfig> systemConfigList = systemConfigPage.getContent();
        List<SystemConfigDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(systemConfigList, target, SystemConfigDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal((long) systemConfigList.size());
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
