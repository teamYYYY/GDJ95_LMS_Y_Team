package com.example.lms.dto;

import java.time.LocalDateTime;
import lombok.Data;


/**
 * 
 * 2025. 11. 21.
 * Author - jm
 * TB_SYSAUTH TABLE DTO
 * 시스템 사용자 권한 테이블 + 시스템 사용자 세부 권한 테이블
 * 핸들링 컬럼이 적어 분할안하고 그냥 합침
 */
@Data
public class SysAuthDTO {
	
	  // TB_SYSAUTH
	  private String authCode; //사용자 권한코드
	  private String authName; //권한명
	  private LocalDateTime authCreatedate; //생성일자
	  
	  // TB_SYSAUTH_DETAIL
	  private String authDetailCode; //사용자 세부 권한코드
	  private String authDetailName; //세부 권한명
	  private LocalDateTime authDetailCreatedate; //생성일자
}
