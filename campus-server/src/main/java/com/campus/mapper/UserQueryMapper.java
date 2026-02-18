package com.campus.mapper;

import com.campus.vo.CategoryVO;
import com.campus.vo.UserQueryVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserQueryMapper {
    @Select("select id,name from category where type=#{type}")
    List<UserQueryVO> getResource(Integer type);
    @Select("select id,name from category where type=#{type}")
    List<UserQueryVO> getFacility(Integer type);
    @Select("select * from resource where category_id=#{id}")
    List<CategoryVO> getResourceList(Long id);
    @Select("select * from facility where category_id=#{id}")
    List<CategoryVO> getFacilityList(Long id);
    @Select("select * from resource where id=#{id}")
    CategoryVO getDetailByResource(Long id);
    @Select("select * from facility where id= #{id}")
    CategoryVO getDetailByVenue(Long id);
}
