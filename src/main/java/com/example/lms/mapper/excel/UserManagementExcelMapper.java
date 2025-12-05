package com.example.lms.mapper.excel;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.SysUserExcelDTO;

/**
 * 
 * 2025. 12. 01.
 * Author - jm
 * 사용자 엑셀 Mapper
 */
@Mapper
public interface UserManagementExcelMapper {
	
		// 시스템사용자 관리 - 시스템 사용자 검색 조회
		List<SysUserExcelDTO> searchUserInfoMapListForExcel(@Param("searchUserCondition") String searchUserCondition);
		
		// ============================= 
		// 엑셀로 다수 사용자 등록 시 검증 필요함
		// =============================
		
		// 1. 권한코드 검증 필요
		Integer sysAuthDetailCdValidateExcel(String userAuth);
		
		// 2. 학년코드 검증 필요
		Integer sysUserGradeCdValidateExcel(Integer userGrade);
		
		// 2_1. 학생인지 검증 필요
		Integer sysUserGradeCdValidateExcel2(String userAuth);
		
		// 3. 학과,소속코드 검증 필요
		Integer deptCdValidateExcel(String deptCode);
		
		// 4. 계정상태코드 검증 필요
		Integer sysUserStatusCdValidateExcel(String userStatus);
		
		// =============================
		// 검증 리스트 종료
		// =============================
		
		// 시스템사용자관리 - 사용자 등록
		Integer insertSysUserExcel(SysUserExcelDTO sysUserExcelDTO);
}
