package com.main.demo.Mapper;

import com.main.demo.entity.po.Course;
import com.main.demo.query.CourseQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CourseMapper {

    List<Course> query(CourseQuery courseQuery);

    List<Course> queryAll();
}
