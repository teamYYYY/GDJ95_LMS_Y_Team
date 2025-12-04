package com.example.lms.controller.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.lms.dto.MainDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.dto.UniversityNoticeDTO;
import com.example.lms.service.admin.UniversityNoticeService;
import com.example.lms.service.common.authorization.ViewAuthorizationValidateService;
import com.example.lms.service.main.MainService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 12. 04.
 * Author - jm
 * 메인페이지 컨트롤러
 */
@Slf4j
@Controller
public class MainController {
	
		@Autowired
		private MainService mainService;
		
		@Autowired
		private ViewAuthorizationValidateService viewAuthorizationValidateService;
		
		@Autowired
		private UniversityNoticeService universityNoticeService;
		
	
		// 메인 페이지 GET
		@GetMapping("/main")
		public String main(HttpSession session, Model model) {
			
			// 세션불러오기
			SysUserDTO loginSysUserDTO = (SysUserDTO) session.getAttribute("loginUser");
			
			Integer userNo = loginSysUserDTO.getUserNo();
			String authCode = loginSysUserDTO.getAuthCode();
			
			// 메뉴 권한 필터 처리
			
			Map<String, Boolean> menuValidateRsltMap = viewAuthorizationValidateService.MenuAuthorizationValidate(authCode);
			
			Boolean useAdmin = menuValidateRsltMap.get("useAdmin");
			Boolean useStudent = menuValidateRsltMap.get("useStudent"); 
			Boolean useProfessor = menuValidateRsltMap.get("useProfessor");
			Boolean useStaff = menuValidateRsltMap.get("useStaff");
			
			// 메인페이지 사용자 프로파일 카드
			MainDTO userProfileCard = new MainDTO();
			userProfileCard = mainService.userProfileCard(userNo);
			
			// 메인 페이지 학교 공지사항
			List<MainDTO> mainUniversityNoticeList = new ArrayList<MainDTO>();
			mainUniversityNoticeList = mainService.mainUniversityNoticeList();
			
			for (MainDTO notice : mainUniversityNoticeList) {
			    Integer code = notice.getUniversityNoticePriorityCode();
			    // Mustache 템플릿에서 조건부 렌더링을 위해 Boolean 값 추가
			    notice.setIs_10(Integer.valueOf(10).equals(code)); //고정공지
			    notice.setIs_20(Integer.valueOf(20).equals(code)); //우선공지
			    notice.setIs_30(Integer.valueOf(30).equals(code)); //일반공지
			}
			
			// 메인 페이지 시간표
			MainDTO mainCourseTimeParams = new MainDTO();
			mainCourseTimeParams.setUserNo(userNo);
			mainCourseTimeParams.setAuthCode(authCode);
			// 오늘 요일 숫자 가져오기 (월=1, 화=2, ..., 금=5)
     		// 일요일=0 → 토요일=6
			int todayYoil = LocalDate.now().getDayOfWeek().getValue() - 1;
			if (todayYoil >= 1 && todayYoil <= 5) {  // 주중만
			    mainCourseTimeParams.setCourseTimeYoil(todayYoil);
			}
			
			List<MainDTO> mainCourseTimeTable = new ArrayList<MainDTO>();
			mainCourseTimeTable = mainService.getMainCourseTimeTable(mainCourseTimeParams);
			boolean hasSchedule = !mainCourseTimeTable.isEmpty();
			
			
			// 메뉴권한 필터처리
			model.addAttribute("useAdmin", useAdmin);
			model.addAttribute("useStudent", useStudent);
			model.addAttribute("useProfessor", useProfessor);
			model.addAttribute("useStaff", useStaff);
			// 메인페이지 사용자 프로파일 카드
			model.addAttribute("userProfileCard", userProfileCard);
			// 메인 페이지 학교 공지사항
			model.addAttribute("mainUniversityNoticeList", mainUniversityNoticeList);
			// 메인 페이지 시간표
			model.addAttribute("mainCourseTimeTable", mainCourseTimeTable);
			// 메인 페이지 뷰에서 만약 리스트 빈값 이면 일정없습니다. 로 띄운다.
			model.addAttribute("hasSchedule", hasSchedule);
	
		    return "/main/main"; 
		}
		
		///////////////////////////////////////////////////////////////////////////////////
		
		// ================================================================================
	    // 1. 학교 공지사항 목록 조회 (모달 진입 시 AJAX 호출)
	    // ================================================================================
	    /**
	     * 공지사항 목록 모달에 표시할 데이터를 조회합니다. (AJAX)
	     */
	    @GetMapping("/getMainUniversityNoticeList") // AJAX 호출용 URL
	    @ResponseBody // JSON 데이터를 반환
	    public Map<String, Object> getMainUniversityNoticeList(
	            @RequestParam(defaultValue = "1") Integer page,
	            @RequestParam(defaultValue = "10") Integer limit) {
	        	
	        Map<String, Object> response = new HashMap<>();

	        Integer startRow = (page - 1) * limit;

	        // 1. 학교 전체 리스트 조회
	        List<UniversityNoticeDTO> universityNoticeList = universityNoticeService.universityNoticeList(startRow, limit);
	        
	        // 2. 전체 건수 조회
	        Integer totalCount = universityNoticeService.universityNoticeListCnt();

	        // 3. 총 페이지 수 계산
	        int totalPages = (int) Math.ceil((double) totalCount / limit);

	        // 4. 우선순위 Boolean 값 설정 (Mustache 조건부 렌더링을 위해)
	        for (UniversityNoticeDTO notice : universityNoticeList) {
	            Integer code = notice.getUniversityNoticePriorityCode();
	            notice.setIs_10(Integer.valueOf(10).equals(code)); //고정공지
	            notice.setIs_20(Integer.valueOf(20).equals(code)); //우선공지
	            notice.setIs_30(Integer.valueOf(30).equals(code)); //일반공지
	        }
	        
	        response.put("status", "success");
	        response.put("universityNoticeList", universityNoticeList);
	        response.put("currentPage", page);
	        response.put("totalPages", totalPages);
	        response.put("totalCount", totalCount);
	        response.put("searchUniversityNoticeCondition", "");
	        
	        return response;
	    }

	    // ================================================================================
	    // 3. 학교 공지사항 상세 정보 조회 (AJAX)
	    // ================================================================================
	    /**
	     * 특정 공지사항의 상세 정보를 조회합니다. (AJAX)
	     */
	    @GetMapping("/getMainUniversityNoticeDetail")
	    @ResponseBody
	    public Map<String, Object> getUniversityNoticeDetail(@RequestParam Integer universityNoticeNo) {

	        Map<String, Object> response = new HashMap<>();

	        // 1. 상세 정보를 조회 (Service에서 조회수 증가 처리 권장)
	        UniversityNoticeDTO universityNoticeDetail = universityNoticeService.selectUniversityNoticeDetail(universityNoticeNo);

	        if (universityNoticeDetail != null) {
	        	
	        	// 조회수 증가처리
	        	mainService.increaseUniversityNoticeViewCnt(universityNoticeNo);

	            response.put("status", "success");
	            response.put("data", universityNoticeDetail);
	        } else {

	            response.put("status", "fail");
	            response.put("message", "해당 학교 공지사항 번호를 찾을 수 없습니다.");
	        }

	        return response;
	    }
	    
	    // ================================================================================
	    // 5. 학교 공지사항 검색 조회 (AJAX - 리스트 갱신용)
	    // ================================================================================
	    /**
	     * 검색 조건에 따라 공지사항 리스트를 조회하고 페이징 정보를 반환합니다. (AJAX)
	     */
	    @GetMapping("/searchMainUniversityNotice")
	    @ResponseBody
	    public Map<String, Object> searchUniversityNotice(@RequestParam String searchUniversityNoticeCondition,
	                                          @RequestParam(value = "currentPage", defaultValue = "1") int pageNo) {

	        Map<String, Object> response = new HashMap<>();

	        int limit = 10;
	        int startRow = (pageNo - 1) * limit;

	        // 1. MyBatis 전달을 위한 파라미터 Map 설정
	        Map<String, Object> searchParams = new HashMap<>();
	        searchParams.put("searchUniversityNoticeCondition", searchUniversityNoticeCondition);
	        searchParams.put("startRow", startRow);
	        searchParams.put("limit", limit);

	        // 2. 검색 리스트 및 전체 카운트 조회
	        List<UniversityNoticeDTO> searchUniversityNoticeInfoList = universityNoticeService.searchUniversityNoticeInfoList(searchParams);
	        int totalCount = universityNoticeService.searchUniversityNoticeInfoListCnt(searchUniversityNoticeCondition);
	        int totalPages = (int) Math.ceil((double) totalCount / limit);

	        // 3. 우선순위 Boolean 값 설정
	        for (UniversityNoticeDTO notice : searchUniversityNoticeInfoList) {
	            Integer code = notice.getUniversityNoticePriorityCode();
	            notice.setIs_10(Integer.valueOf(10).equals(code)); //고정공지
	            notice.setIs_20(Integer.valueOf(20).equals(code)); //우선공지
	            notice.setIs_30(Integer.valueOf(30).equals(code)); //일반공지
	        }

	        // 4. 응답 Map에 데이터 추가
	        response.put("status", "success");
	        response.put("universityNoticeList", searchUniversityNoticeInfoList); // 변수명 통일 (프론트엔드 일관성)
	        response.put("totalCount", totalCount);
	        response.put("totalPages", totalPages); 
	        response.put("currentPage", pageNo);
	        response.put("searchUniversityNoticeCondition", searchUniversityNoticeCondition);

	        return response;
	    }

}
