package com.example.lms.service.admin;

import java.util.List;

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
	public List<SysAuthDTO> sysAuthAllList() {
		
		return sysAuthMapper.sysAuthAllList();
	}
	
	// 사용자 권한 리스트 조회
	public List<SysAuthDTO> sysAuthList() {
		
		return sysAuthMapper.sysAuthList();
	}
	
	// 사용자 세부권한 리스트 조회
	public List<SysAuthDTO> sysAuthDetailList() {
		
		return sysAuthMapper.sysAuthDetailList();
	}
	
	//특정 사용자 권한의 세부권한 리스트 조회 
	public List<SysAuthDTO> seletcAuthCodesysAuthDetailList(String authCode) {
		
		return sysAuthMapper.seletcAuthCodesysAuthDetailList(authCode);
	}
}
