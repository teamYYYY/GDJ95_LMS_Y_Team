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

    private int courseQuestionNo;      // 문의번호 PK
    private int courseNo;              // 강의번호 FK
    private int writerUserNo;          // 학생번호 FK
    private String courseQuestionTitle;// 문의 제목
    private String courseQuestionContent; // 문의 내용
    private String courseQuestionStatus;  // 0=미답변, 1=답변완료
    private String createdate;            // 등록시간
}