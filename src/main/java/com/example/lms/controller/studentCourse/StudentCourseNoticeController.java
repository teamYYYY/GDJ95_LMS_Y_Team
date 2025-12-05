package com.example.lms.controller.studentCourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.StudentCourseNoticeDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.studentCourse.StudentCourseNoticeService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentCourseNoticeController {

    private final StudentCourseNoticeService service;

    // ====================================================================
    // 📌 공지 목록
    // ====================================================================
    @GetMapping("/studentCourseNoticeList")
    public String noticeList(
            @RequestParam int courseNo,
            @RequestParam(defaultValue = "1") int currentPage,
            HttpSession session,
            Model model) {

        SysUserDTO user = (SysUserDTO) session.getAttribute("loginUser");
        if (user == null) return "redirect:/login";

        // 강의 정보(헤더/서브네비 사용)
        model.addAttribute("course", service.getStudentCourseHome(courseNo, user.getUserNo()));

        int rowPerPage = 10;
        int startRow = (currentPage - 1) * rowPerPage;

        // 공지 목록 조회
        List<StudentCourseNoticeDTO> list = service.getNoticeList(courseNo, startRow, rowPerPage);
        int totalRow = service.getNoticeTotal(courseNo);

        // 리스트 각 항목에 courseNo 삽입 → Mustache에서 ../courseNo 제거 가능
        for (StudentCourseNoticeDTO dto : list) {
            dto.setCourseNo(courseNo);
        }

        // 전체 페이지 계산
        int lastPage = (totalRow == 0) ? 1 : (totalRow + rowPerPage - 1) / rowPerPage;

        // 페이지 그룹 5개 단위
        int pageGroup = (currentPage - 1) / 5;
        int startPage = pageGroup * 5 + 1;
        int endPage = Math.min(startPage + 4, lastPage);

        // 화면 표시 index
        int displayIndex = totalRow - startRow;
        for (StudentCourseNoticeDTO dto : list) {
            dto.setIndex(displayIndex--);
        }

        // 페이징 리스트
        List<Map<String, Object>> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("page", i);
            map.put("current", (i == currentPage));
            map.put("courseNo", courseNo);
            pageList.add(map);
        }

        model.addAttribute("list", list);
        model.addAttribute("pageList", pageList);
        model.addAttribute("currentPage", currentPage);

        model.addAttribute("hasPrev", startPage > 1);
        model.addAttribute("hasNext", endPage < lastPage);
        model.addAttribute("prevPage", startPage - 1);
        model.addAttribute("nextPage", endPage + 1);

        model.addAttribute("courseNo", courseNo);

        return "studentCourse/studentCourseNoticeList";
    }

    // ====================================================================
    // 📌 공지 상세
    // ====================================================================
    @GetMapping("/studentCourseNoticeDetail")
    public String noticeDetail(
            @RequestParam int courseNoticeNo,
            @RequestParam int courseNo,
            HttpSession session,
            Model model) {

        SysUserDTO user = (SysUserDTO) session.getAttribute("loginUser");
        if (user == null) return "redirect:/login";

        StudentCourseNoticeDTO detail = service.getStudentCourseNoticeDetail(courseNoticeNo);

        model.addAttribute("detail", detail);
        model.addAttribute("courseNo", courseNo);
        model.addAttribute("course", service.getStudentCourseHome(courseNo, user.getUserNo()));

        return "studentCourse/studentCourseNoticeDetail";
    }
}
