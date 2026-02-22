package com.campus.service;

import com.campus.Result.Result;
import com.campus.vo.StatisticsOverviewVO;

public interface AdminStatisticsService {
    StatisticsOverviewVO overview(String days);

}
