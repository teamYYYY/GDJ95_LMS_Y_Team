package com.example.lms.service.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.SysUserGradeDTO;
import com.example.lms.mapper.admin.SysUserGradeMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 11. 26.
 * Autor - JM
 * 시스템 사용자 상태 서비스
 */
@Slf4j
@Service
@Transactional
public class SysUserGradeService {

	@Autowired
	private SysUserGradeMapper sysUserGradeMapper;
	
	// 사용자 학생 학년 리스트
	public List<SysUserGradeDTO> sysUserGradeList() {
		
		return sysUserGradeMapper.sysUserGradeList();
	}
}
