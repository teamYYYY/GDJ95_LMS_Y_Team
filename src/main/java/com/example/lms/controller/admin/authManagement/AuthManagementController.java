package com.example.lms.controller.admin.authManagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthManagementController {

	// 내정보 - 권한관리
	@GetMapping("/myInfo/authManagement")
	public String mAuthManagement() {
		
		return "/admin/authManagement";
	}

}
