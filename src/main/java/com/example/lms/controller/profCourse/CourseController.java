package com.example.lms.controller.profCourse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.ProfCourseDTO;
import com.example.lms.dto.ProfCourseTimeDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.mapper.profCourse.CourseMapper;
import com.example.lms.service.profCourse.CourseService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CourseController {
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private CourseMapper courseMapper;
	
	// 교수별 강의 리스트
	@GetMapping("/courseList")
	public String courseListByProfessor(HttpSession session, Model model) {

	    SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

	    if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
	    }

	    List<ProfCourseDTO> courseList = courseService.getCourseListByProfessor(loginUser.getUserNo());
	    System.out.println("강의 개수 = " + courseList.size());

	    model.addAttribute("list", courseList);

	    return "profCourse/courseList"; 
	}
	
	// 등록 화면
	@GetMapping("/addCourse")
	public String addCourse(HttpSession session, Model model) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
		
		if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
	    }
		
		// 교시 range (1~10교시)
		List<Integer> range = new ArrayList<>();
	    for (int i = 1; i <= 10; i++) {
	        range.add(i);
	    }
	    model.addAttribute("range", range);
	    
	    // 학과 목록
	    List<DeptDTO> deptList = courseMapper.selectDeptList();
	    model.addAttribute("deptList", deptList);
	    
		return "profCourse/addCourse";
	}
	
	// 등록 처리
	@PostMapping("/addCourse")
	public String addCourse(
			ProfCourseDTO course, 
			@RequestParam("yoil")  List<Integer> yoil,
		    @RequestParam("start") List<Integer> start,
		    @RequestParam("end")   List<Integer> end,
		    HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
		
		if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
	    }
	
		course.setProfessorUserNo(loginUser.getUserNo());
		
		List<ProfCourseTimeDTO> timeList = new ArrayList<>();
		for (int i = 0; i < yoil.size(); i++) {
			ProfCourseTimeDTO t = new ProfCourseTimeDTO();
			t.setCourseTimeYoil(yoil.get(i));
			t.setCourseTimeStart(start.get(i));
			t.setCourseTimeEnd(end.get(i));
			timeList.add(t);
		}
		
		courseService.addCourse(course, timeList);
		
		return "redirect:/courseList";
	}
	
	// 수정 화면
	@GetMapping("/modifyCourse")
	public String editCourseForm(
			@RequestParam("courseNo") int courseNo,
			HttpSession session,
			Model model) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
		
		if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
		}
		
		ProfCourseDTO course = courseService.getCourseDetail(courseNo);
	    model.addAttribute("course", course);
	    
	    // 수정 화면에서도 학과/교시 필요하면 여기서도 추가로 넣어줄 수 있음
	    List<DeptDTO> deptList = courseMapper.selectDeptList();
	    model.addAttribute("deptList", deptList);
	    
	    List<Integer> range = new ArrayList<>();
	    for (int i = 1; i <= 10; i++) {
	        range.add(i);
	    }
	    model.addAttribute("range", range);

	    return "profCourse/modifyCourse";
	}
	
	// 수정 처리
	@PostMapping("/modifyCourse")
    public String editCourse(
    		ProfCourseDTO course, 
    		@RequestParam("yoil")  List<Integer> yoil,
    	    @RequestParam("start") List<Integer> start,
    	    @RequestParam("end")   List<Integer> end,
    	    HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
            return "redirect:/login";
        }

        course.setProfessorUserNo(loginUser.getUserNo());
        
        List<ProfCourseTimeDTO> timeList = new ArrayList<>();
        for (int i = 0; i < yoil.size(); i++) {
        	ProfCourseTimeDTO t = new ProfCourseTimeDTO();
        	t.setCourseTimeYoil(yoil.get(i));
        	t.setCourseTimeStart(start.get(i));
        	t.setCourseTimeEnd(end.get(i));
        	timeList.add(t);
        }

        courseService.modifyCourse(course, timeList);

        return "redirect:/courseDashboard?courseNo=" + course.getCourseNo();
    }
	
	// 삭제
	@GetMapping("/removeCourse")
    public String remove(@RequestParam int courseNo, HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

        if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
            return "redirect:/login";
        }
		
        courseService.removeCourse(courseNo);
        
        return "redirect:/courseList";
    }
	
	// 대시보드
	@GetMapping("/courseDashboard")
	public String courseDashboard(
			@RequestParam("courseNo") int courseNo,
			HttpSession session,
			Model model) {

	    SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

	    if (loginUser == null || !"PROFESSOR".equals(loginUser.getUserAuth())) {
	        return "redirect:/login";
	    }

	    ProfCourseDTO course = courseService.getCourseDetail(courseNo);
	    model.addAttribute("course", course);
	    
	    return "profCourse/courseDashboard";
	}
}
