package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 数据统计 - 综合大盘 VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticsOverviewVO implements Serializable {

    // ================= 1. 顶部核心指标卡片 =================
    
    // 区间内的预约总数
    private Integer appointmentCount;
    
    // 区间内借用频次最高的资源名称
    private String topResourceName;
    
    // 平台注册的学生总数
    private Integer totalStudents;
    
    // 信用分较差（如低于60分）的学生人数
    private Integer poorCreditStudents;


    // ================= 2. 折线图数据 (ECharts) =================
    
    // X轴：日期列表 (格式建议："MM-dd"，如 "02-15", "02-16")
    private List<String> trendDates;
    
    // Y轴：每日对应的预约量列表 (严格与 trendDates 长度和顺序一一对应)
    private List<Integer> trendValues;


    // ================= 3. 排行榜数据 =================
    
    // 右侧的热门预约排行榜列表 (按次数降序，通常取前 5 名)
    private List<RankingItemVO> rankingList;

}