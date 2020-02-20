package com.simple.blog.service.impl;

import com.simple.blog.dto.WechatDialogDTO;
import com.simple.blog.entity.WechatDialog;
import com.simple.blog.repository.WechatDialogRepository;
import com.simple.blog.service.WechatDialogService;
import com.simple.blog.vo.WechatDialogVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.util.ClassConvertUtil;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: songning
 * @date: 2020/2/20 21:07
 */
@Service
public class WechatDialogServiceImpl implements WechatDialogService {

    @Autowired
    private WechatDialogRepository wechatDialogRepository;

    @Override
    public CommonDTO<WechatDialogDTO> getDialog(CommonVO<WechatDialogVO> commonVO) {
        CommonDTO<WechatDialogDTO> commonDTO = new CommonDTO<>();
        List<WechatDialog> src = wechatDialogRepository.findNative();
        List<WechatDialogDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, WechatDialogDTO.class);
        commonDTO.setData(target);
        commonDTO.setTotal((long) src.size());
        return commonDTO;
    }
}
