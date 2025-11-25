package com.example.lms.controller.enrollment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.service.course.CourseService;
import com.example.lms.service.enrollment.EnrollmentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EnrollmentController {
	@Autowired
	private EnrollmentService enrollmentService;
	@Autowired
	private CourseService courseService;
	 
	@GetMapping("/courseListForEnrollment")
	public String courseListForEnrollment(Model model, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {
		
	    model.addAttribute("courseList", courseService.getCourseListByPage(currentPage));
	    model.addAttribute("studentUserNo", 1); // 로그인된 학생번호 넣기
	    return "enrollment/courseListForEnrollment";
	}
	
	// 수강신청 등록 액션
	@PostMapping("/addEnrollment")
	public String addEnrollment(EnrollmentDTO enrollment, Model model) {
		log.info("수강 신청 요청: student={}, course={}"
				, enrollment.getStudentUserNo(), enrollment.getCourseNo());
		
		try {
			// 서비스 호출
			String msg = enrollmentService.addEnrollment(enrollment);
			model.addAttribute("msg", msg); 
			
			log.info("수강 신청 결과: {}", msg);
			
			// 결과 페이지로 이동
            return "enrollment/enrollResult";
		}catch(Exception e) {
			log.error("수강 신청 처리 중 오류 발생", e);
			model.addAttribute("msg", "오류가 발생했습니다. 나중에 다시 시도해 주세요");
			return "enrollment/enrollResult";
		}
		
	}
	
}
