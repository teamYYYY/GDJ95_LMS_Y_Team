package com.example.lms.service.prof;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.AssignmentDTO;
import com.example.lms.mapper.prof.ProfAssignmentMapper;

@Service
@Transactional
public class ProfAssignmentService {

	@Autowired
	ProfAssignmentMapper assignmentMapper;
	
	// 강의별 과제 리스트
	public List<AssignmentDTO> getAssignmentListByProf(int courseNo, int startRow, int rowPerPage) {
		
		return assignmentMapper.selectAssignmentListByProf(courseNo, startRow, rowPerPage);
	}
	
	public int getAssignmentCount(int courseNo) {
		
		return assignmentMapper.selectAssignmentCount(courseNo);
	}
	
	// 상세보기
	public AssignmentDTO getAssignmentDetail(int assignmentNo) {
		
		return assignmentMapper.selectAssignmentDetail(assignmentNo);
	}
	
	// 등록
	public int addAssignment(AssignmentDTO a) {
		
		return assignmentMapper.insertAssignment(a);
	}
	
	// 수정
	public int modifyAssignment(AssignmentDTO a) {
		
		return assignmentMapper.updateAssignment(a);
	}
	
	// 삭제
	public int removeAssignment(int assignmentNo) {
		
		return assignmentMapper.deleteAssignment(assignmentNo); 
	}
}
