package com.example.lms.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 
 * 2025. 11. 21.
 * Author - jm
 * TB_SYSUSER TABLE DTO
 * 사용자 테이블
 */
@Data
public class SysUserDTO {

	private int userNo; //사용자번호
    private String userId; //사용자ID
    private String userAuth; //사용자권한
    private String userStatus; //사용자상태
    private String userName; //사용자명
    private String userPassword; //사용자비밀번호
    private String userEmail; //사용자이메일
    private String userPhone; //사용자연락처
    private Date userBirth; //사용자생년월일
    private String deptCode; //학과코드
    private Integer userGrade; //학년 (학생)
    private String userPosition; //직급 (교수)
    private String userAddr; // 사용자주소
    private String userAddrDetail; //사용자상세주소
    private String userZipcode; //우편번호
    private LocalDateTime userLastLogin; //마지막로그인일자
    private Integer userLoginFailCnt; //로그인실패수
    private LocalDateTime userCreatedate; //생성일자
    private LocalDateTime userUpdatedate; //수정일자
    
    //세션 값에 추가해야함
    private String authDetailName;
    private String authCode;
    private String authName;
}
