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
    
    // 수강신청 처리
    public String addEnrollment(EnrollmentDTO enrollment) {
    	log.debug("[수강신청 요청] enrollment = {}", enrollment);
    	
    	// 1) 이전에 취소했던 수강신청 복구 시도 (status: 1 → 0)
        int reactivated = enrollmentMapper.reactivateEnrollment(enrollment);
        if (reactivated > 0) {
            log.info("[수강신청 복구] studentUserNo={}, courseNo={}",
                    enrollment.getStudentUserNo(), enrollment.getCourseNo());
            return "수강신청이 완료되었습니다.";
        }

        // 2) 이미 신청(활성) 상태인지 중복 체크 (status = 0)
        int exists = enrollmentMapper.countEnrollment(enrollment);
        if (exists > 0) {
            log.info("[수강신청 중복] 이미 신청한 강의 - studentUserNo={}, courseNo={}",
                    enrollment.getStudentUserNo(), enrollment.getCourseNo());
            return "이미 신청한 강의입니다.";
        }

        // 3) 정원 체크
        int capacity = studentCourseMapper.getCourseCapacity(enrollment.getCourseNo());
        int current  = studentCourseMapper.getCurrentEnrollment(enrollment.getCourseNo());

        log.debug("[수강신청 정원 체크] courseNo={}, current={}, capacity={}",
                enrollment.getCourseNo(), current, capacity);

        if (current >= capacity) {
            log.info("[수강신청 실패 - 정원초과] courseNo={}, current={}, capacity={}",
                    enrollment.getCourseNo(), current, capacity);
            return "정원이 초과된 강의입니다.";
        }

        // 4) 신규 수강신청 INSERT (완전히 처음 신청하는 경우)
        enrollment.setEnrollmentStatus(0); // 0 = 신청 상태
        enrollmentMapper.insertEnrollment(enrollment);

        log.info("[수강신청 완료] studentUserNo={}, courseNo={}",
                enrollment.getStudentUserNo(), enrollment.getCourseNo());
        return "수강신청이 완료되었습니다.";
    }

    // 학생별 수강신청 내역 조회
    public List<EnrollmentDTO> selectEnrollmentList(int studentUserNo) {
        return enrollmentMapper.selectEnrollmentList(studentUserNo);
    }

    // 수강취소 0 = 신청 -> 1 = 취소
    public String cancelEnrollment(EnrollmentDTO enrollment) {
    	int updated = enrollmentMapper.cancelEnrollment(enrollment);

        if (updated == 0) {
            // 이미 취소됐거나 아예 없는 경우
            log.warn("[수강취소 실패] 대상 수강 내역 없음 - studentUserNo={}, courseNo={}",
                    enrollment.getStudentUserNo(), enrollment.getCourseNo());
            return "취소할 수 있는 수강 내역이 없습니다.";
        }

        log.info("[수강취소 완료] studentUserNo={}, courseNo={}",
                enrollment.getStudentUserNo(), enrollment.getCourseNo());

        return "수강이 취소되었습니다.";
    }
}