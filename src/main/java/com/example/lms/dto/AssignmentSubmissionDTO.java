/**
 * 2025. 11. 25.
 * Author - yj
 * TB_ASSIGNMENT_SUBMISSION TABLE DTO
 * 학생 과제 제출 정보
 */
package com.example.lms.dto;

import lombok.Data;

@Data
public class AssignmentSubmissionDTO {
    private int assignmentSubmissionId;
    private int assignmentId;
    private int writerUserNo;
    private String assignmentSubmissionFileUrl;
    private String assignmentSubmissionContent;
    private Integer assignmentScore;
    private int assignmentSubmissionStatus;
    private String createdate;
    private String updatedate;
}
