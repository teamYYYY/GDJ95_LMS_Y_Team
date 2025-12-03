package com.example.lms.controller.courseQuestion;

import java.util.List;

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

    // 질문등록
    @PostMapping("/courseQuestionWrite")
    public String courseQuestionWrite(HttpSession session,
                                      @ModelAttribute CourseQuestionDTO q) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        q.setWriterUserNo(loginUser.getUserNo());
        // 공개/비밀은 form 에서 isPrivate 라디오로 넘어옴

        courseQuestionService.insertQuestion(q);

        return "redirect:/courseQuestionList?courseNo=" + q.getCourseNo();
    }

    // 질문 폼
    @GetMapping("/courseQuestionWriteForm")
    public String courseQuestionWriteForm(@RequestParam("courseNo") int courseNo,
                                          Model model,
                                          HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        model.addAttribute("courseNo", courseNo);

        return "courseQuestion/courseQuestionWriteForm";
    }

    // 질문 목록
    @GetMapping("/courseQuestionList")
    public String courseQuestionList(@RequestParam("courseNo") int courseNo,
                                     Model model,
                                     HttpSession session) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        List<CourseQuestionDTO> questionList =
                courseQuestionService.getQuestionList(courseNo, loginUser);

        model.addAttribute("questionList", questionList);
        model.addAttribute("courseNo", courseNo);

        return "courseQuestion/courseQuestionList";
    }

    // 질문 상세(질문 + 댓글)
    @GetMapping("/courseQuestionDetail")
    public String courseQuestionDetail(Model model,
                                       HttpSession session,
                                       @RequestParam("courseQuestionNo") int courseQuestionNo) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        CourseQuestionDTO question =
                courseQuestionService.getQuestionDetail(courseQuestionNo, loginUser);

        model.addAttribute("question", question);

        return "courseQuestion/courseQuestionDetail";
    }

    // 댓글 등록
    @PostMapping("/addAnswer")
    public String addAnswer(HttpSession session,
                            @ModelAttribute CourseQuestionAnswerDTO answerDTO) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        answerDTO.setWriterUserNo(loginUser.getUserNo());
        answerDTO.setWriterRole("STUDENT"); // 교수 쪽 만들면 여기 분기

        courseQuestionService.addAnswer(answerDTO);

        return "redirect:/courseQuestionDetail?courseQuestionNo=" + answerDTO.getCourseQuestionNo();
    }
}