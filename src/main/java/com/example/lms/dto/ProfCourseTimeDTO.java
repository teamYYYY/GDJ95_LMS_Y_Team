package com.example.lms.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 2025. 11. 25.
 * Author - SH
 * TB_ENROLLMENT, TB_SYSUSER, TB_COURSE TABLE DTO
 * 학생 과제 제출 정보
 */

@Data
public class ProfCourseTimeDTO {

    private int courseTimeNo;
    private int professorUserNo;
    private int courseNo;
    private int courseTimeYoil;
    private int courseTimeStart;
    private int courseTimeEnd;

    // 요일 selected
    private boolean yoil1;
    private boolean yoil2;
    private boolean yoil3;
    private boolean yoil4;
    private boolean yoil5;

    // 교시 selected (동적 map 제거, Mustache용 boolean 필드)
    private boolean start1, start2, start3, start4, start5, start6, start7, start8, start9, start10;
    private boolean end1, end2, end3, end4, end5, end6, end7, end8, end9, end10;
}

