/**
 * 2025. 11. 24.
 * Author - yj
 * ENROLLMENT TABLE DTO
 * 학생 수강신청 정보
 */
package com.example.lms.dto;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private int enrollmentId;
    private int studentUserNo;
    private int courseId;
    private int enrollmentStatus;
    private String createdate;
    private String updatedate;
}
