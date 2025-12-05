package com.example.lms.mapper.excel;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.CourseManagementDTO;

/**
*
* 2025. 12. 03.
* Author - jm
* 학점관리 엑셀 Mapper
*/
@Mapper
public interface CourseManagementExcelMapper {
	
	// 학점관리 - 조회 검색 리스트 가져오기 - 관리자 selectedCourseYearAndSemester, searchCourseCondition
	List<CourseManagementDTO> searchCourseManagementListUseAdminForExcel(Map<String, Object> searchParams);
	
	// 학점관리 - 조회 검색 리스트 가져오기 - 학생 selectedCourseYearAndSemester, searchCourseCondition
	List<CourseManagementDTO> searchCourseManagementListUseStudtForExcel(Map<String, Object> searchParams);
}
