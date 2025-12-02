package com.example.lms.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.CourseManagementDTO;
import com.example.lms.mapper.admin.CourseManagementMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 12. 02.
 * Autor - JM
 * 학점 관리 서비스
 */
@Slf4j
@Service
@Transactional
public class CourseManagementService {
	
		@Autowired
		private CourseManagementMapper courseManagementMapper; 
	
		// ----------------------------------------------------
	    // 통합 메서드 ( Controller 에서 사용 )
	    // ----------------------------------------------------

	    // 2025. 12. 03. JM. 학점 관리 - 통합 검색 및 리스트 조회 (관리자전용)
	    /**
	     * 관리자용 학점 목록을 조회하고, 검색 조건에 따라 카운트도 처리하는 통합 메서드
	     * @param paramMap 검색 조건 (selectedYear, selectedSemester, searchCourseCondition) 및 페이징 조건 (startRow, limit)
	     * @return 조회된 학점 목록 및 전체 카운트를 담은 Map
	     */
	    public Map<String, Object> getCourseManagementListUseAdmin(Map<String, Object> paramMap) {
	        
	        Map<String, Object> resultMap = new HashMap<>();
	        List<CourseManagementDTO> courseList;
	        Integer totalCount;
	        Integer totalPages;
	        Integer limit = (Integer) paramMap.get("limit");
	        
	        // 1. 검색 조건 유무 확인
	        boolean isSearchActive = 
	        		
	            (paramMap.get("selectedYear") != null && !String.valueOf(paramMap.get("selectedYear")).trim().isEmpty()) ||
	            (paramMap.get("selectedSemester") != null && !String.valueOf(paramMap.get("selectedSemester")).trim().isEmpty()) ||
	            (paramMap.get("searchCourseCondition") != null && !String.valueOf(paramMap.get("searchCourseCondition")).trim().isEmpty());
	        
	        if (isSearchActive) {
	            
	            // 2. 검색 조건이 있는 경우: search 쿼리 사용
	            log.info("관리자 학점 관리: 검색 조건 적용 조회 시작. Params: {}", paramMap);
	            courseList = searchCourseManagementListUseAdmin(paramMap);
	            totalCount = searchCourseManagementListUseAdminCnt(paramMap);
	            totalPages = (int) Math.ceil((double) totalCount / limit);
	            
	        } else {
	            
	            // 3. 검색 조건이 없는 경우: 기본 list 쿼리 사용
	            log.info("관리자 학점 관리: 기본 전체 조회 시작. Params: {}", paramMap);
	            courseList = courseManagementListUseAdmin(paramMap);
	            totalCount = courseManagementListUseAdminCnt(); // 파라미터 없음
	            totalPages = (int) Math.ceil((double) totalCount / limit);
	        }
	        
	        resultMap.put("list", courseList);
	        resultMap.put("totalCount", totalCount);
	        resultMap.put("totalPages", totalPages);
	        
	        return resultMap;
	    }
	    
	    // 2025. 12. 03. JM. 학점 관리 - 통합 검색 및 리스트 조회 (학생전용)
	    /**
	     * 학생용 학점 목록을 조회하고, 검색 조건에 따라 카운트도 처리하는 통합 메서드
	     * @param paramMap 검색 조건 및 페이징 조건 (userNo, selectedYear, selectedSemester, searchCourseCondition, startRow, limit)
	     * @return 조회된 학점 목록 및 전체 카운트를 담은 Map
	     */
	    public Map<String, Object> getCourseManagementListUseStudt(Map<String, Object> paramMap) {
	        
	        Map<String, Object> resultMap = new HashMap<>();
	        List<CourseManagementDTO> courseList;
	        Integer totalCount;
	        Integer totalPages;
	        Integer limit = (Integer) paramMap.get("limit");

	        // 1. 검색 조건 유무 확인 (userNo 외의 검색 조건)
	        boolean isSearchActive = 
	            (paramMap.get("selectedYear") != null && !String.valueOf(paramMap.get("selectedYear")).trim().isEmpty()) ||
	            (paramMap.get("selectedSemester") != null && !String.valueOf(paramMap.get("selectedSemester")).trim().isEmpty()) ||
	            (paramMap.get("searchCourseCondition") != null && !String.valueOf(paramMap.get("searchCourseCondition")).trim().isEmpty());
	        
	        // userNo 파라미터가 Map에 존재함을 가정합니다.
	        
	        if (isSearchActive) {
	            
	            // 2. 검색 조건이 있는 경우: search 쿼리 사용
	            log.info("학생 학점 관리: 검색 조건 적용 조회 시작. Params: {}", paramMap);
	            courseList = searchCourseManagementListUseStudt(paramMap);
	            totalCount = searchCourseManagementListUseStudtCnt(paramMap);
	            totalPages = (int) Math.ceil((double) totalCount / limit);
	            
	        } else {
	            
	            // 3. 검색 조건이 없는 경우: 기본 list 쿼리 사용
	            log.info("학생 학점 관리: 기본 전체 조회 시작. Params: {}", paramMap);
	            courseList = courseManagementListUseStudt(paramMap);
	            
	            // userNo를 Map에서 추출하여 Integer로 전달
	            Integer userNo = (Integer) paramMap.get("userNo");
	            totalCount = courseManagementListUseStudtCnt(userNo);
	            totalPages = (int) Math.ceil((double) totalCount / limit);
	        }
	        
	        resultMap.put("list", courseList);
	        resultMap.put("totalCount", totalCount);
	        resultMap.put("totalPages", totalPages);
	        
	        return resultMap;
	    }

	    // 2025. 12. 02. JM. 학점 관리 - 학점 조회 (관리자전용)
	    private List<CourseManagementDTO> courseManagementListUseAdmin (Map<String, Object> searchParams) {
	    	
	        return courseManagementMapper.courseManagementListUseAdmin(searchParams);
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 조회 (관리자전용) 리스트 페이징 수
	    private Integer courseManagementListUseAdminCnt() {
	    	
	        return courseManagementMapper.courseManagementListUseAdminCnt();
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 조회 (학생전용)
	    private List<CourseManagementDTO> courseManagementListUseStudt (Map<String, Object> searchParams) {
	    	
	        return courseManagementMapper.courseManagementListUseStudt(searchParams);
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 조회 (학생전용) 리스트 페이징 수
	    private Integer courseManagementListUseStudtCnt(Integer userNo) {
	    	
	        return courseManagementMapper.courseManagementListUseStudtCnt(userNo);
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 (관리자전용)
	    private List<CourseManagementDTO> searchCourseManagementListUseAdmin (Map<String, Object> searchParams) {
	    	
	        return courseManagementMapper.searchCourseManagementListUseAdmin(searchParams);
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 카운트 (관리자전용)
	    private Integer searchCourseManagementListUseAdminCnt(Map<String, Object> searchParams) {
	    	
	        return courseManagementMapper.searchCourseManagementListUseAdminCnt(searchParams);
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 (학생전용)
	    private List<CourseManagementDTO> searchCourseManagementListUseStudt (Map<String, Object> searchParams) {
	    	
	        return courseManagementMapper.searchCourseManagementListUseStudt(searchParams);
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 검색 조회 카운트 (학생전용)
	    private Integer searchCourseManagementListUseStudtCnt(Map<String, Object> searchParams) {
	    	
	        return courseManagementMapper.searchCourseManagementListUseStudtCnt(searchParams);
	    }

	    // ----------------------------------------------------
	    // 셀렉트 박스 및 상세 정보 조회
	    // ----------------------------------------------------

	    // 2025. 12. 02. JM. 학점 관리 - 학점 조회 연도별 셀렉박스 리스트
	    public List<CourseManagementDTO> selectCourseYearList() {
	    	
	        return courseManagementMapper.selectCourseYearList();
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 학점 조회 학기별 셀렉박스 리스트
	    public List<CourseManagementDTO> selectCourseSemesterList() {
	    	
	        return courseManagementMapper.selectCourseSemesterList();
	    }
	    
	    // 2025. 12. 02. JM. 학점 관리 - 강의 상세정보 조회 
	    public CourseManagementDTO selectCourseDetail(Integer courseNo) {
	    	
	        return courseManagementMapper.selectCourseDetail(courseNo);
	    }
}
