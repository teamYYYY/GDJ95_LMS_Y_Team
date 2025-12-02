package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentCourseNoticeDTO {
	private Integer index;
	
	private Integer courseNoticeNo;        // 공지 번호 (상세 이동용)
	private Integer courseNo;
	
    private String courseNoticeTitle;      // 제목
    private String courseNoticeContent;    // 내용
    
    private String writerUserName;         // 작성자 이름
    private Integer courseNoticeViewCount; // 조회수
    
    private String createdate;             // 등록일 (yyyy-MM-dd)
}