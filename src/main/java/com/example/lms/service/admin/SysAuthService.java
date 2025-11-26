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
	
	// 학과 관리 - 학과 정보 조회
	public List<SysAuthDTO> sysAuthList() {
		
		return sysAuthMapper.sysAuthList();
	}
}
