/**
 * 2025. 11. 26.
 * Author - yj
 * 학생 수강신청 화면용 강의 정보 DTO
 */
package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentCourseDTO {

    // TB_COURSE
    private Integer courseNo;            // 강의 번호 (TB_COURSE.course_no)
    private String  courseName;          // 강의명
    private Integer professorUserNo;     // 담당 교수 사용자번호 (TB_SYSUSER.user_no)
    private String  professorName;       // 담당 교수명 (JOIN 필드)
    private Integer courseYear;          // 개설 연도
    private Integer courseSemester;      // 학기 (1=1학기, 2=2학기)
    private Integer courseCapacity;      // 정원
    private Integer courseScore;         // 학점


    // 수강 현황
    private Integer currentCount;        // 현재 신청 인원
    private Boolean isFull;              // 정원 초과 여부

    // 강의 시간
    private Integer courseTimeYoil;
    private Integer courseTimeStart;
    private Integer courseTimeEnd;
}
