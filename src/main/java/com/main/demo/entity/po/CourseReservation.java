package com.main.demo.entity.po;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
//@Builder
public class CourseReservation implements Serializable {

    private Integer id;

    /**
     * 预约课程
     */
    private String course;

    /**
     * 学生姓名
     */
    private String studentName;

    /**
     * 联系方式
     */
    private String contactInfo;

    /**
     * 预约校区
     */
    private String school;

    /**
     * 备注
     */
    private String remark;



}