/**
 * 2025. 11. 25.
 * Author - yj
 * TB_COURSE_QUESTION TABLE DTO
 * 학생 강의 문의 정보
 */
package com.example.lms.dto;

import lombok.Data;

@Data
public class CourseQuestionDTO {
    private int courseQuestionId;
    private int courseId;
    private int writerUserNo;
    private String courseQuestionTitle;
    private String courseQuestionContent;
    private int courseQuestionStatus;
    private String createdate;
}
