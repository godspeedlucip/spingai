package com.main.demo.Service;

import com.main.demo.entity.po.CourseReservation;

public interface CourseReservationService {
    /**
     * 生成预约单并返返回单号
     *
     * @param courseReservation
     * @return
     */
    void add(CourseReservation courseReservation);
}


