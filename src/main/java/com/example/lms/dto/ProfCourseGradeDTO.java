package com.example.lms.dto;

import lombok.Data;

/**
 * 2025. 12. 01.
 * Author - SH
 * TB_GRADE TABLE DTO
 * 성적 정보
 */

@Data
public class ProfCourseGradeDTO {
	
	private int studentUserNo;
	private int courseNo;
	
	// 성적 기준 (교수재량)
	private int attendanceRatio; 	// 출석 비율
	private int assignmentRatio; 	// 과제 비율
	private int examRatio;       	// 시험 비율
	
	private int examMaxScore = 100; // 시험 만점 기본값
	
	// 학생 실제 점수
	private double attendanceRate;	// 출석률
	private double assignmentScore;	// 과제 원점수
	private double examScore;		// 시험 원점수

	// 계산 결과
	private double attendanceScore;	
	private double assignmentScoreWeighted;
	private double examScoreWeighted;
	private double finalScore;
	
	private String gradeValue;
}
