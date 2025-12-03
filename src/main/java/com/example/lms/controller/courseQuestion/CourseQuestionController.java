package com.example.lms.controller.courseQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.CourseQuestionAnswerDTO;
import com.example.lms.dto.CourseQuestionDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.courseQuestion.CourseQuestionService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CourseQuestionController {

    private final CourseQuestionService courseQuestionService;

    // 질문 등록 처리
    @PostMapping("/courseQuestionWrite")
    public String courseQuestionWrite(HttpSession session,
                                      @ModelAttribute CourseQuestionDTO q) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        q.setWriterUserNo(loginUser.getUserNo());
        courseQuestionService.insertQuestion(q);

        return "redirect:/courseQuestionList?courseNo=" + q.getCourseNo();
    }

    // 질문 작성 폼
    @GetMapping("/courseQuestionWriteForm")
    public String courseQuestionWriteForm(@RequestParam int courseNo,
                                          HttpSession session,
                                          Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        model.addAttribute("courseNo", courseNo);
        return "courseQuestion/courseQuestionWriteForm";
    }

    // 질문 목록
    @GetMapping("/courseQuestionList")
    public String courseQuestionList(@RequestParam int courseNo,
                                     HttpSession session,
                                     Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        List<CourseQuestionDTO> rawList =
                courseQuestionService.getQuestionList(courseNo, loginUser);

        List<Map<String, Object>> viewList = new ArrayList<>();

        for (CourseQuestionDTO q : rawList) {

            Map<String, Object> row = new HashMap<>();

            boolean isOwner = q.getWriterUserNo() == loginUser.getUserNo();
            boolean isProfessor = loginUser.getUserGrade() == 2;
            boolean canView = !q.isPrivatePost() || isOwner || isProfessor;

            row.put("courseQuestionNo", q.getCourseQuestionNo());
            row.put("createdate", q.getCreatedate());
            row.put("privatePost", q.isPrivatePost());
            row.put("canView", canView);

            if (!canView) {
                row.put("courseQuestionTitle", "비밀글입니다.");
                row.put("writerName", "비공개");
            } else {
                row.put("courseQuestionTitle", q.getCourseQuestionTitle());
                row.put("writerName", q.getWriterName());
            }

            viewList.add(row);
        }

        model.addAttribute("courseNo", courseNo);
        model.addAttribute("questionList", viewList);

        return "courseQuestion/courseQuestionList";
    }

    // 질문 상세 (질문 + 댓글)
    @GetMapping("/courseQuestionDetail")
    public String courseQuestionDetail(@RequestParam int courseQuestionNo,
                                       HttpSession session,
                                       Model model) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        CourseQuestionDTO question =
                courseQuestionService.getQuestionDetail(courseQuestionNo, loginUser);

        if (!question.isCanView()) {
            return "redirect:/courseQuestionList?courseNo=" + question.getCourseNo();
        }

        model.addAttribute("question", question);
        return "courseQuestion/courseQuestionDetail";
    }

    // 댓글 등록
    @PostMapping("/addAnswer")
    public String addAnswer(HttpSession session,
                            @ModelAttribute CourseQuestionAnswerDTO answerDTO) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        answerDTO.setWriterUserNo(loginUser.getUserNo());
        answerDTO.setWriterRole("STUDENT"); 

        courseQuestionService.addAnswer(answerDTO);

        return "redirect:/courseQuestionDetail?courseQuestionNo=" + answerDTO.getCourseQuestionNo();
    }
}
