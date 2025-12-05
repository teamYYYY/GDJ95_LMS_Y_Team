package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.AssignmentSubmissionDTO;
import com.example.lms.dto.StudentAssignmentDetailDTO;
import com.example.lms.dto.StudentAssignmentListDTO;

@Mapper
public interface StudentAssignmentMapper {

    List<StudentAssignmentListDTO> selectAssignmentList(
            @Param("courseNo") int courseNo,
            @Param("writerUserNo") int writerUserNo);

    StudentAssignmentDetailDTO selectAssignmentDetail(
            @Param("assignmentNo") int assignmentNo,
            @Param("writerUserNo") int writerUserNo);

    Integer selectSubmissionExists(
            @Param("assignmentNo") int assignmentNo,
            @Param("writerUserNo") int writerUserNo);

    void insertSubmission(AssignmentSubmissionDTO dto);

    void updateSubmission(AssignmentSubmissionDTO dto);

    AssignmentSubmissionDTO selectMySubmission(
            @Param("assignmentNo") int assignmentNo,
            @Param("writerUserNo") int writerUserNo);
}
