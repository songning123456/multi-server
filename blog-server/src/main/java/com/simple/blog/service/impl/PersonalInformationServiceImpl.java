package com.simple.blog.service.impl;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.simple.blog.constant.CommonConstant;
import com.simple.blog.dto.PersonalInformationDTO;
import com.simple.blog.entity.PersonalInformation;
import com.simple.blog.jpql.JpqlDao;
import com.simple.blog.repository.PersonalInformationRepository;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.service.PersonalInformationService;
import com.sn.common.util.ClassConvertUtil;
import com.sn.common.util.DateUtil;
import com.simple.blog.util.HttpServletRequestUtil;
import com.sn.common.util.MapConvertEntityUtil;
import com.simple.blog.vo.PersonalInformationVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by songning on 2019/8/25 2:50 PM
 */
@Service
public class PersonalInformationServiceImpl implements PersonalInformationService {

    @Autowired
    private PersonalInformationRepository personalInformationRepository;

    @Autowired
    private HttpServletRequestUtil httpServletRequestUtil;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private JpqlDao jpqlDao;

    @Override
    public <T> CommonDTO<T> savePersonalInfo(CommonVO<List<PersonalInformationVO>> commonVO) {
        List<PersonalInformationVO> list = commonVO.getCondition();
        list.forEach(item -> {
            String infoType = item.getInfoType();
            String mechanism = item.getMechanism();
            String position = item.getPosition();
            String introduction = item.getIntroduction();
            String startTime = item.getStartTime();
            String endTime = item.getEndTime();
            Timestamp startTimeStamp = DateUtil.strToSqlDate(startTime, CommonConstant.YEAR_DATETIME_PATTERN);
            Timestamp endTimeStamp = DateUtil.strToSqlDate(endTime, CommonConstant.YEAR_DATETIME_PATTERN);
            PersonalInformation personalInformation = PersonalInformation.builder().infoType(infoType).mechanism(mechanism).position(position).introduction(introduction).startTime(startTimeStamp).endTime(endTimeStamp).build();
            personalInformationRepository.save(personalInformation);
        });
        return new CommonDTO<>();
    }

    @Override
    public CommonDTO<PersonalInformationDTO> addMyInfo(CommonVO<PersonalInformationVO> commonVO) {
        CommonDTO<PersonalInformationDTO> commonDTO = new CommonDTO<>();
        PersonalInformationVO vo = commonVO.getCondition();
        String username = httpServletRequestUtil.getUsername();
        String userId = usersRepository.findUserIdByNameNative(username);
        String infoType = vo.getInfoType();
        String mechanism = vo.getMechanism();
        String position = vo.getPosition();
        String introduction = vo.getIntroduction();
        String startTime = vo.getStartTime();
        String endTime = vo.getEndTime();
        Timestamp startTimeStamp = DateUtil.strToSqlDate(startTime, CommonConstant.YEAR_DATETIME_PATTERN);
        Timestamp endTimeStamp = DateUtil.strToSqlDate(endTime, CommonConstant.YEAR_DATETIME_PATTERN);
        PersonalInformation personalInformation = PersonalInformation.builder().username(username).userId(userId).infoType(infoType).mechanism(mechanism).position(position).introduction(introduction).startTime(startTimeStamp).endTime(endTimeStamp).updateTime(new Date()).build();
        personalInformationRepository.save(personalInformation);
        return commonDTO;
    }

    @Override
    public CommonDTO<PersonalInformationDTO> getPersonalInfo(CommonVO<PersonalInformationVO> commonVO) {
        CommonDTO<PersonalInformationDTO> commonDTO = new CommonDTO<>();
        String userId = commonVO.getCondition().getUserId();
        List<String> types = personalInformationRepository.findInfoTypeByInfoOwnerNative(userId);
        List<PersonalInformationDTO> result = new ArrayList<>();
        types.forEach(type -> {
            PersonalInformationDTO personalInformationDTO = new PersonalInformationDTO();
            List<Map<String, Object>> personalInformations = personalInformationRepository.findByUserIdAndInfoTypeNative(userId, type);
            List<Map<String, Object>> single = new ArrayList<>();
            personalInformations.forEach(item -> {
                Map<String, Object> map = new HashMap<>(10);
                Timestamp startTime = (Timestamp) item.get("startTime");
                Timestamp endTime = (Timestamp) item.get("endTime");
                map.put("startTime", DateUtil.dateToStr(startTime, CommonConstant.YEAR_DATETIME_PATTERN));
                map.put("endTime", DateUtil.dateToStr(endTime, CommonConstant.YEAR_DATETIME_PATTERN));
                map.put("mechanism", item.get("mechanism"));
                map.put("position", item.get("position"));
                map.put("introduction", item.get("introduction"));
                single.add(map);
            });
            personalInformationDTO.setType(type);
            personalInformationDTO.setInfo(single);
            result.add(personalInformationDTO);
        });
        commonDTO.setData(result);
        commonDTO.setTotal((long) types.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<PersonalInformationDTO> getMyInfo(CommonVO<PersonalInformationVO> commonVO) {
        CommonDTO<PersonalInformationDTO> commonDTO = new CommonDTO<>();
        String username = httpServletRequestUtil.getUsername();
        List<Map<String, Object>> infos = personalInformationRepository.findByUsernameNative(username);
        PersonalInformationDTO dto;
        List<PersonalInformationDTO> result = new ArrayList<>();
        for (Map<String, Object> info : infos) {
            try {
                dto = (PersonalInformationDTO) MapConvertEntityUtil.mapToEntity(PersonalInformationDTO.class, info);
                result.add(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        commonDTO.setData(result);
        commonDTO.setTotal((long) result.size());
        return commonDTO;
    }

    @Override
    public CommonDTO<PersonalInformationDTO> updateMyInfo(CommonVO<PersonalInformationVO> commonVO) {
        CommonDTO<PersonalInformationDTO> commonDTO = new CommonDTO<>();
        PersonalInformationVO vo = commonVO.getCondition();
        String username = httpServletRequestUtil.getUsername();
        Map<String, Object> params = new HashMap<>(2);
        try {
            params = MapConvertEntityUtil.EntityToMap(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(vo.getStartTime())) {
            Timestamp startTimeStamp = DateUtil.strToSqlDate(vo.getStartTime(), CommonConstant.YEAR_DATETIME_PATTERN);
            params.put("startTime", startTimeStamp);
        }
        if (!StringUtils.isEmpty(vo.getEndTime())) {
            Timestamp endTimeStamp = DateUtil.strToSqlDate(vo.getEndTime(), CommonConstant.YEAR_DATETIME_PATTERN);
            params.put("endTime", endTimeStamp);
        }
        params.put("id", vo.getInfoId());
        params.put("updateTime", new Date());
        jpqlDao.update("personalInformationJPQL.updatePersonalInformation", params);
        List<Map<String, Object>> infos = personalInformationRepository.findByUsernameNative(username);
        PersonalInformationDTO dto;
        List<PersonalInformationDTO> result = new ArrayList<>();
        for (Map<String, Object> info : infos) {
            try {
                dto = (PersonalInformationDTO) MapConvertEntityUtil.mapToEntity(PersonalInformationDTO.class, info);
                result.add(dto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        commonDTO.setData(result);
        commonDTO.setTotal((long) result.size());
        return commonDTO;
    }
}
