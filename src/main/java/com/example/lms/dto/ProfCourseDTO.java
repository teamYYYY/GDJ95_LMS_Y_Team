package com.example.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * 2025. 11. 24.
 * Author - SH
 * TB_COURSE TABLE DTO
 * 출석 정보
 */

@Data
public class ProfCourseDTO {
	private int courseNo;			  // 강의번호 PK
	private int professorUserNo;      // 교수번호 FK
	private int courseYear;			  // 개설년도
	private int courseSemester;		  // 학기
	private String courseName;		  // 강의명
	private String courseDescription; // 강의 설명
	private int courseCapacity;		  // 최대 수강 가능 인원
	private int courseScore; 		  // 이수 학점
	private String courseClassroom;   // 강의실
	private int courseStatus;		  // 0=종료, 1=개설
	private String createdate;		  // 생성일시
	private String updatedate;	      // 수정일시
	
	private String deptCode;
	private String deptName;
	
	private Integer courseTimeYoil;
    private Integer courseTimeStart;
    private Integer courseTimeEnd;
    
    public String getYoilName() {
        if (courseTimeYoil == null) return "";
        return switch (courseTimeYoil) {
            case 1 -> "월";
            case 2 -> "화";
            case 3 -> "수";
            case 4 -> "목";
            case 5 -> "금";
            default -> "";
        };
    }
    
    public boolean isOpened() {
        return this.courseStatus == 1;
    }

    public boolean isClosed() {
        return this.courseStatus == 0;
    }

    private List<ProfCourseTimeDTO> timeList;
}
   