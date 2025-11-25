package com.example.lms.mapper.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.SysUserDTO;

/**
 * 
 * 2025. 11. 24.
 * Author - jm
 * 사용자 Mapper
 */

@Mapper
public interface UserMapper {
	
	// 개인정보 관리 - 기본 정보 조회
	Map<String, Object> userInfoMap(SysUserDTO sysUserDTO);
	
	// 개인정보 관리 - 기본 정보 수정
	Integer updateUserInfo(SysUserDTO sysUserDTO);
	
	// 개인정보 관리 - 비밀번호 수정
	Integer modifyUserInfoPassword(Map<String, Object> passwordData);
	
	// 시스템사용자 관리 - 시스템 사용자 전체 조회
	List<Map<String, Object>> userInfoMapList();
	
	// 시스템사용자관리 - 사용자 등록
	Integer insertUserInfo(SysUserDTO sysUserDTO);
	
	// 시스템사용자 관리 - 시스템 사용자 정보 수정
	Integer updateUserInfoByAdmin(SysUserDTO sysUserDTO);
	
	// 시스템사용자 관리 - 시스템 사용자 검색 조회 ( 학번이나 사용자명으로 검색 // 같은 이름의 사용자 가능성 있음)
	List<Map<String, Object>> searchUserInfoMapList(SysUserDTO sysUserDTO);
}
