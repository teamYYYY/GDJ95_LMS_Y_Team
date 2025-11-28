package com.example.lms.controller.enrollment;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.dto.EnrollmentListDTO;
import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentCourseDetailDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.enrollment.EnrollmentService;
import com.example.lms.service.studentCourse.StudentCourseService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentCourseService studentCourseService;
    
	// ---------------------------------------------------------
	// 학생용 강의 상세보기
	// ---------------------------------------------------------
    @GetMapping("/studentCourseDetail")
    public String studentCourseDetail(@RequestParam("courseNo") int courseNo, Model model) {
		StudentCourseDetailDTO detail = studentCourseService.getStudentCourseDetail(courseNo);
    	
		model.addAttribute("detail", detail);
		model.addAttribute("nav_enrollment", "border-blue-600 text-blue-600");
		
		return "enrollment/studentCourseDetail";
    }
    
    // ---------------------------------------------------------
    // 수강신청 가능 강의 목록
    // ---------------------------------------------------------
    @GetMapping("/courseListForEnrollment")
    public String courseListForEnrollment(
            Model model,
            HttpSession session,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int studentUserNo = loginUser.getUserNo();
        int rowPerPage = 10;
        int startRow = (currentPage - 1) * rowPerPage;

        // 강의 목록 조회
        List<StudentCourseDTO> courseList =
                studentCourseService.getCourseListForStudent(studentUserNo, startRow, rowPerPage);

        model.addAttribute("courseList", courseList);

        // 전체 강의 수
        int totalRow = studentCourseService.getTotalCourseCount();
        int lastPage = (totalRow + rowPerPage - 1) / rowPerPage;

        // 페이지 그룹 (5개 단위)
        int pageGroup = (currentPage - 1) / 5;
        int startPage = pageGroup * 5 + 1;
        int endPage = startPage + 4;
        if (endPage > lastPage) endPage = lastPage;

        // pageList 구성
        List<Map<String, Object>> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("page", i);
            p.put("current", i == currentPage);
            pageList.add(p);
        }

        model.addAttribute("pageList", pageList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("hasPrev", currentPage > 1);
        model.addAttribute("hasNext", currentPage < lastPage);
        model.addAttribute("prevPage", currentPage - 1);
        model.addAttribute("nextPage", currentPage + 1);

        model.addAttribute("nav_enrollment", "border-blue-600 text-blue-600");

        return "enrollment/courseListForEnrollment";
    }

    // ---------------------------------------------------------
    // 수강 신청 처리
    // ---------------------------------------------------------
    @PostMapping("/addEnrollment")
    public String addEnrollment(
            EnrollmentDTO dto,
            @RequestParam(defaultValue="1") int currentPage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

        dto.setStudentUserNo(loginUser.getUserNo());
        dto.setEnrollmentStatus(0); // 기본 신청값

        String msg = enrollmentService.addEnrollment(dto);

        redirectAttributes.addFlashAttribute("message", msg);
        redirectAttributes.addFlashAttribute("currentPage", currentPage);

        return "redirect:/courseListForEnrollment?currentPage=" + currentPage;
    }

    // ---------------------------------------------------------
    // 수강 신청 내역
    // ---------------------------------------------------------
    @GetMapping("/enrollmentList")
    public String enrollmentList(
            Model model,
            HttpSession session,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int studentUserNo = loginUser.getUserNo();
        int rowPerPage = 10;
        int startRow = (currentPage - 1) * rowPerPage;

        // 신청 내역 조회
        List<EnrollmentListDTO> list =
                enrollmentService.getEnrollmentList(studentUserNo, startRow, rowPerPage);

        model.addAttribute("list", list);

        // 전체 row
        int totalRow = enrollmentService.getEnrollmentTotalCount(studentUserNo);
        int lastPage = (totalRow + rowPerPage - 1) / rowPerPage;

        // 페이지 그룹 (5개 단위)
        int pageGroup = (currentPage - 1) / 5;
        int startPage = pageGroup * 5 + 1;
        int endPage = startPage + 4;
        if (endPage > lastPage) endPage = lastPage;

        // pageList 구성
        List<Map<String, Object>> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("page", i);
            p.put("current", i == currentPage);
            pageList.add(p);
        }

        model.addAttribute("pageList", pageList);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("hasPrev", currentPage > 1);
        model.addAttribute("hasNext", currentPage < lastPage);
        model.addAttribute("prevPage", currentPage - 1);
        model.addAttribute("nextPage", currentPage + 1);

        model.addAttribute("nav_enrollmentList", "border-blue-600 text-blue-600");

        return "enrollment/enrollmentList";
    }

    // ---------------------------------------------------------
    // 수강 취소
    // ---------------------------------------------------------
    @PostMapping("/cancelEnrollment")
    public String cancelEnrollment(
            @RequestParam int enrollmentNo,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        String msg = enrollmentService.cancelEnrollment(loginUser.getUserNo(), enrollmentNo);
        redirectAttributes.addFlashAttribute("message", msg);

        return "redirect:/enrollmentList";
    }

}