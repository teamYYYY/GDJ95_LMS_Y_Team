package com.example.lms.dto;

import lombok.Data;

/**
 * 2025. 11. 25.
 * Author - SH
 * TB_ENROLLMENT, TB_SYSUSER, TB_COURSE TABLE DTO
 * 학생 과제 제출 정보
 */

@Data
public class ProfCourseTimeDTO {
	private int courseTimeNo;	    // 강의시간번호
	private int professorUserNo;    // 교수번호
	private int courseNo;		    // 강의번호
	private int courseTimeYoil;	    // 요일
	private int courseTimeStart;    // 시작시간	
	private int courseTimeEnd;		// 종료시간

}
