package com.example.lms.controller.admin.userManagement;

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
import com.example.lms.service.admin.AdminCommonMetaDataService;
import com.example.lms.service.admin.SysAuthService;
import com.example.lms.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

/**
 * 2025. 11. 25.
 * Author - JM (Modified)
 * 시스템 사용자 관리 컨트롤러 (관리자 메뉴)
 */
@Slf4j
@Controller
@RequestMapping("/admin") // 공통 경로 매핑
public class UserManagementController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private AdminCommonMetaDataService adminCommonMetaDataService;
    
    // 사용자 세부권한 셀렉박스 기능관련 때문에 주입처리..
    @Autowired
    private SysAuthService sysAuthService;
    
    // ================================================================================
    // 1. 시스템 사용자 전체 조회 (페이지 진입 시 최초 로딩 + 페이징)
    // ================================================================================
    @GetMapping("/userManagement")
    public String userManagement(Model model, 
                                 @RequestParam(defaultValue = "1") Integer page, 
                                 @RequestParam(defaultValue = "10") Integer limit) {
        
        // 페이징 계산
        // page 1 -> startRow 0, page 2 -> startRow 10
        Integer startRow = (page - 1) * limit;
        
        // 1. 사용자 목록 조회
        List<Map<String, Object>> userInfoMapList = userService.userInfoMapList(startRow, limit);
        
        // 2. 전체 건수 조회 (페이징 처리를 위해)
        Integer totalCount = userService.selectSysUserCnt();
        
        // 3. 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / limit);
        
        // 4. 등록/수정 폼에 필요한 메타 데이터 조회 (학과, 권한 등)
        Map<String, Object> metaData = adminCommonMetaDataService.getAllSystemMetadata();
        
        log.info("totalCount : " + totalCount);
        
        model.addAttribute("userList", userInfoMapList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("metaData", metaData); // 모달 폼 렌더링용
        
        return "/admin/userManagement";
    }
    
    // ================================================================================
    // 2. 시스템 사용자 등록 (AJAX)
    // ================================================================================
    @PostMapping("/insertUserInfo")
    @ResponseBody
    public Map<String, Object> insertUserInfo(@RequestBody SysUserDTO insertSysUserDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 비밀번호 암호화 등의 로직은 서비스 계층에서 처리한다고 가정 (필수)
            // insertSysUserDTO.setUserPassword(passwordEncoder.encode(insertSysUserDTO.getUserPassword()));
            
            int result = userService.insertUserInfo(insertSysUserDTO);
            
            if (result == 1) {
                response.put("status", "success");
                response.put("message", "사용자가 성공적으로 등록되었습니다.");
            } else {
                response.put("status", "fail");
                response.put("message", "사용자 등록에 실패했습니다. 다시 시도해주세요.");
            }
        } catch (Exception e) {
            log.error("사용자 등록 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "서버 오류가 발생했습니다: " + e.getMessage());
        }
        
        return response;
    }
    
    // ================================================================================
    // 2. 1 특정 권한 코드에 해당하는 세부 권한 리스트 조회 (AJAX 전용)
    // ================================================================================
    @GetMapping("/getAuthDetailListByAuthCode")
    @ResponseBody
    public Map<String, Object> getAuthDetailListByAuthCode(@RequestParam("authCode") String authCode) {
        Map<String, Object> response = new HashMap<>();

        try {
            // SysAuthService에 있는 메서드를 호출하고 authCode를 파라미터로 전달
            List<SysAuthDTO> detailList = sysAuthService.seletcAuthCodesysAuthDetailList(authCode); 
            
            response.put("status", "success");
            response.put("detailList", detailList);
        } catch (Exception e) {
            log.error("세부 권한 조회 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "세부 권한 정보를 불러오는 데 실패했습니다.");
        }
        return response;
    }
    
    // ================================================================================
    // 3. 시스템 사용자 상세 정보 조회 (AJAX - 수정 화면 데이터 바인딩용)
    // ================================================================================
    @GetMapping("/getUserDetail")
    @ResponseBody
    public Map<String, Object> getUserDetail(@RequestParam String userId) {
        // 검색 조건을 ID로 설정하여 상세 정보 조회 (기존 검색 서비스 활용)
        // 단일 사용자 조회 서비스가 없다면 searchUserInfoMapList를 활용하거나 
        // userService.getUserById(userId) 같은 메서드를 추가하는 것이 좋습니다.
        
        List<Map<String, Object>> searchResult = userService.userInfoDetailMapList(userId);
        
        Map<String, Object> response = new HashMap<>();
        if (searchResult != null && !searchResult.isEmpty()) {
        	
        	Map<String, Object> originalData = searchResult.get(0);
            
            // 🚀 데이터 매핑 (Mapper 쿼리 이름을 JS/HTML 이름에 맞게 수정) 🚀
            
        	// 🚀 1. 세부 권한 코드 (Mapper의 'userAuth' -> JS/HTML의 'userDetailAuth') 🚀
            // 요청하신 변수명 관례에 따라 'authDetailCode'로 사용합니다.
            Object authDetailCode = originalData.get("userAuth"); 
            
            if (authDetailCode != null) {
                originalData.put("userDetailAuth", authDetailCode); 
                // 원래 키는 제거
                originalData.remove("userAuth"); 
            }
            
            // 🚀 2. 상위 권한 코드 (Mapper의 'authCode' -> JS/HTML의 'userAuth') 🚀
            Object userAuthCode = originalData.get("authCode"); // 명확성을 위해 'userAuthCode' 사용
            if (userAuthCode != null) {
                originalData.put("userAuth", userAuthCode); 
                // 원래 키는 제거
                originalData.remove("authCode"); 
            }
            
            // 3. (옵션) 기타 불필요하거나 중복되는 이름 제거
            originalData.remove("authDetailName");
            originalData.remove("authName");
            
            // -------------------------------------------------------------
            response.put("status", "success");
            response.put("data", originalData);
        } else {
            response.put("status", "fail");
            response.put("message", "사용자 정보를 찾을 수 없습니다.");
        }
        return response;
    }
    
    // ================================================================================
    // 4. 시스템 사용자 정보 수정 (AJAX)
    // ================================================================================
    @PostMapping("/updateUserInfoByAdmin")
    @ResponseBody
    public Map<String, Object> updateUserInfoByAdmin(@RequestBody SysUserDTO sysUserDTO) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            int result = userService.updateUserInfoByAdmin(sysUserDTO);
            
            if (result == 1) {
                response.put("status", "success");
                response.put("message", "사용자 정보가 성공적으로 수정되었습니다.");
            } else {
                response.put("status", "fail");
                response.put("message", "정보 수정에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("사용자 수정 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "서버 오류 발생: " + e.getMessage());
        }
        
        return response;
    }
    
    // ================================================================================
    // 5. 시스템 사용자 검색 조회 (AJAX - 리스트 갱신용)
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
    
    // ================================================================================
    // 6. 다수 사용자 계정 폐지 처리 (AJAX)
    // ================================================================================
    @PostMapping("/modifySysUserStatusRetire")
    @ResponseBody
    public Map<String, Object> modifySysUserStatusRetire(@RequestBody Map<String, List<Integer>> requestBody) {
        // @RequestBody로 List를 직접 받기 위해 Map으로 감싸서 받습니다. { "userNoList": [1, 2, 3] }
        List<Integer> retireUserNoList = requestBody.get("userNoList");
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (retireUserNoList == null || retireUserNoList.isEmpty()) {
                response.put("status", "fail");
                response.put("message", "선택된 사용자가 없습니다.");
                return response;
            }

            int result = userService.modifySysUserStatusRetire(retireUserNoList);
            
            if (result > 0) {
                response.put("status", "success");
                response.put("message", result + "명의 계정이 폐지 처리되었습니다.");
            } else {
                response.put("status", "fail");
                response.put("message", "계정 폐지 처리에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("계정 폐지 중 오류 발생", e);
            response.put("status", "error");
            response.put("message", "서버 오류 발생: " + e.getMessage());
        }
        
        return response;
    }
}