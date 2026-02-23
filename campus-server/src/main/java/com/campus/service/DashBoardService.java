package com.campus.service;

import com.campus.vo.LogVO;
import com.campus.vo.OverViewVO;

import java.util.List;

public interface DashBoardService {
    OverViewVO overview();

    List<LogVO> logs();
}
