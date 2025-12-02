package com.example.lms.dto;

import lombok.Data;

/**
 * 학생 성적 요약 DTO
 * TB_GRADE 조회용
 */
@Data
public class GradeSummaryDTO {

    private String gradeValue;   // 등급 (A+, B ...)
    private Double gradeScore;   // 점수 (4.5, 4.0 ...)
}
