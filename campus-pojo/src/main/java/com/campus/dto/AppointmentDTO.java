package com.campus.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentDTO {
    private Long resourceId;
    private String type;        // RESOURCE, VENUE
    private LocalDate appointDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
}