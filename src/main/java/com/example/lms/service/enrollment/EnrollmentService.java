package com.example.lms.service.enrollment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.mapper.enrollment.EnrollmentMapper;
import com.example.lms.mapper.studentCourse.StudentCourseMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class EnrollmentService {

    private final EnrollmentMapper enrollmentMapper;
    private final StudentCourseMapper studentCourseMapper;

    public String addEnrollment(EnrollmentDTO enrollment) {

        log.debug("[수강신청] 요청 데이터 = {}", enrollment);

        // 1) 중복 체크
        int exists = enrollmentMapper.countEnrollment(enrollment);
        if (exists > 0) {
            return "이미 신청한 강의입니다.";
        }

        // 2) 정원 체크
        int capacity = studentCourseMapper.getCourseCapacity(enrollment.getCourseNo());
        int current = studentCourseMapper.getCurrentEnrollment(enrollment.getCourseNo());

        if (current >= capacity) {
            return "정원이 초과된 강의입니다.";
        }

        // 3) 신청
        enrollmentMapper.insertEnrollment(enrollment);
        return "수강신청이 완료되었습니다.";
    }

    public List<EnrollmentDTO> selectEnrollmentList(int studentUserNo) {
        return enrollmentMapper.selectEnrollmentList(studentUserNo);
    }

    public String cancelEnrollment(EnrollmentDTO enrollment) {
        enrollmentMapper.cancelEnrollment(enrollment);
        return "수강이 취소되었습니다.";
    }
}