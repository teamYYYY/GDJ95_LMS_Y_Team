package com.example.lms.service.common.authorization;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.service.admin.AdminCommonMetaDataService;

import lombok.extern.slf4j.Slf4j;


/**
 * 2025. 12. 04.
 * Autor - JM
 * View단 검증관련 서비스
 */
@Slf4j
@Service
@Transactional
public class ViewAuthorizationValidateService {
	
	
	/**
	 * 
	 * 2025. 12. 04.
	 * Author - JM
	 * 메뉴, 네비게이션 사용자 접근 제한 분류를 위한 검증 기능 
	 * @param authCode
	 * @return
	 */
	public Map<String, Boolean> MenuAuthorizationValidate (String authCode) {
		
		/*
		 * AuthCode
		 * A001 -- 관리자
		 * G001 -- 일반직원
		 * P001 -- 교수
		 * S001 -- 학생
		 */
		
		Map<String, Boolean> menuValidateRsltMap = new HashMap<String, Boolean>();
		
		Boolean useAdmin = false;
		Boolean useStudent = false;
		Boolean useProfessor = false;
		Boolean useStaff = false;
		
		if ( authCode.equals("A001")) {
			
			useAdmin = true;
		} else if (authCode.equals("S001")) {
			
			useStudent = true;
		} else if (authCode.equals("P001")) {
			
			useProfessor = true;
		} else if (authCode.equals("G001")) {
			
			useStaff = true;
		}
		
		menuValidateRsltMap.put("useAdmin", useAdmin);
		menuValidateRsltMap.put("useStudent", useStudent);
		menuValidateRsltMap.put("useProfessor", useProfessor);
		menuValidateRsltMap.put("useStaff", useStaff);
		
		return menuValidateRsltMap;
	}
	
}
