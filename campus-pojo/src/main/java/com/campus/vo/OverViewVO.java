package com.campus.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OverViewVO {
    private Integer pendingReview;
    private Integer totalResources;
    private Integer inUse;
    private Integer underRepair;
}
