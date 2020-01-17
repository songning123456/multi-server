package com.simple.blog.service;

import com.simple.blog.dto.MailDTO;
import com.simple.blog.vo.MailVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author songning on 2019/9/14 3:29 PM
 */
public interface MailService {

    /**
     * @param commonVO
     * @throws Exception
     */
    CommonDTO<MailDTO> sendMail(CommonVO<MailVO> commonVO) throws Exception;

    CommonDTO<MailDTO> saveDraft(CommonVO<MailVO> commonVO);
}
