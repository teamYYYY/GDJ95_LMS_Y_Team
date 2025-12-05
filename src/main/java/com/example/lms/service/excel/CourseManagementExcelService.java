package com.example.lms.service.excel;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.CourseManagementDTO;
import com.example.lms.mapper.excel.CourseManagementExcelMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * 2025. 12. 02.
 * Autor - JM
 * 학점 관리 엑셀 서비스
 */
@Slf4j
@Service
@Transactional
public class CourseManagementExcelService {
	
		@Autowired
		private CourseManagementExcelMapper courseManagementExcelMapper;
	
		
	
		// 학점관리 - 조회 검색 리스트 가져오기 - 관리자 selectedCourseYearAndSemester, searchCourseCondition
		public List<CourseManagementDTO> searchCourseManagementListUseAdminForExcel(Map<String, Object> searchParams) {
			
			return courseManagementExcelMapper.searchCourseManagementListUseAdminForExcel(searchParams);
		};
		
		// 학점관리 - 조회 검색 리스트 가져오기 - 학생 selectedCourseYearAndSemester, searchCourseCondition
		public List<CourseManagementDTO> searchCourseManagementListUseStudtForExcel(Map<String, Object> searchParams) {
			
			return courseManagementExcelMapper.searchCourseManagementListUseStudtForExcel(searchParams);
		}
}
