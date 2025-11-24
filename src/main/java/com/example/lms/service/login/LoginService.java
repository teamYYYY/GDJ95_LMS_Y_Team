package com.example.lms.service.login;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.SysUserDTO;
import com.example.lms.mapper.login.LoginMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * 2025. 11. 24.
 * Author - jm
 * 로그인 서비스
 */

@Slf4j
@Service
@Transactional
public class LoginService {
	
	@Autowired
	private LoginMapper loginMapper;
	
	
	// 사용자 로그인
	public Integer userLoginValidate(SysUserDTO sysUserDTO) {
		
		// 0 이면 false, 1이면 true
		return loginMapper.userLoginValidate(sysUserDTO);
	}
	
	// 사용자 세션 정보 저장
	public List<SysUserDTO> loginUserSession(SysUserDTO sysUserDTO) {
		
		return loginMapper.loginUserSession(sysUserDTO);
	}
}
