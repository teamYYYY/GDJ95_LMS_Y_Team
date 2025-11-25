package com.example.lms.controller.profCourse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.CourseDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.profCourse.CourseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	// 교수별 강의 리스트
	@GetMapping("/courseList")
    public String courseListByProfessor(HttpSession session, Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
            return "redirect:/login";
        }

        // 교수 번호 기준 강의 목록 조회
        List<CourseDTO> courseList = courseService.getCourseListByProfessor(loginUser.getUserNo());
        System.out.println("강의 개수 = " + courseList.size());

        
        model.addAttribute("list", courseList);

        return "profCourse/courseList";  // mustache 파일
    }
	
	// 등록
	@GetMapping("/addCourse")
	public String addCourse(HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
		
		if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
	    }
		
		List<CourseDTO> courseList = courseService.getCourseListByProfessor(loginUser.getUserNo());
		
		return "profCourse/addCourse";
	}
	
	@PostMapping("/addCourse")
	public String addCourse(CourseDTO course, HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
		
		if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
	    }
	
		course.setProfessorUserNo(loginUser.getUserNo());
		
		courseService.addCourse(course);
		
		return "redirect:/courseList";
	}
	
	// 수정
	@GetMapping("/modifyCourse")
	public String editCourseForm( @RequestParam("courseNo") int courseNo, HttpSession session, Model model) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
		
		if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
		}
		
		CourseDTO course = courseService.getCourseDetail(courseNo);
	    model.addAttribute("course", course);

	    return "profCourse/modifyCourse";
	}
	
	@PostMapping("/modifyCourse")
    public String editCourse(CourseDTO course, HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
            return "redirect:/login";
        }

        course.setProfessorUserNo(loginUser.getUserNo());

        courseService.modifyCourse(course);

        return "redirect:/courseDashboard?courseNo=" + course.getCourseNo();
    }
	
	//삭제
	@GetMapping("/deleteCourse")
    public String deleteCourse(@RequestParam int courseNo, HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
            return "redirect:/login";
        }
		
        courseService.removeCourse(courseNo);
        
        return "redirect:/courseList";
    }
	
	//대시보드
	@GetMapping("/courseDashboard")
	public String courseDashboard(@RequestParam("courseNo") int courseNo, HttpSession session,  Model model) {

	    SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

	    if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
	    }

	    CourseDTO course = courseService.getCourseDetail(courseNo);
	    model.addAttribute("course", course);
	    
	    System.out.println("courseNo = " + courseNo);
	    System.out.println("courseDTO = " + course);

	    return "profCourse/courseDashboard";
	}
}	
