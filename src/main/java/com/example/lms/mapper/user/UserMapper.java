package com.example.lms.mapper.user;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	
	// 시스템사용자 관리 - 시스템 사용자 전체 조회 1페이지 10명단위
	List<Map<String, Object>> userInfoMapList(@Param("startRow") Integer startRow, @Param("limit") Integer limit);
	
	// 시스템사용자관리 - 사용자 등록
	Integer insertUserInfo(SysUserDTO sysUserDTO);
	
	// 시스템사용자 관리 - 시스템 사용자 정보 수정
	Integer updateUserInfoByAdmin(SysUserDTO sysUserDTO);
	
	// 시스템사용자 관리 - 시스템 사용자 검색 조회 ( 학번이나 사용자명으로 검색 // 같은 이름의 사용자 가능성 있음)
	List<Map<String, Object>> searchUserInfoMapList(@Param("searchUserCondition") String searchUserCondition, 
													@Param("startRow") int startRow,
													@Param("limit") int limit );

	// 시스템사용자 관리 - 시스템 사용자 검색 조회 카운트 ( 페이징 시 사용 )	
	Integer searchUserInfoMapListCnt(@Param("searchUserCondition") String searchUserCondition);
	
	// 시스템사용자 관리 - 시스템 전체 사용자 수 ( 페이징 시 사용 )
	Integer selectSysUserCnt();
	
	// 시스템사용자 관리 - 사용자 상세정보 조회
	List<Map<String, Object>> userInfoDetailMapList(String userId);
	
	// 시스템사용자 관리 - 다수 사용자 계정 폐지 처리 기능 
	Integer modifySysUserStatusRetire(List<Integer> retireUserNoList);
}
