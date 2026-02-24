package com.campus.vo;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysLogExportVO {
    
    @ExcelProperty("日志编号")
    @ColumnWidth(10)
    private Long id;

    @ExcelProperty("操作人ID")
    @ColumnWidth(12)
    private Long operatorId;

    @ExcelProperty("业务模块")
    @ColumnWidth(20)
    private String module;

    @ExcelProperty("操作动作")
    @ColumnWidth(20)
    private String action;

    @ExcelProperty("操作IP")
    @ColumnWidth(20)
    private String ip;

    @ExcelProperty("耗时(毫秒)")
    @ColumnWidth(15)
    private Long executionTime;

    @ExcelProperty("操作时间")
    @ColumnWidth(25)
    private String createTime;

    @ExcelProperty("请求参数")
    @ColumnWidth(40)
    private String params;
}