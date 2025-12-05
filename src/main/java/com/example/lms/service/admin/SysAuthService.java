package com.example.lms.service.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.SysAuthDTO;
import com.example.lms.mapper.admin.SysAuthMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 11. 26.
 * Autor - JM
 * 시스템 권한 서비스
 */
@Slf4j
@Service
@Transactional
public class SysAuthService {

	@Autowired
	private SysAuthMapper sysAuthMapper;
	
	// 사용자 권한 전체 리스트 조회
	public List<SysAuthDTO> sysAuthAllList(Integer startRow, Integer limit) {
		
		return sysAuthMapper.sysAuthAllList(startRow, limit);
	}
	
	///////////시스템사용자 관리에서 셀렉박스 기능 관련 메서드///////////////////
	
	// 사용자 권한 리스트 조회
	public List<SysAuthDTO> sysAuthList() {
		
		return sysAuthMapper.sysAuthList();
	}
	
	// 사용자 세부권한 리스트 조회
	public List<SysAuthDTO> sysAuthDetailList() {
		
		return sysAuthMapper.sysAuthDetailList();
	}
	
	//특정 사용자 권한의 세부권한 리스트 조회 
	public List<SysAuthDTO> selectAuthCodesysAuthDetailList(String authCode) {
		
		return sysAuthMapper.selectAuthCodesysAuthDetailList(authCode);
	}
	
	/////////////////////////////////////////////////////////////////

	// 사용자 권한 전체 리스트 페이징
	public Integer sysAuthAllListCnt() {
		
		return sysAuthMapper.sysAuthAllListCnt();
	}
	
	// 사용자 권한 코드 검색 조회
	public List<SysAuthDTO> searchSysAuthInfoList(Map<String, Object> searchParams) {

		return sysAuthMapper.searchSysAuthInfoList(searchParams);
	}
	
	// 사용자 권한 코드 검색 조회 카운트
	public Integer searchSysAuthInfoListCnt(String searchSysAuthCondition) {
		
		return sysAuthMapper.searchSysAuthInfoListCnt(searchSysAuthCondition);
	}
											
	// 사용자 권한 상세정보 조회
	public SysAuthDTO selectSysAuthAllDetail(String authDetailCode) {
		
		return sysAuthMapper.selectSysAuthAllDetail(authDetailCode);
	}
	
	// 사용자 권한 등록전에 권한코드가 이미 있을 경우에는 Detail만 인서트하면 된다.
	public Integer insertSysAuthExistChk(SysAuthDTO sysAuthDTO) {
		
		return sysAuthMapper.insertSysAuthExistChk(sysAuthDTO);
	}
	
	// 사용자 세부권한 등록 시 동일한 세부권한코드나 세부권한명이 존재하면 안된다.
	public Integer insertSysAuthDetailExistChk(SysAuthDTO sysAuthDTO) {
		
		return sysAuthMapper.insertSysAuthDetailExistChk(sysAuthDTO);
	}
	
	//======================================================
	// 사용자 권한 등록 기능 insert 순서 sysAuth -> sysAuthDetail
	//======================================================
	public Integer insertSysAuth(SysAuthDTO sysAuthDTO) {
		
		return sysAuthMapper.insertSysAuth(sysAuthDTO);
	}

	public Integer insertSysAuthDetail(SysAuthDTO sysAuthDTO) {
	
		return sysAuthMapper.insertSysAuthDetail(sysAuthDTO);
	}
	
	// 사용자 권한 수정 및 삭제 기능 사용시 검증기능 (사용자 테이블에 해당 사용하는 코드가 있을 시 변경불가)
	public Integer updateRemoveSysAuthDetailValidate(String authDetailCode) {
		
		return sysAuthMapper.updateRemoveSysAuthDetailValidate(authDetailCode);
	}
	
	//======================================================
	// 사용자 권한 수정 기능 update 순서 sysAuthDetail -> sysAuth  
	//======================================================
	
	public Integer updateSysAuthDetail(SysAuthDTO sysAuthDTO) {
		
		return sysAuthMapper.updateSysAuthDetail(sysAuthDTO);
	}
	
	public Integer updateSysAuth(SysAuthDTO sysAuthDTO) {
		
		return sysAuthMapper.updateSysAuth(sysAuthDTO);
	}
	
	//======================================================
	// 사용자 권한 삭제 기능 remove 순서 sysAuth -> sysAuthDetail
	//======================================================
	
	// 삭제 전에 auth_code 가져와두기
	public String selectBeforeRemoveAuthCd(String authDetailCode) {
		
		return sysAuthMapper.selectBeforeRemoveAuthCd(authDetailCode);
	}
	
	public Integer removeSysAuthDetail(String authDetailCode) {
		
		return sysAuthMapper.removeSysAuthDetail(authDetailCode);
	}
	
	// 권한코드 삭제 전에 세부사항에 남아 있으면 삭제 불가
	public Integer selectBeforeRemoveAuthCdValidate(String authCode) {
		
		return sysAuthMapper.selectBeforeRemoveAuthCdValidate(authCode);
	}
	
	public Integer removeSysAuth(String authCode) {
		
		return sysAuthMapper.removeSysAuth(authCode);
	}
}

