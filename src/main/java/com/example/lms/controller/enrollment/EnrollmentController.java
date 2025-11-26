package com.example.lms.controller.enrollment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.lms.dto.EnrollmentDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.enrollment.EnrollmentService;
import com.example.lms.service.studentCourse.StudentCourseService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final StudentCourseService studentCourseService;

    /** 수강신청 가능 강의 리스트 */
    @GetMapping("/courseListForEnrollment")
    public String courseListForEnrollment(Model model,
                                         HttpSession session,
                                         @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        log.info("수강신청 화면 요청 - studentUserNo={}", loginUser.getUserNo());

        model.addAttribute("courseList", studentCourseService.getCourseListForStudent());
        model.addAttribute("studentUserNo", loginUser.getUserNo());

        // 네비게이션 활성화
        model.addAttribute("nav_enrollment", "border-blue-600 text-blue-600");

        return "enrollment/courseListForEnrollment";
    }

    /** 수강신청 처리 */
    @PostMapping("/addEnrollment")
    public String addEnrollment(EnrollmentDTO enrollment,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        enrollment.setStudentUserNo(loginUser.getUserNo());
        // 상태코드: 0 = 신청, 1 = 취소  (XML, 중복체크 로직이 이렇게 설계되어 있음)
        enrollment.setEnrollmentStatus(0);

        try {
            String msg = enrollmentService.addEnrollment(enrollment);
            redirectAttributes.addFlashAttribute("message", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다.");
        }

        return "redirect:/courseListForEnrollment";
    }

    /** 수강신청 내역 조회 */
    @GetMapping("/enrollmentList")
    public String enrollmentList(HttpSession session, Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        int studentUserNo = loginUser.getUserNo();

        List<EnrollmentDTO> rawList = enrollmentService.selectEnrollmentList(studentUserNo);

        List<Map<String, Object>> viewList = new ArrayList<>();

        for (EnrollmentDTO e : rawList) {
            Map<String, Object> row = new HashMap<>();
            row.put("enrollmentNo", e.getEnrollmentNo());
            row.put("courseNo", e.getCourseNo());
            row.put("createdate", e.getCreatedate());

            Integer status = e.getEnrollmentStatus();
            if (status == null) status = 1; // 필요하면 기본값 처리

            row.put("isActive", status == 0);   // 신청완료
            row.put("isCanceled", status == 1); // 취소됨

            viewList.add(row);
        }

        model.addAttribute("list", viewList);
        model.addAttribute("nav_enrollmentList", "border-blue-600 text-blue-600");

        return "enrollment/enrollmentList";
    }

    /** 수강취소 */
    @PostMapping("/cancelEnrollment")
    public String cancelEnrollment(@RequestParam int courseNo,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        EnrollmentDTO enrollment = new EnrollmentDTO();
        enrollment.setStudentUserNo(loginUser.getUserNo());
        enrollment.setCourseNo(courseNo);

        try {
            String msg = enrollmentService.cancelEnrollment(enrollment);
            redirectAttributes.addFlashAttribute("message", msg);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "취소 처리 중 오류 발생");
        }

        return "redirect:/enrollmentList";
    }
}
