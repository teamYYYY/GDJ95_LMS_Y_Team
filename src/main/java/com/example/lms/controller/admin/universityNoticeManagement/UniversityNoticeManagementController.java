package com.example.lms.controller.admin.universityNoticeManagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class UniversityNoticeManagementController {
	
	// 내정보 - 공지사항관리
	@GetMapping("/myInfo/universityNotice")
	public String mUniversityNotice() {
		
		return "/admin/universityNotice";
	}
}
