package com.example.lms.dto;

import lombok.Data;

/**
 * 2025. 12. 01.
 * Author - SH
 * 교수용 강의별 공지 DTO
 * courseNoticeSummaryList와 매핑
 */

@Data
public class ProfCourseNoticeDTO {
	private int courseNo;              // 강의번호
    private String courseName;         // 강의명
    private String courseNoticeTitle;  // 최신 공지 제목
    private int noticeCount;           // 해당 강의 공지 개수
}
