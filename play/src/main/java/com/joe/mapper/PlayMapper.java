package com.joe.mapper;

import com.joe.entity.Play;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlayMapper {

    int save(Play play);

    int update(Play play);

}
