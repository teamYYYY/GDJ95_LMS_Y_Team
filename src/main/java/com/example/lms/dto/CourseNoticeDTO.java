package com.example.lms.dto;

import lombok.Data;

@Data
public class CourseNoticeDTO {
	private int courseNoticeNo;
	private int courseNo;
	private int writerUserNo;
	private String CourseNoticeTitle;
	private String CourseNoticeContent;
	private int courseNoticeViewCount;
	private String createdate;
	private String updatedate;
	
}
