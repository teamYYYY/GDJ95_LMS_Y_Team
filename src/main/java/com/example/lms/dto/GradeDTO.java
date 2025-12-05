package com.example.lms.dto;

import lombok.Data;

/**
 * 2025. 11. 24.
 * Author - SH
 * TB_GRADE TABLE DTO
 * 출석 정보
 */

@Data
public class GradeDTO {
	private int gradeNo;		// 성적 PK
	private int studentUserNo;	// 성적 대상 학생 FK
	private int courseNo;		// 강의번호 FK
	private String gradeValue;	// 성적표기 (등급 A+, B0 등 성적 표기 )
	private double gradeScore;  // 점수
	private double gradeFinalScore; //최종점수
	private String createdate;	// 생성일시
}
