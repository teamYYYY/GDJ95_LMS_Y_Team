package com.example.lms.dto;

import lombok.Data;

@Data
public class StudentGradeDTO {

    private int courseNo;
    private int studentUserNo;

    private Double gradeScore;   // 최종 점수
    private String gradeValue;   // 등급
}
