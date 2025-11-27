package com.example.lms.service.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.SysUserDTO;
import com.example.lms.mapper.user.UserMapper;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * 2025. 11. 25. 
 * Author - JM
 * 사용자 인터페이스
 */
@Slf4j
@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserMapper userMapper;
	
	// 개인정보 관리 - 기본 정보 조회
	public Map<String, Object> userInfoMap(SysUserDTO sysUserDTO) {
		
		return userMapper.userInfoMap(sysUserDTO);
	}
	
	// 개인정보 관리 - 기본 정보 수정
	public Integer updateUserInfo(SysUserDTO sysUserDTO) {
		
		return userMapper.updateUserInfo(sysUserDTO);
	}
	
	// 개인정보 관리 - 비밀번호 수정
	public Integer modifyUserInfoPassword(Map<String, Object> passwordData) {
		
		return userMapper.modifyUserInfoPassword(passwordData);
	}
	
	
	/*
	 * 
	 * 이 하단 부터는 userManagementController
	 *
	 */
	
	// 시스템사용자 관리 - 시스템 사용자 전체 조회
	public List<Map<String, Object>> userInfoMapList(Integer startRow, Integer limit) {
		
		return userMapper.userInfoMapList(startRow, limit);
	}
	
	// 시스템사용자관리 - 사용자 등록
	public Integer insertUserInfo(SysUserDTO sysUserDTO) {
		
		return userMapper.insertUserInfo(sysUserDTO);
	}
	
	// 시스템사용자 관리 - 시스템 사용자 정보 수정
	public Integer updateUserInfoByAdmin(SysUserDTO sysUserDTO) {
		
		return userMapper.updateUserInfoByAdmin(sysUserDTO);
	}
	
	// 시스템사용자 관리 - 시스템 사용자 검색 조회 ( 학번이나 사용자명으로 검색 // 같은 이름의 사용자 가능성 있음)
	public List<Map<String, Object>> searchUserInfoMapList(String searchUserCondition, Integer startRow, Integer limit) {
		
		return userMapper.searchUserInfoMapList(searchUserCondition, startRow, limit );
	}
	
	// 시스템사용자 관리 - 시스템 사용자 검색 조회 카운트 ( 페이징 시 사용 )
	public Integer searchUserInfoMapListCnt(String searchUserCondition) {
		
		return userMapper.searchUserInfoMapListCnt(searchUserCondition);
	}
	
	// 시스템사용자 관리 - 시스템 전체 사용자 수 ( 페이징 시 사용 )
	public Integer selectSysUserCnt() {
		
		return userMapper.selectSysUserCnt();
	}
	
	public List<Map<String, Object>> userInfoDetailMapList(String userId) {
		
		return userMapper.userInfoDetailMapList(userId);
	}
	
	// 시스템사용자 관리 - 다수 사용자 계정 폐지 처리 기능
	public Integer modifySysUserStatusRetire(List<Integer> retireUserNoList) {
		
		return userMapper.modifySysUserStatusRetire(retireUserNoList);
	}
}
