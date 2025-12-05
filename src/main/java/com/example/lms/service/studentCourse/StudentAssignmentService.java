package com.example.lms.service.studentCourse;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lms.dto.AssignmentSubmissionDTO;
import com.example.lms.dto.StudentAssignmentDetailDTO;
import com.example.lms.dto.StudentAssignmentListDTO;
import com.example.lms.mapper.studentCourse.StudentAssignmentMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentAssignmentService {

    private final StudentAssignmentMapper mapper;

    // 과제 목록
    public List<StudentAssignmentListDTO> getAssignmentList(int courseNo, int studentUserNo) {

        System.out.println("DEBUG >>> getAssignmentList() 호출됨");
        System.out.println("DEBUG >>> courseNo = " + courseNo);
        System.out.println("DEBUG >>> studentUserNo = " + studentUserNo);

        List<StudentAssignmentListDTO> list = mapper.selectAssignmentList(courseNo, studentUserNo);

        System.out.println("DEBUG >>> mapper.selectAssignmentList 결과 = " + list);
        if (list != null) {
            System.out.println("DEBUG >>> 결과 개수 = " + list.size());
        }

        return list;
    }


    // 과제 상세 + 내 제출 정보까지
    public StudentAssignmentDetailDTO getAssignmentDetail(int assignmentNo, int studentUserNo) {
        return mapper.selectAssignmentDetail(assignmentNo, studentUserNo);
    }

    // 과제 제출 또는 수정
    public void submitAssignment(AssignmentSubmissionDTO dto) {

        // 1) 기존 제출 조회
        AssignmentSubmissionDTO existing =
                mapper.selectMySubmission(dto.getAssignmentNo(), dto.getWriterUserNo());

        if (existing == null) {
            // 처음 제출 → INSERT
            mapper.insertSubmission(dto);

        } else {
            // 이미 제출한 적 있음 → UPDATE
            dto.setAssignmentSubmissionNo(existing.getAssignmentSubmissionNo());

            // 파일을 새로 안 올렸으면 기존 파일 URL 유지
            if (dto.getAssignmentSubmissionFileUrl() == null ||
                dto.getAssignmentSubmissionFileUrl().isBlank()) {
                dto.setAssignmentSubmissionFileUrl(existing.getAssignmentSubmissionFileUrl());
            }

            mapper.updateSubmission(dto);
        }
    }
}
