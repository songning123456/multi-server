package com.sn.common.util.classconvert.service;

import com.sn.common.util.DateUtil;

/**
 * @author songning
 * @date 2020/1/21
 * description
 */
public class StringToDateImpl implements IClassConvert {

    @Override
    public Object classConvert(Object param) {
        return DateUtil.strToDate(param.toString(), "yyyy-MM-dd HH:mm:ss");
    }
}
