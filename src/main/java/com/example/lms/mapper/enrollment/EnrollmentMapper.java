package com.example.lms.mapper.enrollment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.EnrollmentDTO;

/**
 * 
 * 2025. 11. 24.
 * Author - yj
 * 학생 수강신청 기능 Mapper 인터페이스
 */
@Mapper
public interface EnrollmentMapper {
	// 수강신청 등록
	int insertEnrollment(EnrollmentDTO enrollment);
	
	// 학생이 특정 강의를 신청했는지 체크
	int countEnrollment(EnrollmentDTO enrollment);
	
	// 학생번호 기준 수강 목록
	List <EnrollmentDTO> selectEnrollmentList(int studentUserNo);
	
	// 수강 신청 취소
	int cancelEnrollment(EnrollmentDTO enrollment);
}
