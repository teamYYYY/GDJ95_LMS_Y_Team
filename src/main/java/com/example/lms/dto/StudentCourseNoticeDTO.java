package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentCourseNoticeDTO {
    private Integer index;

    private Integer courseNoticeNo;
    private Integer courseNo;

    private String courseNoticeTitle;
    private String courseNoticeContent;

    private String writerUserName;

    private String createdate; 
}
