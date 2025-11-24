package com.example.lms.controller.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.login.LoginService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	// 로그인 페이지
	@GetMapping("/login")
    public String login(HttpSession session) {

		// 이미 로그인된 경우 메인으로 리다이렉트
        if (session.getAttribute("loginUser") != null) {
        
        	return "redirect:/main/main";
        }
        
        return "/login/login"; 
    }
	
	// 로그인 폼
	@PostMapping("/loginForm")
    public String loginForm( @RequestParam String userId, @RequestParam String userPassword, HttpSession session, RedirectAttributes redirectAttributes) {

        SysUserDTO loginDto = new SysUserDTO();
        loginDto.setUserId(userId);
        loginDto.setUserPassword(userPassword);

        // 1. 로그인 검증
        int isValid = loginService.userLoginValidate(loginDto);

        if (isValid == 1) {
           
        	// 2. 사용자 정보 조회 (세션에 넣을 데이터)
            List<SysUserDTO> userSessionInfo = loginService.loginUserSession(loginDto);

            if (userSessionInfo != null && !userSessionInfo.isEmpty()) {
            	
                SysUserDTO loginUser = userSessionInfo.get(0);

                // 3. 세션에 사용자 정보 저장
                session.setAttribute("loginUser", loginUser);
                session.setMaxInactiveInterval(1800); // 30분

                // 로그인 성공 로그 (운영 시 필수!)
                log.info("로그인 성공: {}", userId);

                return "redirect:/main";
            }
        }

        // 로그인 실패
        redirectAttributes.addFlashAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
        redirectAttributes.addFlashAttribute("username", userId); // 아이디 유지

        return "redirect:/login";
    }
	
    //로그인 시 메인 페이지로 이동
	@GetMapping("/main")
    public String main() {

        return "/main/main"; 
    }
	

	// 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        log.info("로그아웃 완료");
        return "redirect:/login";
    }
}
