package com.campus.mapper;

import com.campus.Type.OperationType;
import com.campus.annotation.AutoFill;
import com.campus.dto.FacilityPageQueryDto;
import com.campus.entity.Facility;
import com.campus.entity.Resource;
import com.campus.vo.FacilityVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FacilityMapper {
    @Select("select * from facility")
    List<Facility> list();
    List<FacilityVO> pageList(FacilityPageQueryDto page);
    @Select("select * from facility where id=#{id}")
    Facility getById(Long id);
    @AutoFill(value = OperationType.INSERT)
    void add(Facility facility);
    @AutoFill(value = OperationType.UPDATE)
    void update(Facility facility);
    void delete(List<Long> ids);
    @Select("select * from resource where facility_id=#{id}")
    List<Resource> searchResource(Long id);
    @Update("update facility set status=#{status} where id=#{id}")
    void startOrStop(Integer status,Long id);
}
