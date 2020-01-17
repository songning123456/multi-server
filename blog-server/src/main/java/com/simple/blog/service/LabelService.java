package com.simple.blog.service;


import com.simple.blog.dto.LabelDTO;
import com.simple.blog.vo.LabelVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @Author songning
 * @create 2019/7/31 18:01
 */
public interface LabelService {
    CommonDTO<LabelDTO> getSelectedLabel() throws Exception ;

    CommonDTO<LabelDTO> getAllLabel(CommonVO<LabelVO> vo) throws Exception;

    CommonDTO<LabelDTO> statisticLabel(CommonVO<LabelVO> commonVO);

    CommonDTO<LabelDTO> updateAttention(CommonVO<LabelVO> commonVO)  throws Exception;

    CommonDTO<LabelDTO> getAllLabelConfig();
}
