package com.simple.blog.service;

import com.simple.blog.dto.WechatDialogDTO;
import com.simple.blog.vo.WechatDialogVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author: songning
 * @date: 2020/2/20 21:07
 */
public interface WechatDialogService {

    CommonDTO<WechatDialogDTO> getDialog(CommonVO<WechatDialogVO> commonVO);
}
