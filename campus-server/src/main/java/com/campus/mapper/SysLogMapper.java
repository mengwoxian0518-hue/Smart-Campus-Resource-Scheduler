package com.campus.mapper;

import com.campus.entity.SysLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface SysLogMapper {

    /**
     * 插入一条操作日志
     * (注意：前提是你的 application.yml 开启了驼峰命名映射 map-underscore-to-camel-case: true)
     */
    @Insert("INSERT INTO sys_log (operator_id, module, action, method_name, params, ip, execution_time, create_time) " +
            "VALUES (#{operatorId}, #{module}, #{action}, #{methodName}, #{params}, #{ip}, #{executionTime}, #{createTime})")
    void insert(SysLog sysLog);
    @Select("SELECT * FROM sys_log WHERE create_time >= #{startDate} ORDER BY create_time DESC")
    List<SysLog> selectLogsSince(@Param("startDate") LocalDate startDate);

}