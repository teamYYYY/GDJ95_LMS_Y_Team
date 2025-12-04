package com.example.lms.dto;

import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;

/**
 * 
 * 2025. 12. 04.
 * Author - jm
 * 메인페이지에서 사용할 DTO
 */
@Data
public class MainDTO {
	
	// TB_SYSUSER 사용자테이블
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
	
    // TB_SYSAUTH 사용자권한테이블
    private String authCode; //사용자 권한코드
 	private String authName; //권한명
 	private LocalDateTime authCreatedate; //생성일자
 	private LocalDateTime authUpdatedate; //업데이트일자
 	  
 	// TB_SYSAUTH_DETAIL 사용자세부권한테이블
 	private String authDetailCode; //사용자 세부 권한코드
 	private String authDetailName; //세부 권한명
 	private LocalDateTime authDetailCreatedate; //생성일자
 	private LocalDateTime authDetailUpdatedate; //업데이트일자
 	
 	// TB_DEPT 학과 테이블
    private String deptName; //학과명
    private LocalDateTime deptCreatedate; //생성일자
    private LocalDateTime deptUpdatedate; //업데이트일자
    
    // TB_NOTICE 학교 공지사항 테이블
 	private int universityNoticeNo; //공지번호
    private String universityNoticeTitle; //공지제목
    private String universityNoticeContent; //공지내용
    private int universityWriterUserNo; //작성자
    private int universityNoticePriorityCode; // 우선순위코드(최우선 최상단 공지사항 지정처리)
    private int universityNoticeViewCnt; // 조회수
    private LocalDateTime universityNoticeUpdatedate; // 업데이트일자
    private LocalDateTime universityNoticeCreatedate; //등록일자
    
    //TB_UNIVERSITY_NOTICE_PRIORITY 학교 공지사항 우선순위 테이블
    private String universityNoticePriorityName; // 우선순위코드 명칭 ex) 고정공지사항 10 , 우선공지사항 20, 일반공지사항 30
    // 뷰단 순위 스타일땜에 추가
    // private String priorityColorClass; // 예: "text-red-600"
    private Boolean is_10;
    private Boolean is_20;
    private Boolean is_30;
    // 작성자 이름 DTO에 담아야해서 추가함
    private String universityWriterUserName; // 작성자 이름
    
    // TB_ENROLLMENT
    private Integer enrollmentNo;    // 수강신청번호 PK
    private Integer studentUserNo;       // 학생번호 (FK)
    private Integer  courseNo;            // 강의번호 (FK)
    private Integer enrollmentStatus; // 0=신청완료, 1=취소됨
    private String createdate;       // 신청일시
    private String updatedate;       // 수정일시
    
    // TB_COURSE_TIME
    private Integer courseTimeYoil;
    private Integer courseTimeStart;
    private Integer courseTimeEnd;
    
    public String getYoilName() {
        if (courseTimeYoil == null) return "";
        return switch (courseTimeYoil) {
            case 1 -> "월";
            case 2 -> "화";
            case 3 -> "수";
            case 4 -> "목";
            case 5 -> "금";
            default -> "";
        };
    }
    
    //TB_COURSE
  	private int professorUserNo;     // 교수번호 FK
  	private int courseYear;          // 개설년도
  	private int courseSemester;      // 학기
  	private String courseName;       // 강의명
  	private String courseDescription;// 강의 설명
  	private int courseCapacity;      // 최대 수강 가능 인원
  	private int courseScore;         // 이수 학점
  	private String courseClassroom;  // 강의실
  	private int courseStatus;        // 0=종료, 1=개설
  	
	private String professorUserName; //교수이름
}
