package com.example.lms.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * 2025. 11. 21.
 * Author - jm
 * TB_DEPT TABLE DTO
 * 학과 테이블 
 */
@Data
public class DeptDTO {
	
    private String deptCode; //학과코드
    private String deptName; //학과명
    private LocalDateTime deptCreatedate; //생성일자
}
