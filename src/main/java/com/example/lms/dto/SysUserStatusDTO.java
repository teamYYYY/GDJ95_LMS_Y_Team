package com.example.lms.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * 2025. 11. 21.
 * Author - jm
 * TB_SYSUSER_STATUS TABLE DTO
 * 사용자 상태 테이블
 */
@Data
public class SysUserStatusDTO {
    
	private String statusCode; //사용자 상태코드 ( 1=재학, 2=휴학, 3=퇴학 )
    private String statusName; //상태코드 상태명
    private LocalDateTime statusCreatedate; //생성일자
}
