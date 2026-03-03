package com.main.demo.Service;

import com.main.demo.entity.po.School;

import java.util.List;

public interface SchoolService {

    /**
     * 查询所有校区
     * * @return
     */
    List<School> queryAll();
}
