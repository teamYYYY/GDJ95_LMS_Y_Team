package com.example.lms.controller.studentCourse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentCourseDetailDTO;
import com.example.lms.dto.StudentCourseHomeDTO;
import com.example.lms.dto.StudentCourseNoticeDTO;
import com.example.lms.dto.StudentTimetableDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.studentCourse.StudentCourseService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StudentCourseController {

    private final StudentCourseService studentCourseService;

    // ---------------------------------------------------------
    // 공통: 강의 내부 서브 메뉴 Active 설정
    // ---------------------------------------------------------
    private void setActiveNav(Model model, String activeMenu) {
        model.addAttribute("nav_home", "");
        model.addAttribute("nav_notice", "");
        model.addAttribute("nav_assignment", "");
        model.addAttribute("nav_attendance", "");
        model.addAttribute("nav_grade", "");
        model.addAttribute("nav_qna", "");

        switch (activeMenu) {
            case "home" -> model.addAttribute("nav_home", "border-blue-600 text-blue-600");
            case "notice" -> model.addAttribute("nav_notice", "border-blue-600 text-blue-600");
            case "assignment" -> model.addAttribute("nav_assignment", "border-blue-600 text-blue-600");
            case "attendance" -> model.addAttribute("nav_attendance", "border-blue-600 text-blue-600");
            case "grade" -> model.addAttribute("nav_grade", "border-blue-600 text-blue-600");
            case "qna" -> model.addAttribute("nav_qna", "border-blue-600 text-blue-600");
        }
    }

    // ---------------------------------------------------------
    // 공통: 강의 정보(course, courseNo)를 모델에 넣는 함수
    // ---------------------------------------------------------
    private void loadCourseInfo(int courseNo, int studentUserNo, Model model) {

        StudentCourseHomeDTO courseInfo =
                studentCourseService.getStudentCourseHome(courseNo, studentUserNo);

        model.addAttribute("course", courseInfo);
        model.addAttribute("courseNo", courseNo);   // list 루프 내부에서도 사용 가능
    }


    // ---------------------------------------------------------
    // 강의 홈 (Dashboard)
    // ---------------------------------------------------------
    @GetMapping("/studentCourseHome")
    public String studentCourseHome(
            @RequestParam("courseNo") int courseNo,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        loadCourseInfo(courseNo, loginUser.getUserNo(), model);
        setActiveNav(model, "home");

        return "studentCourse/studentCourseHome";
    }


    // ---------------------------------------------------------
    // 공지 전체보기
    // ---------------------------------------------------------
    @GetMapping("/studentCourseNoticeList")
    public String studentCourseNoticeList(
            @RequestParam("courseNo") int courseNo,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int studentUserNo = loginUser.getUserNo();

        loadCourseInfo(courseNo, studentUserNo, model);
        setActiveNav(model, "notice");

        // 페이징 설정
        int rowPerPage = 10;
        int startRow = (currentPage - 1) * rowPerPage;

        List<StudentCourseNoticeDTO> list =
                studentCourseService.getStudentCourseNoticeList(courseNo, startRow, rowPerPage);

        int totalRow = studentCourseService.getStudentCourseNoticeTotal(courseNo);
        int lastPage = (totalRow == 0) ? 1 : ((totalRow + rowPerPage - 1) / rowPerPage);

        int pageGroup = (currentPage - 1) / 5;
        int startPage = pageGroup * 5 + 1;
        int endPage = Math.min(startPage + 4, lastPage);

        // 번호(index)
        int displayIndex = totalRow - startRow;
        for (StudentCourseNoticeDTO dto : list) {
            dto.setIndex(displayIndex--);
        }

        // PageList
        List<Map<String, Object>> pageList = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) {
            Map<String, Object> p = new HashMap<>();
            p.put("page", i);
            p.put("current", i == currentPage);
            pageList.add(p);
        }

        model.addAttribute("list", list);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("hasPrev", startPage > 1);
        model.addAttribute("hasNext", endPage < lastPage);
        model.addAttribute("prevPage", startPage - 1);
        model.addAttribute("nextPage", endPage + 1);

        return "studentCourse/studentCourseNoticeList";
    }


    // ---------------------------------------------------------
    // 공지 상세
    // ---------------------------------------------------------
    @GetMapping("/studentCourseNoticeDetail")
    public String studentCourseNoticeDetail(
            @RequestParam("courseNoticeNo") int courseNoticeNo,
            @RequestParam("courseNo") int courseNo,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        StudentCourseNoticeDTO detail =
                studentCourseService.getStudentCourseNoticeDetail(courseNoticeNo);

        model.addAttribute("detail", detail);

        loadCourseInfo(courseNo, loginUser.getUserNo(), model);
        setActiveNav(model, "notice");

        return "studentCourse/studentCourseNoticeDetail";
    }


    // ---------------------------------------------------------
    // 내 수강과목 목록
    // ---------------------------------------------------------
    @GetMapping("/myCourses")
    public String myCourses(HttpSession session, Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int studentUserNo = loginUser.getUserNo();

        List<StudentCourseDTO> list =
                studentCourseService.getMyCourseList(studentUserNo);

        model.addAttribute("courseList", list);
        model.addAttribute("nav_myCourses", "border-blue-600 text-blue-600");

        return "studentCourse/myCourses";
    }


    // ---------------------------------------------------------
    // 강의 상세보기
    // ---------------------------------------------------------
    @GetMapping("/studentCourseDetail")
    public String studentCourseDetail(
            @RequestParam("courseNo") int courseNo,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        StudentCourseDetailDTO detail =
                studentCourseService.getStudentCourseDetail(courseNo);

        model.addAttribute("detail", detail);

        loadCourseInfo(courseNo, loginUser.getUserNo(), model);
        setActiveNav(model, "home");  // or 다른 메뉴

        return "enrollment/studentCourseDetail";
    }


    // ---------------------------------------------------------
    // 학생 시간표
    // ---------------------------------------------------------
    @GetMapping("/studentTimetable")
    public String studentTimetable(HttpSession session, Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int studentUserNo = loginUser.getUserNo();

        List<StudentTimetableDTO> timetable =
                studentCourseService.getStudentTimetable(studentUserNo);

        StudentTimetableDTO[][] grid = new StudentTimetableDTO[5][8];

        for (StudentTimetableDTO t : timetable) {
            int yoilIndex = t.getCourseTimeYoil() - 1;
            for (int p = t.getCourseTimeStart(); p <= t.getCourseTimeEnd(); p++) {
                grid[yoilIndex][p - 1] = t;
            }
        }

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
}
