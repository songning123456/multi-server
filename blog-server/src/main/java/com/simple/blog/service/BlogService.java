package com.simple.blog.service;

import com.simple.blog.dto.BlogDTO;
import com.simple.blog.vo.BlogVO;
import com.simple.blog.vo.LabelVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;

/**
 * @author sn
 */
public interface BlogService {
    /**
     * 新增文章
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> saveArticle(CommonVO<BlogVO> commonVO);

    /**
     * 查询摘要等信息
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getAbstract(CommonVO<BlogVO> commonVO);

    /**
     * 我所写过的
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getByAuthor(CommonVO<BlogVO> commonVO);

    /**
     * 我所赞过的
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getByLove(CommonVO<BlogVO> commonVO);

    /**
     * 获取文章内容
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getContent(CommonVO<BlogVO> commonVO);

    /**
     * 查询热门文章
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getHotArticle(CommonVO<BlogVO> commonVO);

    /**
     * 高亮搜索文章内容
     *
     * @param commonVO
     * @return
     */
    CommonDTO<BlogDTO> getHighlightArticle(CommonVO<BlogVO> commonVO);

    Long statisticLabel(CommonVO<LabelVO> vo);

    <T> CommonDTO<T> deleteWrittenBlog(CommonVO<BlogVO> vo);
}
