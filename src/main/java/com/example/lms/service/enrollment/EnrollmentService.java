package com.example.lms.service.enrollment;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.mapper.course.CourseMapper;
import com.example.lms.mapper.enrollment.EnrollmentMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class EnrollmentService {
	 private EnrollmentMapper enrollmentMapper;
	 private CourseMapper courseMapper;
	 
	 // 수강신청기능
	 public String addEnrollment(EnrollmentDTO enrollment) {
		 log.debug("[수강신청] 요청 데이터 = {}", enrollment);
		 
		 // 중복 체크
		 int exists = enrollmentMapper.countEnrollment(enrollment);
	        if (exists > 0) {
	            log.debug("이미 신청한 강의입니다.");
	            return "이미 신청한 강의입니다.";
	        }

		 // 정원 체크
	     int capacity = courseMapper.getCourseCapacity(enrollment.getCourseNo());
	     int current = courseMapper.getCurrentEnrollment(enrollment.getCourseNo());
	     
	     if(current >= capacity) {
	    	 log.debug("정원 초과된 강의");
	    	 return "정원 초과된 강의입니다.";
	     }
	     
	     // 신청
	     int row = enrollmentMapper.insertEnrollment(enrollment);
	     log.debug("수강 신청 완료 row={}", row);
	     
	     return "수강신청이 완료되었습니다.";
	 }
	 
	 public List<EnrollmentDTO> selectEnrollmentList(int studentUserNo){
		 return enrollmentMapper.selectEnrollmentList(studentUserNo);
	 }
	 
	 public String cancelEnrollment(EnrollmentDTO enrollment) {
		 enrollmentMapper.cancelEnrollment(enrollment);
		 return "수강이 취소되었습니다.";
	 }	 
}
