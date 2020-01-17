package com.simple.blog.service;

import com.simple.blog.dto.PersonalInformationDTO;
import com.simple.blog.vo.PersonalInformationVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

import java.util.List;

/**
 * Created by songning on 2019/8/25 2:50 PM
 */
public interface PersonalInformationService {

    /**
     * 插入个人信息
     *
     * @param commonVO
     * @return
     */
    <T> CommonDTO<T> savePersonalInfo(CommonVO<List<PersonalInformationVO>> commonVO);

    /**
     * 插入单条个人信息
     *
     * @param commonVO
     * @return
     */
    CommonDTO<PersonalInformationDTO> addMyInfo(CommonVO<PersonalInformationVO> commonVO);

    /**
     * 他人获取作者信息
     *
     * @param commonVO
     * @return
     */
    CommonDTO<PersonalInformationDTO> getPersonalInfo(CommonVO<PersonalInformationVO> commonVO);

    CommonDTO<PersonalInformationDTO> getMyInfo(CommonVO<PersonalInformationVO> commonVO);

    CommonDTO<PersonalInformationDTO> updateMyInfo(CommonVO<PersonalInformationVO> commonVO);
}
