package com.example.lms.controller.prof;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.AssignmentDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.service.prof.ProfAssignmentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProfAssignmentController {

    @Autowired
    ProfAssignmentService assignmentService;

    // 리스트
    @GetMapping("/assignmentListByProf")
    public String getAssignmentListByProf(
            Model model,
            @RequestParam("courseNo") int courseNo,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {

        int rowPerPage = 10;
        int startRow = (currentPage - 1) * rowPerPage;

        List<AssignmentDTO> list = assignmentService.getAssignmentListByProf(courseNo, startRow, rowPerPage);

        int totalRow = assignmentService.getAssignmentCount(courseNo);
        int lastPage = (totalRow % rowPerPage == 0) ? (totalRow / rowPerPage) : (totalRow / rowPerPage) + 1;

        int startPage = ((currentPage - 1) / 10 * 10) + 1;
        int endPage = Math.min(startPage + 9, lastPage);

        List<Integer> pages = new ArrayList<>();
        for (int i = startPage; i <= endPage; i++) pages.add(i);

        model.addAttribute("list", list);
        model.addAttribute("courseNo", courseNo);
        model.addAttribute("prePage", currentPage > 1 ? currentPage - 1 : 1);
        model.addAttribute("nextPage", currentPage < lastPage ? currentPage + 1 : lastPage);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("pages", pages);

        return "profAssignment/assignmentListByProf";
    }

    // 상세보기
    @GetMapping("/assignmentDetail")
    public String assignmentDetail(
            Model model,
            @RequestParam("assignmentNo") int assignmentNo,
            @RequestParam("courseNo") int courseNo) {

        AssignmentDTO assignmentDTO = assignmentService.getAssignmentDetail(assignmentNo);
        model.addAttribute("assignment", assignmentDTO);
        model.addAttribute("courseNo", courseNo);

        return "profAssignment/assignmentDetail";
    }

    // 등록 폼
    @GetMapping("/addAssignment")
    public String addAssignmentForm(Model model, HttpSession session, @RequestParam("courseNo") int courseNo) {

        SysUserDTO loginUser = (SysUserDTO) session.getAttribute("loginUser");
        if (loginUser == null || !loginUser.getUserAuth().startsWith("P_")) {
		    return "redirect:/login";
		}

        model.addAttribute("courseNo", courseNo);

        return "profAssignment/addAssignment";
    }

    // 등록 처리
    @PostMapping("/addAssignment")
    public String addAssignment(AssignmentDTO assignment, HttpSession session) {

        assignmentService.addAssignment(assignment);

        return "redirect:/assignmentListByProf?courseNo=" + assignment.getCourseNo();
    }

    // 수정 폼
    @GetMapping("/modifyAssignment")
    public String modifyAssignmentForm(Model model, 
    									HttpSession session,
    									@RequestParam("assignmentNo") int assignmentNo) {

        AssignmentDTO assignment = assignmentService.getAssignmentDetail(assignmentNo);
        model.addAttribute("assignment", assignment);
        
        model.addAttribute("assignmentStatus1", assignment.getAssignmentStatus() == 1);
        model.addAttribute("assignmentStatus0", assignment.getAssignmentStatus() == 0);


        return "profAssignment/modifyAssignment";
    }

    // 수정 처리
    @PostMapping("/modifyAssignment")
    public String modifyAssignment(AssignmentDTO assignment) {

        assignmentService.modifyAssignment(assignment);

        return "redirect:/assignmentDetail?assignmentNo=" 
                + assignment.getAssignmentNo()
                + "&courseNo=" + assignment.getCourseNo();
    }

    // 삭제
    @GetMapping("/removeAssignment")
    public String removeAssignment(@RequestParam int assignmentNo) {

        AssignmentDTO assignment = assignmentService.getAssignmentDetail(assignmentNo);
        int courseNo = assignment.getCourseNo();

        assignmentService.removeAssignment(assignmentNo);

        return "redirect:/assignmentListByProf?courseNo=" + courseNo;
    }
}
