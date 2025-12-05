package com.example.lms.mapper.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.SysAuthDTO;

/**
 * 
 * 2025. 11. 26.
 * Author - jm
 * 사용자 권한 Mapper
 */
@Mapper
public interface SysAuthMapper {

	
	// 사용자 권한 코드 전체 리스트 조회
	List<SysAuthDTO> sysAuthAllList(@Param("startRow") Integer startRow, @Param("limit") Integer limit);	
	///////////시스템사용자 관리에서 셀렉박스 기능 관련 메서드///////////////////
	
	// 사용자 권한 리스트 조회
	List<SysAuthDTO> sysAuthList();
	
	// 사용자 세부권한 리스트 조회
	List<SysAuthDTO> sysAuthDetailList();
	
	//특정 사용자 권한의 세부권한 리스트 조회 
	List<SysAuthDTO> selectAuthCodesysAuthDetailList(String authCode);
	
	/////////////////////////////////////////////////////////////////

	// 사용자 권한 코드 전체 리스트 페이징
	Integer sysAuthAllListCnt();
	
	// 사용자 권한 코드 검색 조회
	List<SysAuthDTO> searchSysAuthInfoList(Map<String, Object> searchParams);
	
	// 사용자 권한 코드 검색 조회 카운트
	Integer searchSysAuthInfoListCnt(@Param("searchSysAuthCondition") String searchSysAuthCondition);
	
	// 사용자 권한 코드 상세정보 조회
	SysAuthDTO selectSysAuthAllDetail(String authDetailCode);
	
	// 사용자 권한 등록전에 권한코드가 이미 있을 경우에는 Detail만 인서트하면 된다.
	Integer insertSysAuthExistChk(SysAuthDTO sysAuthDTO);
	
	// 사용자 세부권한 등록 시 동일한 세부권한코드나 세부권한명이 존재하면 안된다.
	Integer insertSysAuthDetailExistChk(SysAuthDTO sysAuthDTO);
	
	//======================================================
	// 사용자 권한 등록 기능 insert 순서 sysAuth -> sysAuthDetail
	//======================================================
	
	Integer insertSysAuth(SysAuthDTO sysAuthDTO);

	Integer insertSysAuthDetail(SysAuthDTO sysAuthDTO);
	
	// 사용자 권한 코드 수정 및 삭제 기능 사용시 검증기능 (사용자 테이블에 해당 사용하는 코드가 있을 시 변경불가)
	Integer updateRemoveSysAuthDetailValidate(String authDetailCode);
	
	//======================================================
	// 사용자 권한 코드 수정 기능 update 순서 sysAuthDetail -> sysAuth 
	//======================================================
	
	Integer updateSysAuthDetail(SysAuthDTO sysAuthDTO);
	
	Integer updateSysAuth(SysAuthDTO sysAuthDTO);
	
	//======================================================
	// 사용자 권한 코드 삭제 기능 remove 순서 sysAuthDetail -> sysAuth 
	//======================================================
	
	// 삭제전에 auth_code 가져와두기
	String selectBeforeRemoveAuthCd(String authDetailCode);
	
	Integer removeSysAuthDetail(String authDetailCode);
	
	// 권한코드 삭제 전에 세부사항에 남아 있으면 삭제 불가
	Integer selectBeforeRemoveAuthCdValidate(String authCode);
	
	Integer removeSysAuth(String authCode);
}