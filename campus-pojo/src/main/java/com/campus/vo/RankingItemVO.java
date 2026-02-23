package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 数据统计 - 排行榜单项 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingItemVO implements Serializable {
    
    // 资源或场地名称
    private String name;
    
    // 借用次数
    private Integer count;
    
}