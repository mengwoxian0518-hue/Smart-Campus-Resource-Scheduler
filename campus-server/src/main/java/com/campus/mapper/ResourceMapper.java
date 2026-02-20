package com.campus.mapper;

import com.campus.Type.OperationType;
import com.campus.annotation.AutoFill;
import com.campus.dto.ResourcePageQueryDTO;
import com.campus.entity.Resource;
import com.campus.vo.ResourceVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ResourceMapper {
    List<ResourceVO> pageList(ResourcePageQueryDTO resourcePageQueryDTO);
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into resource(name,category_id,facility_id,credit_cost,image,model,asset_code,description,status,create_time,update_time,create_user,update_user) values(#{name},#{categoryId},#{facilityId},#{creditCost},#{image},#{model},#{assetCode},#{description},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void add(Resource resource);
    @AutoFill(value = OperationType.UPDATE)
    @Update("update resource set name=#{name},category_id=#{categoryId},facility_id=#{facilityId},credit_cost=#{creditCost},image=#{image},model=#{model},asset_code=#{assetCode},description=#{description},status=#{status},update_time=#{updateTime},update_user=#{updateUser} where id=#{id}")
    void update(Resource resource);
    void delete(List<Long> id);
    @Select("select * from resource where id=#{id}")
    Resource getById(Long id);
    @Update("update resource set status=#{status} where id=#{id}")
    void startOrStop(Integer status, Long id);
    List<Resource> list(Long id);
}
