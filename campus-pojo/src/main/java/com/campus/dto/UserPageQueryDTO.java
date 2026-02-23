package com.campus.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserPageQueryDTO implements Serializable {

    // 分页参数 (前端传 page, pageSize)
    private int page=1;
    private int pageSize=10;

    // 查询条件
    private String realName;     // 真实姓名 (模糊查询)
    private String studentId;    // 学号 (模糊或精确查询)
    
    //GOOD or POOR
    private String creditStatus; 

}