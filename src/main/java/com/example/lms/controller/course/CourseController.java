package com.example.lms.controller.course;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.CourseDTO;
import com.example.lms.service.course.CourseService;

@Controller
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@GetMapping("/courseList")
	public String courseList(Model model, @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {
		
		List<CourseDTO> list = courseService.getCourseListByPage(currentPage);
		model.addAttribute("list",list);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("prePage", currentPage > 1 ? currentPage - 1 : 1);
		model.addAttribute("nextPage", currentPage+1);
		
		return "course/courseList";
	}
	
	@GetMapping("/courseDetail")
	public String courseDetail(Model model, @RequestParam("courseNo") int courseNo) {
		
		CourseDTO courseDTO = courseService.getCourseDetail(courseNo);
		model.addAttribute("course", courseDTO);
		
		return "course/courseDatail"; 
	}
}	
