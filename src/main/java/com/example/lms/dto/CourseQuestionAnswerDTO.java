/**
 * 2025. 11. 24.
 * Author - yj
 * TB_COURSE_QUESTION_ANSWER TABLE DTO
 * 강의 문의사항 답변 정보
 */
package com.example.lms.dto;

import lombok.Data;

@Data
public class CourseQuestionAnswerDTO {
	 private int answerNo;              // PK
	 private int courseQuestionNo;      // 어떤 질문에 대한 답변인지 (FK)
	    
	 private int writerUserNo;          // 작성자 번호
	 private String writerName;         // 작성자 이름 (JOIN)
	 private String writerRole;         // STUDENT / PROFESSOR / ADMIN
	    
	 private String answerContent;      // 답변 내용
	 private String createdate;         // 작성 시간
}
