package com.example.lms.mapper.enrollment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.EnrollmentDTO;

@Mapper
public interface EnrollmentMapper {

    // 신규 수강신청 INSERT
    int insertEnrollment(EnrollmentDTO enrollment);

    // 이미 신청된 강의인지 (status = 0) 확인
    int countEnrollment(EnrollmentDTO enrollment);

    // 학생별 수강신청 목록 조회
    List<EnrollmentDTO> selectEnrollmentList(int studentUserNo);

    // 수강취소 (status = 0 → 1)
    int cancelEnrollment(EnrollmentDTO enrollment);

    // 취소된 수강신청 복구 (status = 1 → 0)
    int reactivateEnrollment(EnrollmentDTO enrollment);
}
