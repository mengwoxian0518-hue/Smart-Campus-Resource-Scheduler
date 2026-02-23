package com.campus.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.campus.entity.Appointment;
import com.campus.mapper.AdminAppointmentMapper;
import com.campus.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class AppointmentTask {

    @Autowired
    private AdminAppointmentMapper appointmentMapper;

    @Autowired
    private WebSocketServer webSocketServer;
    @Scheduled(cron = "0 * * * * ?")
    public void checkUrgentAppointments() {
        Integer countPending = appointmentMapper.getCountPending();
        if(countPending>0)
        {log.info("有待处理的预约");
            Map map=new HashMap();
            map.put("type","PENDING_REVIEW");
            map.put("count",countPending);
            map.put("content",String.format("您有%d个待审核的预约申请,请尽快前往处理!",countPending));
            map.put("timestamp",System.currentTimeMillis());
            String jsonString = JSON.toJSONString(map);
            webSocketServer.sendToAllAdmin(jsonString);
    }
    }
}