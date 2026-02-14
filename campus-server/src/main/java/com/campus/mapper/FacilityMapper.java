package com.campus.mapper;

import com.campus.entity.Facility;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FacilityMapper {
    @Select("select * from facility")
    List<Facility> list();
}
