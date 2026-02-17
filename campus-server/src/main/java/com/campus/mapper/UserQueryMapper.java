package com.campus.mapper;

import com.campus.vo.CategoryVO;
import com.campus.vo.UserQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserQueryMapper {
    @Select("select id,name from resource")
    List<UserQueryVO> getResource();
    @Select("select id,name from facility")
    List<UserQueryVO> getFacility();
    @Select("select * from resource where category_id=#{id}")
    List<CategoryVO> getResourceList(Long id);
    @Select("select * from facility where category_id= #{id}")
    List<CategoryVO> getFacilityList(Long id);
    @Select("select * from resource where id=#{id}")
    CategoryVO getDetailByResource(Long id);
    @Select("select * from facility where id= #{id}")
    CategoryVO getDetailByVenue(Long id);
}
