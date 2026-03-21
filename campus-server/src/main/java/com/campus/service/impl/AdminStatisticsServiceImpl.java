package com.campus.service.impl;

import com.campus.Result.Result;
import com.campus.entity.Resource;
import com.campus.mapper.AdminStatisticsMapper;
import com.campus.service.AdminStatisticsService;
import com.campus.vo.RankingItemVO;
import com.campus.vo.StatisticsOverviewVO;
import com.campus.vo.topResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminStatisticsServiceImpl implements AdminStatisticsService {
    @Autowired
    AdminStatisticsMapper adminStatisticsMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public StatisticsOverviewVO overview(String days) {
        int count=Integer.parseInt(days);
        List<LocalDate> trendDates=new ArrayList<>();
        List<Integer> trendValues=new ArrayList<>();
        LocalDate end=LocalDate.now();
        LocalDate start=end.minusDays(count);
        Long cache = stringRedisTemplate.opsForZSet().size("overview:"+start+"-"+end);
        if(cache==null||cache==0)
        {
            List<RankingItemVO> rankingItemVOS = adminStatisticsMapper.rankingList(start, end);
            for(int i=0;i<Math.min(rankingItemVOS.size(),5);i++)
            {
                stringRedisTemplate.opsForZSet().add("overview:"+start+"-"+end,rankingItemVOS.get(i).getName(),rankingItemVOS.get(i).getCount());
                stringRedisTemplate.expire("overview:"+start+"-"+end,1, TimeUnit.DAYS);
            }
        }
        List<RankingItemVO> rankingItemVOS = stringRedisTemplate.opsForZSet().reverseRangeWithScores("overview:"+start+"-"+end,0,4).stream().map(item->new RankingItemVO(item.getValue(),item.getScore().intValue())).collect(Collectors.toList());
        LocalDateTime startTime = LocalDateTime.of(start, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MIN);
        Integer totalStudents=adminStatisticsMapper.totalStudents();
        Integer poorCreditStudents=adminStatisticsMapper.poorCreditStudents();
        Integer appointmentCount=adminStatisticsMapper.appointmentCount(start,end);
        topResource topresource = adminStatisticsMapper.topResourceId(start, end);
        while(start.isBefore(end))
        {
            trendDates.add(start);
            start=start.plusDays(1);
        }
        String topResourceName="暂无";
        if(topresource!=null)
        {
            if(topresource.getType().equals("RESOURCE"))
            {
                topResourceName = adminStatisticsMapper.topResourceName(topresource.getResourceId());
            }
            else
            {
                topResourceName = adminStatisticsMapper.topVenueName(topresource.getResourceId());
            }
        }
        for (LocalDate trendDate : trendDates) {
            Integer i = adminStatisticsMapper.trendValues(trendDate);
            trendValues.add(i == null ? 0 : i);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");
        List<String> dateS = trendDates.stream().map(date -> date.format(formatter)).collect(Collectors.toList());
        return StatisticsOverviewVO.builder()
                .appointmentCount(appointmentCount)
                .topResourceName(topResourceName)
                .totalStudents(totalStudents)
                .poorCreditStudents(poorCreditStudents)
                .trendDates(dateS)
                .trendValues(trendValues)
                .rankingList(rankingItemVOS).build();

    }
}
