package com.example.lms.dto;

import java.time.LocalDateTime;
import lombok.Data;


/**
 * 
 * 2025. 11. 21.
 * Author - jm
 * TB_SYSAUTH TABLE DTO
 * 시스템 사용자 권한 테이블 
 */
@Data
public class SysAuthDTO {
	
	  private String authCode; //사용자 권한코드
	  private String authName; //권한명
	  private LocalDateTime authCreatedate; //생성일자
}
