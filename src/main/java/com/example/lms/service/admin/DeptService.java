package com.example.lms.service.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.SysAuthDTO;
import com.example.lms.mapper.admin.DeptMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 11. 28.
 * Autor - JM
 * 시스템 학과 서비스
 */
@Slf4j
@Service
@Transactional
public class DeptService {
	
	@Autowired
	private DeptMapper deptMapper;
	
	// 시스템 사용자 관리 - 학과 코드 조회
	public List<DeptDTO> useMangementDeptList() {
		
		return deptMapper.useMangementDeptList();
	}
	
	// 학과 관리 - 학과 코드 전체 리스트 조회
	public List<DeptDTO> deptList(Integer startRow, Integer limit) {
		
		return deptMapper.deptList(startRow, limit);
	}
	
	// 학과 코드 전체 리스트 페이징
	public Integer deptListCnt() {
		
		return deptMapper.deptListCnt();
	}
	
	// 학과 코드 검색 조회
	public List<DeptDTO> searchDeptInfoList(Map<String, Object> searchParams) {
		
		return deptMapper.searchDeptInfoList(searchParams);
	}
	
	// 학과 코드 검색 조회 카운트
	public Integer searchDeptInfoListCnt(String searchDeptCondition) {
		
		return deptMapper.searchDeptInfoListCnt(searchDeptCondition);
	}
	
	// 학과 코드 상세정보 조회
	public DeptDTO selectDeptDetail(String deptCode) {
		
		return deptMapper.selectDeptDetail(deptCode);
	}
	
	// 학과 코드 등록, 수정, 삭제 전에 한번 검증 하기
	public Integer insertUpdRemvDeptValidate(String deptCode) {
		
		return deptMapper.insertUpdRemvDeptValidate(deptCode);
	}
	
	//======================================================
	// 학과 코드 등록 기능
	//======================================================
	public Integer insertDept(DeptDTO deptDTO) {
		
		return deptMapper.insertDept(deptDTO);
	}
	
	// 학과 코드 수정 및 삭제 기능 사용시 검증기능 (사용자 테이블에 해당 사용하는 코드가 있을 시 변경불가)
	public Integer updateRemoveDeptValidate(String deptCode) {
		
		return deptMapper.updateRemoveDeptValidate(deptCode);
	}
	
	//======================================================
	// 학과 코드 수정 기능
	//======================================================
	
	public Integer updateDept(DeptDTO deptDTO) {
		
		return deptMapper.updateDept(deptDTO);
	}
	
	//======================================================
	// 학과 코드 삭제 기능
	//======================================================
	
	public Integer removeDept(String deptCode) {
		
		return deptMapper.removeDept(deptCode);
	}
}
