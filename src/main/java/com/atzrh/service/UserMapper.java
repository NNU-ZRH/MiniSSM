package com.atzrh.service;

import com.atzrh.pojo.User;
import com.ssm.mapper.Mapper;
import com.ssm.mapper.Select;

/**
 * @author zrh
 * @version 1.0.0
 * @title UserMapper
 * @description <TODO description class purpose>
 * @create 2023/11/6 15:20
 **/
@Mapper
public interface UserMapper {
    @Select("select * from name_info where id = 1")
    User queryById(int id);
}
