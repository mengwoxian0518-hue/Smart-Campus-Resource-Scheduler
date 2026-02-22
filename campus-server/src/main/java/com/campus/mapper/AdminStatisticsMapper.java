package com.campus.mapper;

import com.campus.vo.RankingItemVO;
import com.campus.vo.topResource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.Rank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface AdminStatisticsMapper {
    @Select("select count(*) from user")
    Integer totalStudents();
    @Select("select count(*) from user as u left join user_detail as d on u.id = d.user_id where d.credit_score < 60")
    Integer poorCreditStudents();

    Integer appointmentCount(LocalDate start, LocalDate end);
    topResource topResourceId(LocalDate start, LocalDate end);
    @Select("select name from resource where id = #{resourceId}")
    String topResourceName(Integer resourceId);
    @Select("select name from facility where id = #{resourceId}")
    String topVenueName(Integer resourceId);
    @Select("select count(*) from appointment where appoint_date=#{day}")
    Integer trendValues(LocalDate day);
    List<RankingItemVO> rankingList(LocalDate start, LocalDate end);
}
