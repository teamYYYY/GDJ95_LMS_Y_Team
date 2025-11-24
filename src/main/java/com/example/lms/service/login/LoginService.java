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
	
	
	// 사용자 로그인 검증
	public Integer userLoginValidate(SysUserDTO sysUserDTO) {

		return loginMapper.userLoginValidate(sysUserDTO);
	}
	
	// 계정 잠금 사용자 검증 
	public Integer userStatusLockValidate(SysUserDTO sysUserDTO) {
		
		return loginMapper.userStatusLockValidate(sysUserDTO);
	}
	
	// 로그인 실패 시 카운트 이력 증가 (5회 실패시 계정잠금)
	public Integer incrementUserLoginFailCnt(String userId) {
		
		return loginMapper.incrementUserLoginFailCnt(userId);
	}
	
	// 사용자 로그인 계정 잠금 처리
	public Integer userStatusLock(String userId) {
		
		return loginMapper.userStatusLock(userId);
	}
	
	// 사용자 세션 정보 저장
	public List<SysUserDTO> loginUserSession(SysUserDTO sysUserDTO) {
		
		return loginMapper.loginUserSession(sysUserDTO);
	}
	
	// 사용자 비밀번호 초기화 처리
	public Integer resetUserPassword(SysUserDTO sysUserDTO) {
		
		return loginMapper.resetUserPassword(sysUserDTO);
	}
	
	
	// 로그인 실패 카운트 이력 초기화
	public Integer resetUserLoginFailCnt(String userId) {
		
		return loginMapper.resetUserLoginFailCnt(userId);
	}
}
