package com.example.lms.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.DeptDTO;

/**
 * 
 * 2025. 11. 26.
 * Author - jm
 * 학과 Mapper
 */
@Mapper
public interface DeptMapper {
	
	// 학과 관리 - 학과 정보 조회
	List<DeptDTO> deptList();
}
