package com.example.lms.mapper.main;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.MainDTO;


/**
 * 
 * 2025. 12. 04.
 * Author - jm
 * 메인페이지 Mapper
 */
@Mapper
public interface MainMapper {
	
	// 메인페이지 사용자 프로파일카드
	MainDTO userProfileCard(Integer userNo);
	
	// 메인페이지 학교 공지사항
	List<MainDTO> mainUniversityNoticeList();
	
	// 메인페이지 시간표 ( 교수가 보는 강의 시간표 )
	List<MainDTO> mainCourseTimeTableByProfessor(MainDTO mainDTO);
	
	// 메인페이지 시간표 ( 학생이 보는 강의 시간표 )
	List<MainDTO> mainCourseTimeTableByStudent(MainDTO mainDTO);
	
	// 메인페이지 공지사항 조회시 ( 조회 카운트 증가 처리 )
	Integer increaseUniversityNoticeViewCnt(Integer universityNoticeNo);
	
	// 메인페이지 메인페이지 졸업증명서 발급 이름, 생년월일, 학번, 학과, 학위명(학사), 입학일, 졸업일(2월28일로고정),
	List<MainDTO> certificateOfGraduationList(Integer userNo);
}
