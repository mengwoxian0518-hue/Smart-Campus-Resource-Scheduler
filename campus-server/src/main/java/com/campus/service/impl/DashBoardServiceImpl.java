package com.campus.service.impl;

import cn.hutool.core.date.DateUtil;
import com.campus.entity.Appointment;
import com.campus.mapper.DashBoardMapper;
import com.campus.mapper.UserAppointmentMapper;
import com.campus.service.DashBoardService;
import com.campus.vo.LogVO;
import com.campus.vo.OverViewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    DashBoardMapper dashBoardMapper;
    @Override
    public OverViewVO overview() {
        Integer pendingReview = dashBoardMapper.overviewPending();
        Integer totalResources = dashBoardMapper.overviewTotalResources();
        LocalDate date = LocalDate.now();
        LocalTime now = LocalTime.now();
        Integer inUse = dashBoardMapper.overviewInUse(date,now);
        Integer underRepair = dashBoardMapper.overviewUnderRepair();
        return OverViewVO.builder()
                .pendingReview(pendingReview)
                .totalResources(totalResources)
                .inUse(inUse)
                .underRepair(underRepair)
                .build();
    }

    @Override
    public List<LogVO> logs() {
        List<Appointment> appointments = dashBoardMapper.getAppointment();
        List<LogVO> logs = new ArrayList<>();
        for (Appointment appointment : appointments) {
            LogVO logVO = new LogVO();
            logVO.setId(appointment.getId());
            //获取用户名字
            String name=dashBoardMapper.getUserName(appointment.getUserId());
            //获取资源或者场地名字
            String resourceName = dashBoardMapper.getResourceName(appointment.getResourceId(), appointment.getType());
            LocalDateTime updateTime = appointment.getUpdateTime();
            String timeAgo = "";
            if (updateTime != null) {
                long minutes = Duration.between(updateTime, LocalDateTime.now()).toMinutes();

                if (minutes < 1) {
                    timeAgo = "刚刚";
                } else if (minutes < 60) {
                    timeAgo = minutes + "分钟前";
                } else if (minutes < 1440) {
                    timeAgo = (minutes / 60) + "小时前";
                } else {
                    timeAgo = (minutes / 1440) + "天前";
                }
            }
            logVO.setTime(timeAgo);
            switch (appointment.getStatus()) {
                case 0:
                    logVO.setContent(name+" 提交了 "+resourceName+" 的预约申请");
                    logVO.setType("primary");
                    break;
                case 1:
                    logVO.setContent(name+" 预约 "+resourceName+" 成功");
                    logVO.setType("success");
                    break;
                case 2:
                    logVO.setContent("驳回了 "+name+" 的 "+resourceName+" 预约申请");
                    logVO.setType("danger");
                    break;
                case 3:
                    logVO.setContent(name+" 取消了 "+resourceName+" 的预约申请");
                    logVO.setType("info");
                    break;
                default:
                    logVO.setContent("系统错误");
                    break;
             }
             logs.add(logVO);
        }
        return logs;
    }
}
