package com.example.lms.controller.studentAssignment;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.lms.dto.AssignmentSubmissionDTO;
import com.example.lms.dto.StudentAssignmentDetailDTO;
import com.example.lms.dto.StudentAssignmentListDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.studentAssignment.StudentAssignmentService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentAssignmentController {

    private final StudentAssignmentService service;

    // ==============================
    // 과제 목록
    // ==============================
    @GetMapping("/student/assignment/list")
    public String assignmentList(
            @RequestParam("courseNo") int courseNo,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        int studentUserNo = loginUser.getUserNo();

        List<StudentAssignmentListDTO> list =
                service.getAssignmentList(courseNo, studentUserNo);

        model.addAttribute("courseNo", courseNo);
        model.addAttribute("list", list);

        return "studentCourse/studentAssignmentList";
    }

    // ==============================
    // 과제 상세 + 내 제출 정보
    // ==============================
    @GetMapping("/student/assignment/detail")
    public String assignmentDetail(
            @RequestParam("assignmentNo") int assignmentNo,
            @RequestParam("courseNo") int courseNo,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");

        StudentAssignmentDetailDTO detail =
                service.getAssignmentDetail(assignmentNo, loginUser.getUserNo());
        
     // 🔥 디버그 (가장 중요)
        System.out.println("🔥 [CTRL] assignment.detail = " + detail);
        System.out.println("🔥 [CTRL] deadlinePassed = " + detail.getDeadlinePassed());
        
        model.addAttribute("courseNo", courseNo);
        model.addAttribute("assignment", detail);

        return "studentCourse/studentAssignmentDetail";
    }

    // ==============================
    // 과제 제출 / 수정
    // ==============================
    @PostMapping("/student/assignment/submit")
    public String submitAssignment(
            @RequestParam("assignmentNo") int assignmentNo,
            @RequestParam("courseNo") int courseNo,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            HttpSession session) throws IOException {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        int studentUserNo = loginUser.getUserNo();

        // 1) DTO 생성
        AssignmentSubmissionDTO dto = new AssignmentSubmissionDTO();
        dto.setAssignmentNo(assignmentNo);
        dto.setWriterUserNo(studentUserNo);
        dto.setAssignmentSubmissionContent(content);

        // 2) 파일 업로드 (있을 때만)
        if (file != null && !file.isEmpty()) {

            // 실제 저장 폴더
            String uploadDir = "C:/lmsUpload/assignment/";

            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File target = new File(uploadDir + file.getOriginalFilename());
            file.transferTo(target);

            // DB에 저장할 URL (WebMvcConfig 매핑 기준)
            String fileUrl = "/upload/assignment/" + file.getOriginalFilename();
            dto.setAssignmentSubmissionFileUrl(fileUrl);
        }

        // 3) INSERT or UPDATE (Service 에서 분기)
        service.submitAssignment(dto);

        // 4) 다시 상세 페이지로
        return "redirect:/student/assignment/detail?assignmentNo=" + assignmentNo +
                "&courseNo=" + courseNo;
    }

}
