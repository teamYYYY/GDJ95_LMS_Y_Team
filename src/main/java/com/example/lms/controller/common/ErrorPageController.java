package com.example.lms.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.lms.controller.login.LoginController;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 12. 05.
 * Author - jm
 * 에러페이지 던지는 컨트롤러
 */
@Slf4j
@Controller
public class ErrorPageController {
	
	@GetMapping("/common/error")
	public String commonErrorPage() {
		
		return "/common/commonError";
	}

}
