package com.example.lms.mapper.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.SysUserStatusDTO;

/**
 * 
 * 2025. 11. 26.
 * Author - jm
 * 사용자 계정 상태 Mapper
 */
@Mapper
public interface SysUserStatusMapper {
	
	// 사용자 상태 정보 리스트
	List<SysUserStatusDTO> sysUserStatusList();

}
