package com.example.lms.mapper.user;

import java.util.List;

import com.example.lms.dto.SysUserDTO;

/**
 * 
 * 2025. 11. 24.
 * Author - jm
 * 사용자 Mapper
 */
public interface UserMapper {
	
	// 사용자 리스트 출력
	List<SysUserDTO> userList(SysUserDTO sysUserDTO);
}
