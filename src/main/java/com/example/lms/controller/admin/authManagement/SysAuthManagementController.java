package com.example.lms.controller.admin.authManagement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.lms.dto.SysAuthDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.admin.DeptService;
import com.example.lms.service.admin.SysAuthService;
import com.example.lms.service.admin.SysUserGradeService;
import com.example.lms.service.admin.SysUserStatusService;
import com.example.lms.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin") // 공통 경로 매핑
public class SysAuthManagementController {
	
	@Autowired
	private SysAuthService sysAuthService;

    // ================================================================================
    // 1. 사용자 권한 관리 조회 (페이지 진입 시 최초 로딩 + 페이징)
    // ================================================================================
	@GetMapping("/sysAuthManagement")
	public String mAuthManagement(Model model,
            @RequestParam(defaultValue = "1") Integer page, 
            @RequestParam(defaultValue = "10") Integer limit) {
		
		// 페이징 계산
        // page 1 -> startRow 0, page 2 -> startRow 10
        Integer startRow = (page - 1) * limit;
		
		// 사용자 권한 전체 리스트
		List<SysAuthDTO> sysAuthAllList = sysAuthService.sysAuthAllList(startRow, limit);
		
		// 전체 건수 조회 ( 페이징 처리 )
		Integer totalCount = sysAuthService.sysAuthAllListCnt();
		
		// 총 페이지 수 계산
		int totalPages = (int) Math.ceil((double) totalCount / limit);
		
		log.info("totalCount : " + totalCount);
		
		model.addAttribute("sysAuthAllList", sysAuthAllList);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalCount", totalCount);
		
		return "/admin/sysAuthManagement";
	}
	
    // ================================================================================
    // 2. 사용자 권한 등록 (AJAX)
    // ================================================================================
	@PostMapping("/insertSysAuth")
    @ResponseBody
    public Map<String, Object> insertSysAuth(@RequestBody SysAuthDTO insertSysAuthDTO) {
        
		Map<String, Object> response = new HashMap<>();
        
        try {
            
            int result = sysAuthService.insertSysAuth(insertSysAuthDTO);
            
            if (result == 1) {
            	
            	int result2 = sysAuthService.insertSysAuthDetail(insertSysAuthDTO);
            	
            	if ( result2 == 1 ) {
            		
            	   response.put("status", "success");
                   response.put("message", "권한 및 세부 권한이 성공적으로 등록되었습니다.");
            	} else {
            		
            	  response.put("status", "fail");
                  response.put("message", "세부 권한 등록에 실패했습니다. 다시 시도해주세요.");
            	}
            	
            } else {
            	
            	response.put("status", "fail");
                response.put("message", "권한 등록에 실패했습니다. 다시 시도해주세요.");
            }
        } catch (Exception e) {
            log.error("권한 등록 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
	
	// ================================================================================
    // 3. 시스템 사용자 권한 코드 상세 정보 조회 (AJAX - 수정 화면 데이터 바인딩용)
    // ================================================================================
    @GetMapping("/getSysAuthAllDetail")
    @ResponseBody
    public Map<String, Object> getSysAuthAllDetail(@RequestParam String authDetailCode) {
        
    	Map<String, Object> response = new HashMap<>();
    	
    	List<SysAuthDTO> selectSysAuthAllDetailList = sysAuthService.selectSysAuthAllDetailList(authDetailCode);
    	
    	
        return response;
    }
    
    // ================================================================================
    // 4. 시스템 사용자 권한 코드 상세 정보 수정 (AJAX - 수정 화면 데이터 바인딩용)
    // ================================================================================
    @GetMapping("/updateSysAuth")
    @ResponseBody
    public Map<String, Object> updateSysAuth(@RequestBody SysAuthDTO insertSysAuthDTO) {
        
    	Map<String, Object> response = new HashMap<>();
    	
    	// 검증 사용자테이블에 관련 연동 부서 데이터 있으면 수정불가 
    	int updateRemoveValidate = sysAuthService.updateRemoveSysAuthDetailValidate(insertSysAuthDTO.getAuthDetailCode());
    	
    	
    	
    	
    	
    	
    	if ( updateRemoveValidate < 1 ) {
    		
    	  // 수정가능
    	  // 권한코드 수정
          int updateSysAuthResult = sysAuthService.updateSysAuth(insertSysAuthDTO);
          
          //세부 권한 코드 수정
      	  int updateSysAuthDetailResult = sysAuthService.updateSysAuthDetail(insertSysAuthDTO);
       	  
    	} else {
    		
    	  //수정불가
      	  response.put("status", "fail");
          response.put("message", "사용자 테이블에 관련 권한코드가 있어 수정 실패하였습니다.");
    	}
    	
        return response;
    }
    
    // ================================================================================
    // 5. 사용자 권한 코드 조회 (AJAX - 리스트 갱신용)
    // ================================================================================
    @GetMapping("/searchSysAuthInfo")
    @ResponseBody
    public Map<String, Object> searchUserInfo(@RequestParam String searchSysAuthCondition,
    		@RequestParam(value = "currentPage", defaultValue = "1") int pageNo) {
        Map<String, Object> response = new HashMap<>();
        
        int limit = 10;      // 페이지당 사용자 수
        // 💡 시작 행 계산: (현재 페이지 - 1) * 페이지당 개수
        int startRow = (pageNo - 1) * limit;
        
        List<SysAuthDTO> searchSysAuthInfoList = sysAuthService.searchSysAuthInfoList(searchSysAuthCondition, startRow, limit);
        
        // 2. 전체 개수 카운트
        int totalCount = sysAuthService.searchSysAuthInfoListCnt(searchSysAuthCondition);
        
        response.put("status", "success");
        response.put("userList", searchSysAuthInfoList);
        // 💡 응답에 페이징 정보 포함
        response.put("totalCount", totalCount); 
        response.put("currentPage", pageNo);
        
        return response;
    }
    
    
    // ================================================================================
    // 6. 다수 사용자 권한 코드 삭제 처리 (AJAX)
    // ================================================================================
    @PostMapping("/removeSysAuth")
    @ResponseBody
    public Map<String, Object> removeSysAuth(@RequestParam String authDetailCode) {
        
    	// 검증 사용자테이블에 관련 연동 부서 데이터 있으면 수정불가 
    	int updateRemoveValidate = sysAuthService.updateRemoveSysAuthDetailValidate(authDetailCode);
    	
    	//삭제 전에 auth_code 따기
    	String authCode = sysAuthService.selectBeforeRemoveAuthCd(authDetailCode);
    	
        Map<String, Object> response = new HashMap<>();
        
        try {
        	
        	if ( updateRemoveValidate < 0) {
        		
        		// 삭제 처리 가능
        		// 세부권한테이블부터 삭제
            	int removeSysAuthDetailResult = sysAuthService.removeSysAuthDetail(authDetailCode);
            	
            	if ( removeSysAuthDetailResult == 1 ) {
            		
            		log.info("SysAuthManagementController$$removeSysAuthDetail 성공" );
            		// 권한테이블 삭제
            		int removeSysAuthResult = sysAuthService.removeSysAuth(authCode);
            		
            		if ( removeSysAuthResult == 1 ) {
            			
            			log.info("SysAuthManagementController$$removeSysAuth 성공" );
            		}
            	} else {
            		
            		log.info("SysAuthManagementController$$removeSysAuth 실패");
            	}
        		
            } else {
            	
            	//삭제 처리 불가능
            	log.info("SysAuthManagementController$$removeSysAuthDetail 실패" );
            }
        } catch (Exception e) {
            log.error("권한 삭제 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "서버 오류 발생: " + e.getMessage());
        }
        
        return response;
    }

}
