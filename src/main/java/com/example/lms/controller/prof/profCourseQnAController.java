package com.example.lms.controller.prof;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.prof.ProfCourseQnAService;

import jakarta.servlet.http.HttpSession;

@Controller
public class profCourseQnAController {
	
	@Autowired
	ProfCourseQnAService profCourseQnAService;
	
	// 문의 목록
	@GetMapping("/profCourseQnAList")
	public String profCourseQuestionList(@RequestParam("courseNo") int courseNo,
										Model model,
										HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.getUserAuth().startsWith("P_")) {
            return "redirect:/login";
        }
        
        List<Map<String, Object>> list = profCourseQnAService.getQuestionList(courseNo);
        System.out.println("===== DEBUG =====");
        for(Map<String, Object> row : list) {
            System.out.println("answered = " + row.get("answered"));
        }
        model.addAttribute("list", list);

        
       // model.addAttribute("list", profCourseQnAService.getQuestionList(courseNo));
        model.addAttribute("courseNo", courseNo);
        return "profCourseQnA/profCourseQnAList";
	}
	
	// 상세
	@GetMapping("/profCourseQnADetail")
	public String profCourseQuestionDetail(@RequestParam("courseQuestionNo") int courseQuestionNo,
											@RequestParam("courseNo") int courseNo,
											Model model,
											HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.getUserAuth().startsWith("P_")) {
            return "redirect:/login";
        }
        
        model.addAttribute("question",profCourseQnAService.getQuestionDetail(courseQuestionNo));
        model.addAttribute("answers", profCourseQnAService.getAnswerList(courseQuestionNo));
        model.addAttribute("courseQuestionNo", courseQuestionNo);
        model.addAttribute("courseNo", courseNo);
        
        return "profCourseQnA/profCourseQnADetail";
	}
	
	// 답변 작성
	@GetMapping("/profCourseAnswerForm")
	public String profCourseAnswerForm(@RequestParam("courseQuestionNo") int courseQuestionNo,
										@RequestParam("courseNo") int courseNo,
										Model model,
										HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.getUserAuth().startsWith("P_")) {
            return "redirect:/login";
        }
        
        model.addAttribute("courseQuestionNo", courseQuestionNo);
        model.addAttribute("courseNo", courseNo);
        
        return "profCourseQnA/profCourseAnswerForm";
	}
	
	@PostMapping("/profCourseAnswer")
	public String profCourseAnswer(@RequestParam("courseQuestionNo") int courseQuestionNo,
                                  @RequestParam("courseNo") int courseNo,
                                  @RequestParam("answerContent") String answerContent,
                                  HttpSession session) {
		
		SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
		
		profCourseQnAService.addAnswer(courseQuestionNo, loginUser.getUserNo(), answerContent);
		
		return "redirect:/profCourseQnADetail?courseQuestionNo=" + courseQuestionNo + "&courseNo=" + courseNo;
	}
	
	// 답변 수정
	@PostMapping("/profCourseAnswerUpdate")
	public String profCourseAnswerUpdate(@RequestParam("answerNo") int answerNo,
								        @RequestParam("courseQuestionNo") int courseQuestionNo,
								        @RequestParam("courseNo") int courseNo,
								        @RequestParam("answerContent") String answerContent) {
		
		profCourseQnAService.updateAnswer(answerNo, answerContent);
		
		return "redirect:/profCourseQnADetail?courseQuestionNo=" + courseQuestionNo + "&courseNo=" + courseNo;
	}
	
	// 답변 삭제
    @GetMapping("/profCourseAnswerDelete")
    public String profAnswerDelete(@RequestParam("answerNo") int answerNo,
                                   @RequestParam("courseQuestionNo") int courseQuestionNo,
                                   @RequestParam("courseNo") int courseNo) {

    	profCourseQnAService.deleteAnswer(answerNo, courseQuestionNo);

        return "redirect:/profCourseQnADetail?courseQuestionNo=" + courseQuestionNo + "&courseNo=" + courseNo;
    }
}