package com.simple.blog.vo;

import com.sn.common.annotation.ClassConvertIgnore;
import lombok.Data;

/**
 * Created by songning on 2019/8/25 2:43 PM
 */
@Data
public class PersonalInformationVO {
    private String infoId;

    private String infoType;

    private String mechanism;

    private String position;

    private String introduction;

    @ClassConvertIgnore
    private String startTime;

    @ClassConvertIgnore
    private String endTime;

    private String userId;
}
