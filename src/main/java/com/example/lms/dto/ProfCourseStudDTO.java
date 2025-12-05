package com.example.lms.dto;

import lombok.Data;

/**
 * 2025. 11. 25.
 * Author - SH
 * TB_ENROLLMENT, TB_SYSUSER, TB_COURSE TABLE DTO
 * 학생 수강 정보 + 출석률 + 성적 (목록 화면 표시용)
 */

@Data
public class ProfCourseStudDTO {

    private int enrollmentNo;      // 수강신청번호
    private int studentUserNo;     // 학생 사용자번호

    private String studentId;      // 학번(user_id)
    private String studentName;    // 이름(user_name)
    private String studentPhone;   // 전화번호
    private int studentGrade;      // 학년
    private String deptName;       // 학과명

    private String gradeValue;     // 최종 등급(A,B,C...)
    private Double examScore;      // 시험 원점수 (TB_GRADE의 grade_score)

    private Double assignmentScore; // 과제 점수 (목록 화면 표시용) 
    
    private Double attendanceRate; // 출석률 (0~100%)
    
    private Double finalScore;
    
    private Integer attendanceStatus; // 출석 상태 
}