/**
 * 2025. 11. 25.
 * Author - yj
 * TB_COURSE_QUESTION TABLE DTO (안전 버전)
 */
package com.example.lms.dto;

import java.util.List;
import lombok.Data;

@Data
public class CourseQuestionDTO {

    // 문의번호 PK
    private int courseQuestionNo;

    // 강의번호 FK
    private int courseNo;

    // 작성자 정보
    private int writerUserNo;
    private String writerName;
    private String writerRole; // STUDENT / PROFESSOR

    // 제목/내용
    private String courseQuestionTitle;
    private String courseQuestionContent;

    // 상태값
    private String courseQuestionStatus; // 0=미답변, 1=답변완료
    private String createdate;

    // Boolean 값들
    private boolean answered;     // 답변 여부
    private boolean privatePost;  // 비밀글 여부
    private boolean canView;      // 열람 가능 여부
    private boolean owner;        // 작성자 본인 여부
    private boolean professor;    // 교수 여부

    // 댓글 리스트
    private List<CourseQuestionAnswerDTO> answerList;
    
    // 화면 표시용 번호
    private int index;
}
