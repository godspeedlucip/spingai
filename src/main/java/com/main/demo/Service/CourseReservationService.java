package com.main.demo.Service;

import com.main.demo.entity.po.CourseReservation;
import org.springframework.stereotype.Service;

@Service
public interface CourseReservationService {
    /**
     * 生成预约单并返返回单号
     *
     * @param courseReservation
     * @return
     */
    int add(CourseReservation courseReservation);
}


