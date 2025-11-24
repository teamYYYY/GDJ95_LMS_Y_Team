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
public class NoticeDTO {
	
	private int noticeNo; //공지번호
    private String noticeTitle; //공지제목
    private String noticeContent; //공지내용
    private int noticeWriterUserNo; //작성자
    private LocalDateTime noticeCreatedate; //등록일자
}
