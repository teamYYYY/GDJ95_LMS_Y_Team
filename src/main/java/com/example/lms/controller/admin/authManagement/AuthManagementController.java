package com.example.lms.controller.admin.authManagement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.lms.service.admin.DeptService;
import com.example.lms.service.admin.SysAuthService;
import com.example.lms.service.admin.SysUserGradeService;
import com.example.lms.service.admin.SysUserStatusService;
import com.example.lms.service.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class AuthManagementController {
	

	// 내정보 - 권한관리
	@GetMapping("/myInfo/authManagement")
	public String mAuthManagement() {
		
		return "/admin/userManagement";
	}

}
