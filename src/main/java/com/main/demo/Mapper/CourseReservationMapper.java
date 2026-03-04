package com.main.demo.Mapper;

import com.main.demo.entity.po.CourseReservation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CourseReservationMapper {

    /**
     * 生成预约单并返返回单号
     *
     * @param courseReservation
     * @return
     */
    void add(CourseReservation courseReservation);
}
