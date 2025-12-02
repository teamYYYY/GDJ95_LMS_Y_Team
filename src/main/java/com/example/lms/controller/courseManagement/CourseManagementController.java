package com.example.lms.controller.courseManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.lms.controller.admin.userManagement.UserManagementController;
import com.example.lms.dto.CourseManagementDTO;
import com.example.lms.dto.SysAuthDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.admin.CourseManagementService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 12. 02.
 * Author - JM (Modified)
 * 학점 관리 컨트롤러 (관리자 , 학생전용 )
 */
@Slf4j
@Controller
// @RequestMapping("/admin") 여긴 공통경로 사용 안함..
public class CourseManagementController {
	
	@Autowired
	private CourseManagementService courseManagementService;
	
	// ================================================================================
    // 1. 시스템 사용자 전체 조회 (페이지 진입 시 최초 로딩 + 페이징 ) 관리자 + 학생 전용
    // ================================================================================
    @GetMapping("/userManagement")
    public String userManagement(Model model,
                                 @RequestParam(defaultValue = "1") Integer page, 
                                 @RequestParam(defaultValue = "10") Integer limit,
                                 @RequestParam String searchCourseCondition,
                                 @RequestParam String selectedYear,
                                 @RequestParam String selectedSemester,
                                 HttpSession session) {
        
    	SysAuthDTO sessionUserAuthDto = (SysAuthDTO) session.getAttribute("loginUser");
    	String userAuthCd = sessionUserAuthDto.getAuthCode();
    	
        // 페이징 계산
        // page 1 -> startRow 0, page 2 -> startRow 10
        Integer startRow = (page - 1) * limit;
        
    	Map<String, Object> searchParams = new HashMap<>();
    	searchParams.put("searchCourseCondition", searchCourseCondition);
    	searchParams.put("selectedYear", selectedYear);
    	searchParams.put("selectedSemester", selectedSemester);
    	searchParams.put("startRow", startRow);
    	searchParams.put("limit", limit);
        
    	// 관리자
    	if (userAuthCd.equals("A001")) {
    		
    		//학생
    	} else if (userAuthCd.equals("S001")) {
    		
    		return "redirect:/"
    	}
    	
        // 1. 학점 목록 조회 및 페이징 처리
        Map<String, Object> courseList = courseManagementService.getCourseManagementListUseAdmin(searchParams);
        
        // 검색조건 시 셀렉박스 리스트 ( 연도별, 학기별 )
        List<CourseManagementDTO> selectCourseYearList = courseManagementService.selectCourseYearList();
        List<CourseManagementDTO> selectCourseSemesterList = courseManagementService.selectCourseSemesterList();
        
        
        model.addAttribute("userList", userInfoMapList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("metaData", metaData); // 모달 폼 렌더링용
        
        return "/admin/userManagement";
    }
    
    // 	================================================================================
    // 2. 강의 세부 정보 조회 (AJAX 전용)
    // ================================================================================
    @GetMapping("/getCourseDetailListByCourseNo")
    @ResponseBody
    public Map<String, Object> getAuthDetailListByAuthCode(@RequestParam("courseNo") Integer courseNo) {
        
    	Map<String, Object> response = new HashMap<>();

        try {
        	
            CourseManagementDTO courseList = courseManagementService.selectCourseDetail(courseNo); 
            
            response.put("status", "success");
            response.put("detailList", courseList);
        } catch (Exception e) {
        	
            log.error("세부 권한 조회 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "세부 권한 정보를 불러오는 데 실패했습니다.");
        }
        return response;
    }
    
    // ================================================================================
    // 3. 강의 검색 조회 (AJAX - 리스트 갱신용)
    // ================================================================================
    @GetMapping("/searchUserInfo")
    @ResponseBody
    public Map<String, Object> searchUserInfo(@RequestParam String searchCondition,
    		@RequestParam(value = "currentPage", defaultValue = "1") int pageNo) {
        Map<String, Object> response = new HashMap<>();
        
        int limit = 10;      // 페이지당 사용자 수
        // 💡 시작 행 계산: (현재 페이지 - 1) * 페이지당 개수
        int startRow = (pageNo - 1) * limit;
        
        List<Map<String, Object>> searchList = userService.searchUserInfoMapList(searchCondition, startRow, limit);
        
     // 2. 전체 개수 카운트
        int totalCount = userService.searchUserInfoMapListCnt(searchCondition);
        
        response.put("status", "success");
        response.put("userList", searchList);
     // 💡 응답에 페이징 정보 포함
        response.put("totalCount", totalCount); 
        response.put("currentPage", pageNo);
        
        return response;
    }

}
