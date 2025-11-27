package com.example.lms.mapper.enrollment;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.dto.EnrollmentListDTO;

@Mapper
public interface EnrollmentMapper {

    // 시간표 겹침 체크
    int countTimeOverlap(
            @Param("studentUserNo") int studentUserNo,
            @Param("courseNo") int courseNo
    );

    // 중복 신청 체크
    int countEnrollment(
            @Param("studentUserNo") int studentUserNo,
            @Param("courseNo") int courseNo
    );

    // 신규 신청
    int insertEnrollment(EnrollmentDTO dto);

    // 신청 내역 페이징 조회
    List<EnrollmentListDTO> selectEnrollmentListPaged(
            @Param("studentUserNo") int studentUserNo,
            @Param("startRow") int startRow,
            @Param("rowPerPage") int rowPerPage
    );

    // 전체 row
    int countEnrollmentList(@Param("studentUserNo") int studentUserNo);

    // 수강 취소
    int cancelEnrollment(
            @Param("studentUserNo") int studentUserNo,
            @Param("enrollmentNo") int enrollmentNo
    );
}
