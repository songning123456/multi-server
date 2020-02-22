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

    /**
     * 微聊 特殊处理查询
     * 按照时间正序查询，首先统计总数，判断总数是否大于pageRecordNum,如果>= 先统计最后最后两页的
     *
     * @param commonVO
     * @return
     */
    @Override
    public CommonDTO<WechatDialogDTO> getDialog(CommonVO<WechatDialogVO> commonVO) {
        CommonDTO<WechatDialogDTO> commonDTO = new CommonDTO<>();
        Integer recordStartNo = commonVO.getRecordStartNo();
        Integer pageRecordNum = commonVO.getPageRecordNum();
        List<WechatDialog> src = new ArrayList<>();
        if (recordStartNo != null) {
            // 向上滚动 加载数据
            int offset = (recordStartNo - 1) * pageRecordNum;
            src = wechatDialogRepository.findLimitNative(offset, pageRecordNum);
        } else {
            // 初始化时加载数据
            long total = wechatDialogRepository.count();
            if (total > 0) {
                if (total < pageRecordNum) {
                    src = wechatDialogRepository.findLimitNative(0, (int) total);
                } else {
                    int offset = Long.valueOf((total / pageRecordNum) - 1).intValue() * pageRecordNum;
                    int size = Long.valueOf(total % pageRecordNum).intValue() + pageRecordNum;
                    src = wechatDialogRepository.findLimitNative(offset, size);
                }
            }
            commonDTO.setTotal(total);
        }
        List<WechatDialogDTO> target = new ArrayList<>();
        ClassConvertUtil.populateList(src, target, WechatDialogDTO.class);
        commonDTO.setData(target);
        return commonDTO;
    }
}
