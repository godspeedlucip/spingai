package com.main.demo.Service.Impl;

import com.main.demo.Mapper.CourseReservationMapper;
import com.main.demo.Service.CourseReservationService;
import com.main.demo.entity.po.CourseReservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseReservationServiceImpl implements CourseReservationService {
    @Autowired
    private CourseReservationMapper courseReservationMapper;

    /**
     * 生成预约单并返返回单号
     *
     * @param courseReservation
     * @return
     */
    @Override
    public void add(CourseReservation courseReservation){
        courseReservationMapper.add(courseReservation);
    }
}
