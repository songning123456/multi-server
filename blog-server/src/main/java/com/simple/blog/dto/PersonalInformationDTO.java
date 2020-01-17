package com.simple.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * Created by songning on 2019/8/25 2:46 PM
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PersonalInformationDTO {
    // 以json格式返回
    private String type;

    private List info;

    // 数据库字段返回
    private String infoId;

    private String infoType;

    private Date startTime;

    private Date endTime;

    private String mechanism;

    private String introduction;

    private String position;
}
