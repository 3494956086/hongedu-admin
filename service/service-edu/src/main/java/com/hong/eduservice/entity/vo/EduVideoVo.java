package com.hong.eduservice.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class EduVideoVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String title;
    private String videoSourceId;
    private Boolean isFree;
}
