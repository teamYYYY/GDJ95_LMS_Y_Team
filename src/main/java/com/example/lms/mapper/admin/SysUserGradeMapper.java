package com.example.lms.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.SysUserGradeDTO;

/**
 * 
 * 2025. 11. 26.
 * Author - jm
 * 사용자 학생 학년 구분 Mapper
 */
@Mapper
public interface SysUserGradeMapper {
	
	// 사용자 학생 학년 리스트
	List<SysUserGradeDTO> sysUserGradeList();
}
