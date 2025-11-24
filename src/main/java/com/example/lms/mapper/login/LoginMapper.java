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
	
	// 사용자 세션 정보
	List<SysUserDTO> loginUserSession(SysUserDTO sysUserDTO);
}
