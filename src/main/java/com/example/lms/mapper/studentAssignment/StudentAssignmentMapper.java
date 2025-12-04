package com.example.lms.mapper.studentAssignment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.AssignmentSubmissionDTO;
import com.example.lms.dto.StudentAssignmentDetailDTO;
import com.example.lms.dto.StudentAssignmentListDTO;

@Mapper
public interface StudentAssignmentMapper {

    // 과제 목록
    List<StudentAssignmentListDTO> selectAssignmentList(
            @Param("courseNo") int courseNo,
            @Param("studentUserNo") int studentUserNo);

    // 과제 상세
    StudentAssignmentDetailDTO selectAssignmentDetail(
            @Param("assignmentNo") int assignmentNo,
            @Param("studentUserNo") int studentUserNo);

    // 내 제출 1건 조회
    AssignmentSubmissionDTO selectMySubmission(
            @Param("assignmentNo") int assignmentNo,
            @Param("studentUserNo") int studentUserNo);

    // INSERT
    int insertSubmission(AssignmentSubmissionDTO dto);

    // UPDATE
    int updateSubmission(AssignmentSubmissionDTO dto);
}
