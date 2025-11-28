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
import com.example.lms.dto.StudentTimetableDTO;
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
	// 필터
	// ---------------------------------------------------------
    @GetMapping("/courseListForEnrollment")
    public String courseListForEnrollment(
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "yoil", required = false) Integer yoil,  // 1~5
            @RequestParam(value = "professor", required = false) String professor,
            @RequestParam(value = "deptCode", required = false) String deptCode,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int studentUserNo = loginUser.getUserNo();
        int rowPerPage = 10;
        int startRow = (currentPage - 1) * rowPerPage;

        List<StudentCourseDTO> list =
                studentCourseService.getCourseListForStudentFiltered(
                        studentUserNo, yoil, professor, deptCode, startRow, rowPerPage
                );

        int totalRow = studentCourseService.countFilteredCourseList(yoil, professor, deptCode);

        model.addAttribute("courseList", list);
        model.addAttribute("yoil", yoil);
        model.addAttribute("professor", professor);
        model.addAttribute("deptCode", deptCode);
        model.addAttribute("deptList", studentCourseService.getDeptList());

        // nav 하이라이트
        model.addAttribute("nav_enrollment", "border-blue-600 text-blue-600");

        return "enrollment/courseListForEnrollment";
    }

	// ---------------------------------------------------------
	// 학생 시간표 보기
	// ---------------------------------------------------------
	 @GetMapping("/studentTimetable")
	 public String studentTimetable(HttpSession session, Model model) {
	
	     SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
	     if (loginUser == null) {
	         return "redirect:/login";
	     }
	
	     int studentUserNo = loginUser.getUserNo();
	
	     // 1) DB에서 신청 강의 시간표 목록 가져오기
	     List<StudentTimetableDTO> timetable = studentCourseService.getStudentTimetable(studentUserNo);
	
	     // 2) 월~금 / 1~8교시 2차원 배열 생성
	     StudentTimetableDTO[][] grid = new StudentTimetableDTO[5][8];
	
	     for (StudentTimetableDTO t : timetable) {
	         int yoilIndex = t.getCourseTimeYoil() - 1;     // 1=월 → index 0
	         for (int p = t.getCourseTimeStart(); p <= t.getCourseTimeEnd(); p++) {
	             int periodIndex = p - 1;                   // 1교시 → index 0
	             grid[yoilIndex][periodIndex] = t;
	         }
	     }
	     
	     // periods 설정
	     List<Map<String, Object>> periods = new ArrayList<>();

	     for (int p = 1; p <= 8; p++) {
	         Map<String, Object> periodRow = new HashMap<>();
	         periodRow.put("period", p);

	         List<Map<String, Object>> yoils = new ArrayList<>();
	         for (int y = 0; y < 5; y++) {

	             Map<String, Object> cell = new HashMap<>();

	             StudentTimetableDTO t = grid[y][p - 1];
	             if (t != null) cell.put("timetable", t);

	             yoils.add(cell);
	         }
	         periodRow.put("yoils", yoils);
	         periods.add(periodRow);
	     }

	     model.addAttribute("periods", periods);

	     model.addAttribute("grid", grid);
	     model.addAttribute("nav_timetable", "border-blue-600 text-blue-600");
	
	     return "enrollment/studentTimetable";
	 }

    
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