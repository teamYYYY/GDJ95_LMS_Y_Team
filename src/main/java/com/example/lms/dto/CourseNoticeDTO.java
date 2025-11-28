package com.example.lms.dto;

import lombok.Data;

/**
 * 2025. 11. 24.
 * Author - SH
 * TB_COURSE_NOTICE TABLE DTO
 * 출석 정보
 */

@Data
public class CourseNoticeDTO {
	private int courseNoticeNo;			// 공지번호 PK
	private int courseNo; 				// 강의번호 FK
	private int writerUserNo;			// 교수번호 FK
	private String CourseNoticeTitle;	// 공지제목
	private String CourseNoticeContent; // 공지내용
	private int courseNoticeViewCount;	// 조회수
	private String createdate;			// 생성일시
	private String updatedate;			// 수정일시
	
}
