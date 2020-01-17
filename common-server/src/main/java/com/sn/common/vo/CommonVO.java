package com.sn.common.vo;

import lombok.Data;

/**
 * @param <T>
 * @author sn
 * 公共vo 层
 */
@Data
public class CommonVO<T> {
    /**
     * 指定第一页开始记录号
     */
    private Integer recordStartNo;

    /**
     * 每页记录数
     */
    private Integer pageRecordNum;

    private T condition;
}
