package com.example.lms.controller.studentCourse;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.StudentGradeDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.studentCourse.StudentGradeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentGradeController {

    private final StudentGradeService service;

    // ---------------------------------------------------------
    // 학생 성적 페이지
    // ---------------------------------------------------------
    @GetMapping("/student/grade")
    public String studentGrade(
            @RequestParam int courseNo,
            HttpSession session,
            Model model) {

        // 로그인 체크
        SysUserDTO user = (SysUserDTO) session.getAttribute("loginUser");
        if(user == null) return "redirect:/login";

        int studentUserNo = user.getUserNo();

        // ---------------------------------------------------------
        // 학생 성적 목록 조회
        // ---------------------------------------------------------
        StudentGradeDTO grade = service.getStudentGrade(courseNo, studentUserNo);

        model.addAttribute("grade", grade);


        // ---------------------------------------------------------
        // 모델에 데이터 바인딩
        // ---------------------------------------------------------
        model.addAttribute("courseNo", courseNo);
        model.addAttribute("gradeList", grade);

        // ---------------------------------------------------------
        // 강의 내부 네비게이션 활성화
        // ---------------------------------------------------------
        model.addAttribute("nav_grade", "border-blue-600 text-blue-600");

        return "studentCourse/studentGrade";
    }
}
