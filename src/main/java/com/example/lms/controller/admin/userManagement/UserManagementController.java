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

import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.admin.AdminCommonMetaDataService;
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
    // 3. 시스템 사용자 상세 정보 조회 (AJAX - 수정 화면 데이터 바인딩용)
    // ================================================================================
    @GetMapping("/getUserDetail")
    @ResponseBody
    public Map<String, Object> getUserDetail(@RequestParam String userId) {
        // 검색 조건을 ID로 설정하여 상세 정보 조회 (기존 검색 서비스 활용)
        // 단일 사용자 조회 서비스가 없다면 searchUserInfoMapList를 활용하거나 
        // userService.getUserById(userId) 같은 메서드를 추가하는 것이 좋습니다.
        
        // 여기서는 기존 searchUserInfoMapList를 활용한다고 가정 (List의 첫 번째 요소 반환)
        List<Map<String, Object>> searchResult = userService.searchUserInfoMapList(userId);
        
        Map<String, Object> response = new HashMap<>();
        if (searchResult != null && !searchResult.isEmpty()) {
            response.put("status", "success");
            response.put("data", searchResult.get(0));
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
    public Map<String, Object> searchUserInfo(@RequestParam String searchCondition) {
        Map<String, Object> response = new HashMap<>();
        
        List<Map<String, Object>> searchList = userService.searchUserInfoMapList(searchCondition);
        
        response.put("status", "success");
        response.put("userList", searchList);
        
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