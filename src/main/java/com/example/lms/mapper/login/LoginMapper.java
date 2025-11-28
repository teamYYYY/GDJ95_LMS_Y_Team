package com.example.lms.mapper.login;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.SysUserDTO;

/**
 * 
 * 2025. 11. 24.
 * Author - jm
 * 로그인 Mapper
 */
@Mapper
public interface LoginMapper {

	// 사용자 로그인 검증 ( user_id, user_password )
	Integer userLoginValidate(SysUserDTO sysUserDTO);
	
	// 계정 잠금 사용자 검증 ( user_id, user_password )
	Integer userStatusLockValidate(SysUserDTO sysUserDTO);

	// 사용자 로그인 실패 시 카운트 이력 증가 (5회 이상 실패시 계정잠금)
	Integer incrementUserLoginFailCnt(String userId);
	
	// 사용자 로그인 계정 잠금 처리 ( user_id )
	Integer userStatusLock(String userId);
	
	// 사용자 세션 정보
	List<SysUserDTO> loginUserSession(SysUserDTO sysUserDTO);
	
	// 사용자 비밀번호 초기화 처리
	Integer resetUserPassword(SysUserDTO sysUserDTO);
	
	// 로그인 실패 카운트 이력 초기화
	Integer resetUserLoginFailCnt(String userId);
}
