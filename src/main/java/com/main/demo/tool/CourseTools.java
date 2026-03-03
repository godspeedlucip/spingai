package com.main.demo.tool;

import com.main.demo.Service.CourseReservationService;
import com.main.demo.Service.CourseService;
import com.main.demo.Service.SchoolService;
import com.main.demo.entity.po.Course;
import com.main.demo.entity.po.CourseReservation;
import com.main.demo.entity.po.School;
import com.main.demo.query.CourseQuery;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CourseTools {
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseReservationService courseReservationService;

    @Autowired
    private SchoolService schoolService;

    @Tool(description = "给定查询条件来查询所有课程")
    public List<Course> queryCourser(@ToolParam(description = "查询课程所需的条件", required = false) CourseQuery courseQuery){
        if(courseQuery==null){
            // 没给条件，就返所有的课程
            return courseService.queryAll();
        }
        // 根据条件来查询course
        List<Course> queryCourses = courseService.query(courseQuery);
        return queryCourses;
    }

    @Tool(description = "查询所有校区")
    public List<School> querySchool(){
        return schoolService.queryAll();
    }

    @Tool(description = "生成课程预约单")
    public String genCourseReservation(@ToolParam(description = "预约课程名称") String courseName,
                                @ToolParam(description = "学生姓名") String studentName,
                                @ToolParam(description = "联系电话") String contactInfo,
                                @ToolParam(description = "校区")String school,
                                @ToolParam(description = "备注", required = false) String remark){
//        CourseReservation courseReservation = CourseReservation.builder()
//                .course(courseName)
//                .studentName(studentName)
//                .contactInfo(contactInfo)
//                .school(school)
//                .remark(remark)
//                .build();
        CourseReservation courseReservation = new CourseReservation();
        courseReservation.setSchool(school);
        courseReservation.setCourse(courseName);
        courseReservation.setContactInfo(contactInfo);
        courseReservation.setRemark(remark);
        int id = courseReservationService.add(courseReservation);
        return String.valueOf(id);
    }

}
