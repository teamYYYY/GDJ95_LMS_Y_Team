package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentAssignmentDTO {

    private Integer assignmentNo;
    private String assignmentTitle;
    private String assignmentDeadline;

    private Boolean submitted;       // 제출 여부
    private Double assignmentScore;  // 점수 (TB_ASSIGNMENT_SUBMISSION)

}