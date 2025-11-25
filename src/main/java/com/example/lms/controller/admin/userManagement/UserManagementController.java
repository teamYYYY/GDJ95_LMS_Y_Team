package com.example.lms.controller.admin.userManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.user.UserService;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * 2025. 11. 25.
 * Author - tester
 * 시스템 사용자 관리 컨트롤러 ( 관리자 메뉴 )
 */
@Slf4j
@Controller
public class UserManagementController {

	@Autowired
	private UserService userService;
	
	// 내정보 - 시스템사용자관리 - 시스템 사용자 전체 조회
	@GetMapping("/admin/userManagement")
	public String userManagement(Model model) {
		
		List<Map<String, Object>> userInfoMapList = new ArrayList<Map<String,Object>>();
		
		userInfoMapList = userService.userInfoMapList();
		
		log.info("userInfoMapList 조회 완료: {}", userInfoMapList);
		model.addAttribute(userInfoMapList);
		
		return "/admin/userManagement";
	}
	
	// 내정보 - 시스템사용자관리 - 시스템 사용자 등록
	@PostMapping("/admin/insertUserInfo")
	public String insertUserInfo(@ModelAttribute SysUserDTO insertSysUserDTO, RedirectAttributes redirectAttributes) {
		
		Integer InsertResult = userService.insertUserInfo(insertSysUserDTO);
		
		if ( InsertResult == 1) {
			
			log.info("UserManagementController$$insertUserInfo : 사용자 등록 성공");
			return "redirect:/admin/userManagement";
		} else {

			log.warn("UserManagementController$$insertUserInfo : 사용자 등록 실패. DB 인서트 행 수: {}", InsertResult);
	        
	        // 실패 메시지 전달
	        // 뷰 단에서 이 'error' 메시지를 받아서 모달 등으로 띄워야 합니다.
	        redirectAttributes.addFlashAttribute("error", "개인 정보 변경에 실패했습니다. 입력 정보를 확인해 주세요.");
	        
	        // 현재 편집 화면에 유지
	        // '/admin/userManagement' 페이지에서 `?edit=true`와 같은 파라미터로 편집 모드를 유지한다고 가정
	        return "redirect:/admin/userManagement?edit=true"; 
		}
	}
	
	// 내정보 - 시스템사용자관리 - 시스템 사용자 정보 수정
	
	
	// 내정보 - 시스템사용자관리 - 시스템 사용자 검색 조회

}
