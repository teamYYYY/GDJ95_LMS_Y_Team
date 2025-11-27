/**
 * 2025. 11. 27.
 * Author - yj
 * 학생 수강신청 내역 DTO
 * TB_ENROLLMENT + TB_COURSE + TB_SYSUSER + TB_COURSE_TIME JOIN 결과
 */
package com.example.lms.dto;

import lombok.Data;

@Data
public class EnrollmentListDTO {

    // TB_ENROLLMENT
    private Integer enrollmentNo;
    private Integer enrollmentStatus; // 0=신청완료, 1=취소됨
    private String createdate;

    // TB_COURSE
    private Integer courseNo;
    private String  courseName;
    private Integer courseScore;
    private Integer courseCapacity;

    // 교수명
    private String professorName;

    // TB_COURSE_TIME
    private String courseTimeYoil;
    private String courseTimeStart;
    private String courseTimeEnd;

    // 화면 전용 (Mustache 사용)
    private Boolean isActive;    // status=0
    private Boolean isCanceled;  // status=1
}
