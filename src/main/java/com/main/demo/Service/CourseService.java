package com.main.demo.Service;

import com.main.demo.entity.po.Course;
import com.main.demo.query.CourseQuery;

import java.util.List;

public interface CourseService {
    /**
     * 根据条件来查询Course
     * @param courseQuery
     * @return
     */
    List<Course> query(CourseQuery courseQuery);

    List<Course> queryAll();
}
