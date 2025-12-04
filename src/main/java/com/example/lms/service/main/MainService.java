package com.example.lms.service.main;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.MainDTO;
import com.example.lms.mapper.main.MainMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * 2025. 12. 04.
 * Autor - JM
 * 메인 페이지 서비스
 */
@Slf4j
@Service
@Transactional
public class MainService {
	
		@Autowired
		private MainMapper mainMapper;
	
		// 메인페이지 사용자 프로파일카드
		public MainDTO userProfileCard(Integer userNo) {
			
			return mainMapper.userProfileCard(userNo);
		}
		
		// 메인페이지 학교 공지사항
		public List<MainDTO> mainUniversityNoticeList() {
			
			return mainMapper.mainUniversityNoticeList();
		}
		
		
		// 메인 페이지 시간표 뿌리기 ( authCode, userNo, courseTimeYoil )
		public List<MainDTO> getMainCourseTimeTable (MainDTO mainDTO) {
			
			
			List<MainDTO> getMainCourseTimeTable = new ArrayList<MainDTO>();
			String authCode = mainDTO.getAuthCode();
			
			// 교수 
			if (authCode.equals("P001")) {
				
				getMainCourseTimeTable = mainCourseTimeTableByProfessor(mainDTO);
				//학생
			} else if (authCode.equals("S001")) {
				
				getMainCourseTimeTable = mainCourseTimeTableByStudent(mainDTO);
			}
			
			return getMainCourseTimeTable;
		}
		
		// 메인페이지 공지사항 조회시 ( 조회 카운트 증가 처리 )
		public Integer increaseUniversityNoticeViewCnt(Integer universityNoticeNo) {
			
			return mainMapper.increaseUniversityNoticeViewCnt(universityNoticeNo);
		}
		
		/**
		 * 메인페이지 시간표 ( 교수가 보는 강의 시간표 )
		 * @param mainDTO ( userNo, courseTimeYoil )
		 */
		private List<MainDTO> mainCourseTimeTableByProfessor(MainDTO mainDTO) {
			
			return mainMapper.mainCourseTimeTableByProfessor(mainDTO);
		}
		
		/**
		 * 메인페이지 시간표 ( 학생이 보는 강의 시간표 )
		 * @param mainDTO ( userNo, courseTimeYoil )
		 */
		private List<MainDTO> mainCourseTimeTableByStudent(MainDTO mainDTO) {
			
			return mainMapper.mainCourseTimeTableByStudent(mainDTO);
		}
}
