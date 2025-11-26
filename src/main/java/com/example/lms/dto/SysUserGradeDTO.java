package com.example.lms.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * 2025. 11. 26.
 * Author - jm
 * TB_SYSUSER_GRADE TABLE DTO
 * 시스템 사용자 학년 테이블 
 */
@Data
public class SysUserGradeDTO {
	
	  private Integer gradeCode; //사용자 학년코드
	  private String gradeName; //사용자 학년명
	  private LocalDateTime gradeCreatedate; //생성일자
}
