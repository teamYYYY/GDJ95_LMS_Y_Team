package com.example.lms.mapper.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.DeptDTO;

/**
 * 
 * 2025. 11. 28.
 * Author - jm
 * 학과 Mapper
 */
@Mapper
public interface DeptMapper {
	
	// 시스템 사용자 관리 - 학과 코드 조회
	List<DeptDTO> useMangementDeptList();
	
	// 학과 코드 전체 리스트 조회
	List<DeptDTO> deptList(@Param("startRow") Integer startRow, @Param("limit") Integer limit);	
	
	// 학과 코드 전체 리스트 페이징
	Integer deptListCnt();
	
	// 학과 코드 검색 조회
	List<DeptDTO> searchDeptInfoList(Map<String, Object> searchParams);
	
	// 학과 코드 검색 조회 카운트
	Integer searchDeptInfoListCnt(@Param("searchDeptCondition") String searchDeptCondition);
	
	// 학과 코드 상세정보 조회
	DeptDTO selectDeptDetail(String deptCode);
	
	// 학과 코드 등록 전에 한번 검증 하기
	Integer insertUpdRemvDeptValidate(String deptCode);
	
	//======================================================
	// 학과 코드 등록 기능
	//======================================================
	Integer insertDept(DeptDTO deptDTO);
	
	// 학과 코드 수정 및 삭제 기능 사용시 검증기능 (사용자 테이블에 해당 사용하는 코드가 있을 시 변경불가)
	Integer updateRemoveDeptValidate(String deptCode);
	
	//======================================================
	// 학과 코드 수정 기능
	//======================================================
	
	Integer updateDept(DeptDTO deptDTO);
	
	//======================================================
	// 학과 코드 삭제 기능
	//======================================================
	
	Integer removeDept(String deptCode);
}
