package com.example.lms.controller.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lms.dto.SysUserDTO;
import com.example.lms.mapper.login.LoginMapper;
import com.example.lms.service.login.LoginService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 11. 24.
 * Author - jm
 * 사용자 로그인 컨트롤러
 */
@Slf4j
@Controller
public class LoginController {
	
	@Autowired
	private LoginService loginService;

	// 로그인 페이지 GET
	@GetMapping("/login")
    public String login(HttpSession session) {

		// 이미 로그인된 경우 메인으로 리다이렉트
        if (session.getAttribute("loginUser") != null) {
        
        	return "redirect:/main/main";
        }
        
        return "/login/login"; 
    }
	
    // 메인 페이지 GET
	@GetMapping("/main")
    public String main() {

        return "/main/main"; 
    }
	
	// 로그인 폼
	@PostMapping("/loginForm")
	public String loginForm( @RequestParam String userId, @RequestParam String userPassword, HttpSession session, RedirectAttributes redirectAttributes) {

	    SysUserDTO loginDto = new SysUserDTO();
	    loginDto.setUserId(userId);
	    loginDto.setUserPassword(userPassword);

	    log.info("loginDto.getUserId()" + loginDto.getUserId());
	    log.info("loginDto.getUserPassword()" + loginDto.getUserPassword());
	    
	    
	    // 1. 로그인 검증 ( 계정잠금 사용자 확인: 이전 시도에서 이미 잠금된 상태인지 확인)
	    int lockValid = loginService.userStatusLockValidate(loginDto);
	    
	    log.info("lockValid" + lockValid);
	    
	    // 이미 계정이 잠금 되어 있음
	    if (lockValid == 1) {
	    	
	    	// 계정 잠금 오류 메시지를 플래시 속성으로 전달
	        redirectAttributes.addFlashAttribute("lockError", "계정 비밀번호 5회 오류로 계정이 잠금되었습니다. 비밀번호를 초기화해주세요.");
	        // 여기서 userId를 다시 담아 아이디가 유지되도록 합니다.
	        redirectAttributes.addFlashAttribute("userId", userId);
	        return "redirect:/login";
	    }
	    
	    // 1.1 로그인 검증 ( 퇴학자, 계정잠금 사용자 필터 됨 )
	    int isValid = loginService.userLoginValidate(loginDto);

	    if (isValid == 1) {
	       
	    	// 🚨 1.2 로그인 성공 시: 실패 카운트 0으로 초기화 처리 (추가)
	        loginService.resetUserLoginFailCnt(userId); 
	        
	    	// 2. 사용자 정보 조회 (세션에 넣을 데이터)
	        List<SysUserDTO> userSessionInfo = loginService.loginUserSession(loginDto);

	        if (userSessionInfo != null && !userSessionInfo.isEmpty()) {
	        	
	            SysUserDTO loginUser = userSessionInfo.get(0);

	            // 3. 세션에 사용자 정보 저장
	            session.setAttribute("loginUser", loginUser);
	            // 세션 저장 기간 30분
	            session.setMaxInactiveInterval(1800); 

	            // 로그인 성공 로그 (운영 시 필수!)
	            log.info("LoginController$$loginForm == 로그인 성공: {}", userId);

	            return "redirect:/main";
	        }
	    }

	    // --- 로그인 실패 처리 로직 ---
	    
	    // 4. 로그인 실패 시 로그인 실패 카운트 이력 증가 처리
	    int loginFailCntChk = loginService.incrementUserLoginFailCnt(userId);
	    log.info("LoginController$$incrementUserLoginFailCnt == 실패 카운트 처리 확인 : " + loginFailCntChk);
	    
	    // 5. 로그인 실패 카운트이력이 5 인경우 계정락 처리
	    // 이 메서드(userStatusLock)가 계정 잠금(user_status=99)을 성공적으로 수행했다면, 1을 반환한다고 가정
	    int loginUserStatusLock = loginService.userStatusLock(userId);
	    log.info("LoginController$$loginUserStatusLock == 계정 락 처리 확인 : " + loginUserStatusLock);
	    
	    // 🚨 6. 방금 계정이 잠금 처리 되었는지 확인하여 메시지를 다르게 전달
	    if (loginUserStatusLock == 1) { 
	        // 계정이 방금 5회 실패로 인해 잠금 처리되었으므로 lockError를 전달
	        redirectAttributes.addFlashAttribute("lockError", "계정 비밀번호 5회 오류로 계정이 잠금되었습니다. 비밀번호를 초기화해주세요.");
	    } else {
	        // 일반적인 비밀번호 불일치 실패 메시지 전달
	        redirectAttributes.addFlashAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
	    }
	    
	    // 아이디는 항상 유지
	    redirectAttributes.addFlashAttribute("userId", userId); 

	    return "redirect:/login";
	}

	// 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
    	
        if (session != null) {
        	
            session.invalidate();
        }
        
        log.info("LoginController$$logout == 로그아웃 완료");
        return "redirect:/login";
    }
    
    // 비밀번호 초기화
    @GetMapping("/resetUserPassword")
    public String resetUserPassword() {
    	
    	return "/login/resetUserPassword";
    }
    
    //비밀번호 초기화폼 (AJAX 처리 방식)
    @PostMapping("/resetUserPasswordForm")
    @ResponseBody // 이 메서드가 뷰 이름 대신 JSON 데이터를 HTTP 응답 본문에 직접 기록하도록 지시
    public Map<String, Object> resetUserPasswordForm(SysUserDTO sysUserDTO) {
        
        Map<String, Object> response = new HashMap<String, Object>();
        
        log.info("getUserId : " + sysUserDTO.getUserId());
        log.info("getUserName : " + sysUserDTO.getUserName());
        log.info("getUserEmail : " + sysUserDTO.getUserEmail());
        log.info("getUserPhone : " + sysUserDTO.getUserPhone());
        
        log.info("loginService.resetUserPassword(sysUserDTO) : " + loginService.resetUserPassword(sysUserDTO));

        if (loginService.resetUserPassword(sysUserDTO) == 1) {

            log.info("LoginController$$resetUserPasswordForm == 비밀번호 초기화 처리 성공");

            int resetUserLoginFailCnt = loginService.resetUserLoginFailCnt(sysUserDTO.getUserId());
            
            if (resetUserLoginFailCnt == 1) {

            	log.info("LoginController$$resetUserLoginFailCnt == 로그인 실패 카운트 이력 초기화 성공");
            }
            
            // 프론트엔드 JS의 data.success = true로 인식되도록 설정
            response.put("success", true);
            // 추가적으로 메시지나 데이터를 보낼 수 있음 (예: response.put("tempPassword", "1234"))
            
        } else {

        	log.warn("LoginController$$resetUserPasswordForm == 비밀번호 초기화 처리 실패");
            
            // 프론트엔드 JS의 data.success = false로 인식되도록 설정
            response.put("success", false);
            // 프론트엔드에 에러 메시지를 표시하도록 메시지 추가
            response.put("message", "입력하신 정보(학번, 이름, 이메일, 핸드폰)가 일치하지 않아 초기화에 실패했습니다.");
        }

        // Map 객체가 JSON으로 변환되어 프론트엔드로 응답됨
        return response; 
    }
}
