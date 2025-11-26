package com.example.lms.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.SysAuthDTO;

/**
 * 
 * 2025. 11. 26.
 * Author - jm
 * 사용자 권한 Mapper
 */
@Mapper
public interface SysAuthMapper {

	// 사용자 권한 리스트 조회
	List<SysAuthDTO> sysAuthList();
}
