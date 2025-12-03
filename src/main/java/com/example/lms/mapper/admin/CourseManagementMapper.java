package com.example.lms.mapper.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.CourseManagementDTO;

/**
 * 
 * 2025. 12. 02. 
 * Author - jm
 * 내정보 - 학점관리 Mapper
 */
@Mapper
public interface CourseManagementMapper {
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 조회 (관리자전용)
	List<CourseManagementDTO> courseManagementListUseAdmin (Map<String, Object> searchParams);
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 조회 (관리자전용) 리스트 페이징 수
	Integer courseManagementListUseAdminCnt();
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 조회 (학생전용)
	List<CourseManagementDTO> courseManagementListUseStudt (Map<String, Object> searchParams);
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 조회 (학생전용) 리스트 페이징 수
	Integer courseManagementListUseStudtCnt(Integer userNo);

	// 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 조건 셀렉박스 리스트 (연도-학기별 관리자 전용)
	List<CourseManagementDTO> selectCourseYearAndSemesterListByAdmin();

	// 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 조건 셀렉박스 리스트 (연도-학기별 학생 전용)
	List<CourseManagementDTO> selectCourseYearAndSemesterListByAStudt(Integer userNo);
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 (관리자전용)
	// Map에 모든 검색 조건 (연도, 학기, 검색어, 페이징) 포함
	List<CourseManagementDTO> searchCourseManagementListUseAdmin (Map<String, Object> searchParams);
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 카운트 (관리자전용)
	// Map에 모든 검색 조건 (연도, 학기, 검색어) 포함
	Integer searchCourseManagementListUseAdminCnt(Map<String, Object> searchParams);
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 (학생전용)
	// Map에 모든 검색 조건 (userNo, 연도, 학기, 검색어, 페이징) 포함
	List<CourseManagementDTO> searchCourseManagementListUseStudt (Map<String, Object> searchParams);
	
	// 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 카운트 (학생전용)
	// Map에 모든 검색 조건 (userNo, 연도, 학기, 검색어) 포함
	Integer searchCourseManagementListUseStudtCnt(Map<String, Object> searchParams);
	
	// 2025. 12. 02. JM. 학점 관리 - 강의 상세정보 조회 
	CourseManagementDTO selectCourseDetail(Integer courseNo);
	
}
