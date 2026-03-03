package com.main.demo.Mapper;

import com.main.demo.entity.po.School;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SchoolMapper {

    /**
     * 查询所有校区
     * @return
     */
    @Select("select * from school")
    List<School> queryAll();
}
