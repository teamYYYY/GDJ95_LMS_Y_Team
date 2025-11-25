/**
 * 2025. 11. 25.
 * Author - yj
 * TB_STUDENT_COURSE TABLE DTO
 * 학생 수강신청 화면용 강의 정보 DTO
 * (정원 / 현재 인원 / 정원초과 여부 포함)
 */
package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentCourseDTO {

    private int courseNo;               // 강의번호 (TB_COURSE FK)
    private String courseName;          // 강의명
    private String professorName;   	// 담당 교수 이름 (TB_SYSUSER.user_name)
    private int courseCapacity;         // 강의 정원
    private int currentCount;           // 현재 수강 신청 인원

    private boolean isFull;             // 정원 여부 true=꽉참, false=신청가능

    private String createDate;       // 생성일시
}
