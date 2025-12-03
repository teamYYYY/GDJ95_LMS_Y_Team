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

    private int courseQuestionNo;      // 문의번호 PK
    private int courseNo;              // 강의번호 FK

    private int writerUserNo;          // 작성자 번호
    private String writerName;         // 작성자 이름
    private String writerRole;         // STUDENT / PROFESSOR

    private String courseQuestionTitle;    // 문의 제목
    private String courseQuestionContent;  // 문의 내용

    private String courseQuestionStatus;   // 0=미답변, 1=답변완료
    private String createdate;             // 등록시간

    private boolean answered;              // 답변 여부

    /**
     * DB tinyint(1) is_private → DTO privatePost로 안전하게 매핑
     *
     * Mustache: {{privatePost}}
     * Service:  dto.isPrivatePost()
     */
    private boolean privatePost;

    /**
     * 로그인한 사용자가 이 글을 볼 수 있는지 여부
     * 목록 & 상세 공통
     */
    private boolean canView;

    private List<CourseQuestionAnswerDTO> answerList;
}
