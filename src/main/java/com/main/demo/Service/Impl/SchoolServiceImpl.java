package com.main.demo.Service.Impl;

import com.main.demo.Mapper.SchoolMapper;
import com.main.demo.Service.SchoolService;
import com.main.demo.entity.po.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private SchoolMapper schoolMapper;

    /**
     * 查询所有校区
     * * @return
     */
    public List<School> queryAll(){
        return schoolMapper.queryAll();
    }
}
