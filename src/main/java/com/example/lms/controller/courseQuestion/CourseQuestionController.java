package com.example.lms.controller.courseQuestion;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.CourseQuestionDTO;
import com.example.lms.dto.CourseQuestionAnswerDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.courseQuestion.CourseQuestionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CourseQuestionController {

    private final CourseQuestionService service;

    // 문의 목록 + 페이징
    @GetMapping("/courseQuestionList")
    public String courseQuestionList(
            @RequestParam int courseNo,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            Model model, HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        int rowPerPage = 10;

        // 전체 개수
        int totalCount = service.getTotalQuestionCount(courseNo);
        int lastPage = (int) Math.ceil((double) totalCount / rowPerPage);

        if (currentPage < 1) currentPage = 1;
        if (currentPage > lastPage) currentPage = lastPage;

        // 페이징 목록
        List<CourseQuestionDTO> list =
                service.getPagedQuestionList(courseNo, loginUser, currentPage, rowPerPage);

        // 페이지 블럭 (공지랑 구조 동일)
        int pagePerBlock = 5;
        int blockStartPage = ((currentPage - 1) / pagePerBlock) * pagePerBlock + 1;
        int blockEndPage = Math.min(blockStartPage + pagePerBlock - 1, lastPage);

        model.addAttribute("questionList", list);
        model.addAttribute("courseNo", courseNo);

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("lastPage", lastPage);

        model.addAttribute("blockStart", blockStartPage);
        model.addAttribute("blockEnd", blockEndPage);

        return "courseQuestion/courseQuestionList";
    }

    // 문의 상세
    @GetMapping("/courseQuestionDetail")
    public String detail(
            @RequestParam int courseQuestionNo,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        CourseQuestionDTO question = service.getQuestionDetail(courseQuestionNo, loginUser);
        
        boolean isOwner = loginUser.getUserNo() == question.getWriterUserNo();
        model.addAttribute("isOwner", isOwner);
        
        model.addAttribute("question", question);
        model.addAttribute("courseNo", question.getCourseNo());

        return "courseQuestion/courseQuestionDetail";
    }

    // 문의 작성 폼
    @GetMapping("/courseQuestionWriteForm")
    public String writeForm(@RequestParam int courseNo, Model model) {
        model.addAttribute("courseNo", courseNo);
        return "courseQuestion/courseQuestionWriteForm";
    }

    // 문의 작성 처리
    @PostMapping("/courseQuestionWrite")
    public String write(CourseQuestionDTO dto, HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        dto.setWriterUserNo(loginUser.getUserNo());
        service.insertQuestion(dto);

        return "redirect:/courseQuestionList?courseNo=" + dto.getCourseNo();
    }

    // 문의 수정 폼
    @GetMapping("/courseQuestionEditForm")
    public String editForm(@RequestParam int courseQuestionNo, HttpSession session, Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        CourseQuestionDTO q = service.getQuestionDetail(courseQuestionNo, loginUser);
        model.addAttribute("question", q);

        return "courseQuestion/courseQuestionEditForm";
    }

    // 문의 수정 처리
    @PostMapping("/courseQuestionEdit")
    public String edit(CourseQuestionDTO dto, HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        boolean ok = service.updateQuestion(dto, loginUser);
        if (!ok) return "redirect:/accessDenied";

        return "redirect:/courseQuestionDetail?courseQuestionNo=" + dto.getCourseQuestionNo();
    }

    // 문의 삭제
    @GetMapping("/courseQuestionDelete")
    public String delete(@RequestParam int courseQuestionNo, HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        // 삭제 전에 courseNo 먼저 가져와야 함
        int courseNo = service.getCourseNoByQuestion(courseQuestionNo);

        boolean ok = service.deleteQuestion(courseQuestionNo, loginUser);
        if (!ok) return "redirect:/accessDenied";

        return "redirect:/courseQuestionList?courseNo=" + courseNo;
    }


    // 댓글 작성
    @PostMapping("/addAnswer")
    public String addAnswer(CourseQuestionAnswerDTO dto, HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        dto.setWriterUserNo(loginUser.getUserNo());
        dto.setWriterRole(loginUser.getUserAuth());

        service.addAnswer(dto);

        return "redirect:/courseQuestionDetail?courseQuestionNo=" + dto.getCourseQuestionNo();
    }

    // 댓글 수정 폼
    @GetMapping("/courseQuestionAnswerEditForm")
    public String answerEditForm(
            @RequestParam int answerNo,
            @RequestParam int courseQuestionNo,
            HttpSession session,
            Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        CourseQuestionDTO q = service.getQuestionDetail(courseQuestionNo, loginUser);

        CourseQuestionAnswerDTO answer = q.getAnswerList().stream()
                .filter(a -> a.getAnswerNo() == answerNo)
                .findFirst()
                .orElse(null);

        model.addAttribute("answer", answer);
        model.addAttribute("courseQuestionNo", courseQuestionNo);

        return "courseQuestion/courseQuestionAnswerEditForm";
    }

    // 댓글 수정 처리
    @PostMapping("/courseQuestionAnswerEdit")
    public String answerEdit(CourseQuestionAnswerDTO dto, HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        boolean ok = service.updateAnswer(dto, loginUser);
        if (!ok) return "redirect:/accessDenied";

        return "redirect:/courseQuestionDetail?courseQuestionNo=" + dto.getCourseQuestionNo();
    }
}
