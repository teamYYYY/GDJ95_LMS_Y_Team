package com.example.lms.mapper.profAssignment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.AssignmentDTO;

@Mapper
public interface AssignmentMapper {
	
	// 강의별 과제 리스트
	List<AssignmentDTO> selectAssignmentListByProf(int courseNo, int startRow, int rowPerPage);
	int selectAssignmentCount(int courseNo); //페이징
	
	// 상세보기
	AssignmentDTO selectAssignmentDetail(int assignmentNo);
	
	// 등록
	int insertAssignment(AssignmentDTO a);
	
	// 수정
	int updateAssignment(AssignmentDTO a);
	
	// 삭제
	int deleteAssignment(int assignmentNo);
}
