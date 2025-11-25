package com.example.lms.controller.admin.deptManagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class DeptManagementController {
	
	// 내정보 - 학과관리
	@GetMapping("/myInfo/deptManagement")
	public String mDeptManagement() {
		
		return "/admin/deptManagement";
	}

}
