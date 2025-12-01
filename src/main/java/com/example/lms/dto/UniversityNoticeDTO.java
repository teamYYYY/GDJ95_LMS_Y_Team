package com.example.lms.dto;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * 
 * 2025. 11. 21.
 * Author - jm
 * TB_NOTICE TABLE DTO
 * 학교 공지사항 테이블 
 */
@Data
public class UniversityNoticeDTO {
	
	// TB_NOTICE
	private int universityNoticeNo; //공지번호
    private String universityNoticeTitle; //공지제목
    private String universityNoticeContent; //공지내용
    private int universityWriterUserNo; //작성자
    private int universityNoticePriorityCode; // 우선순위코드(최우선 최상단 공지사항 지정처리)
    private int universityNoticeViewCnt; // 조회수
    private LocalDateTime universityNoticeUpdatedate; // 업데이트일자
    private LocalDateTime universityNoticeCreatedate; //등록일자
    
    //TB_UNIVERSITY_NOTICE_PRIORITY
    private String universityNoticePriorityName; // 우선순위코드 명칭 ex) 고정공지사항 10 , 우선공지사항 20, 일반공지사항 30
    
    // 뷰단 순위 스타일땜에 추가
    private String priorityColorClass; // 예: "text-red-600"
    
    // 작성자 이름 DTO에 담아야해서 추가함
    private String universityWriterUserName; // 작성자 이름
}
