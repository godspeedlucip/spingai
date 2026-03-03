package com.main.demo.Service.Impl;

import com.main.demo.Mapper.CourseMapper;
import com.main.demo.Service.CourseService;
import com.main.demo.entity.po.Course;
import com.main.demo.query.CourseQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseMapper courseMapper;

    /**
     * 根据条件来查询Course
     * @param courseQuery
     * @return
     */
    public List<Course> query(CourseQuery courseQuery){
        return courseMapper.query(courseQuery);
    }

    /**
     * 查找所有课程
     * @return
     */
    public List<Course> queryAll(){
        return courseMapper.queryAll();
    }
}
