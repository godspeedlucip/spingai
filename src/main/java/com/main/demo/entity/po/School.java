package com.main.demo.entity.po;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class School implements Serializable {

    /**
     * 主键
     */
    private Integer id;

    /**
     * 校区名称
     */
    private String name;

    /**
     * 校区所在城市
     */
    private String city;



}