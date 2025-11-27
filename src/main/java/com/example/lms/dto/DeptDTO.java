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
    private LocalDateTime deptUpdatedate; //업데이트일자
    
    // 업데이트 시 넣을 새 학과코드 변수명이 필요함...
    private String deptNewCode; // 업데이트 학과코드
    
    // 리스트 추가할 때 필요 해서 넣어둠...
    private Integer rownum;
}
