package com.example.lms.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CourseTimeDTO {

    // PK
    private int courseTimeNo;

    // FK : 교수
    private int professorUserNo;

    // FK : 강의
    private int courseNo;

    // 요일 코드 (1=월,2=화,3=수,4=목,5=금,6=토,7=일)
    private int courseTimeYoil;

    // 시작 시간 (정수: 900, 1330)
    private int courseTimeStart;

    // 종료 시간 (정수: 1030, 1530)
    private int courseTimeEnd;

    // 생성/수정일
    private String createdate;
    private String updatedate;
}
