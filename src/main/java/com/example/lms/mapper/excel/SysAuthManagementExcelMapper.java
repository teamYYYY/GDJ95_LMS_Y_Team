package com.example.lms.mapper.excel;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.SysAuthDTO;

/**
*
* 2025. 12. 01.
* Author - jm
* 사용자 권한 관리 엑셀 Mapper
*/
@Mapper
public interface SysAuthManagementExcelMapper {
	
	// 사용자 권한 코드 검색 조회
	List<SysAuthDTO> searchSysAuthInfoListForExcel(@Param("searchSysAuthCondition") String searchSysAuthCondition);
	
	// 사용자 권한 등록전에 권한코드가 이미 있을 경우에는 Detail만 인서트하면 된다.
	Integer insertSysAuthChkExcel(SysAuthDTO sysAuthDTO);
	
	// 사용자 세부권한 등록 시 동일한 세부권한코드나 세부권한명이 존재하면 안된다.
	Integer insertSysAuthDetailChkExcel(SysAuthDTO sysAuthDTO);
	
	//======================================================
	// 사용자 권한 등록 기능 insert 순서 sysAuth -> sysAuthDetail
	//======================================================
	Integer insertSysAuthExcel(SysAuthDTO sysAuthDTO);

	Integer insertSysAuthDetailExcel(SysAuthDTO sysAuthDTO);

}
