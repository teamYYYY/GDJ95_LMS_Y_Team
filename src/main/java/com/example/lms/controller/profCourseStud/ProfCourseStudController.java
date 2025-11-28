package com.example.lms.controller.profCourseStud;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.lms.dto.AttendanceDTO;
import com.example.lms.dto.ProfCourseStudDTO;
import com.example.lms.service.profCourseStud.ProfCourseStudService;

@Controller
public class ProfCourseStudController {
	
	@Autowired
	ProfCourseStudService profCourseStudService;
	
	@GetMapping("/profCourseStudList")
	public String profCourseStudList(Model model,
									@RequestParam("courseNo") int courseNo,
						            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage) {
		
		int rowPerPage = 10;
	    int startRow = (currentPage - 1) * rowPerPage;
	    
	    List<ProfCourseStudDTO> list = profCourseStudService.getStudentListByProf(courseNo, startRow, rowPerPage);
	    
	    int totalRow = profCourseStudService.getStudentCountByProf(courseNo);
	    
	    int lastPage = (totalRow % rowPerPage == 0) ? (totalRow / rowPerPage) : (totalRow / rowPerPage) + 1;
	    int startPage = ((currentPage - 1) / 10 * 10) + 1;
	    int endPage = startPage + 9;
	    if (endPage > lastPage) endPage = lastPage;
	    
	    List<Integer> pages = new ArrayList<>();
	    for (int i = startPage; i <= endPage; i++) {
	        pages.add(i);
	    }
	    
	    model.addAttribute("courseNo", courseNo);
	    model.addAttribute("prePage", currentPage > 1 ? currentPage - 1 : 1);
	    model.addAttribute("nextPage", currentPage < lastPage ? currentPage + 1 : lastPage);

	    model.addAttribute("list", list);
	    model.addAttribute("currentPage", currentPage);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
	    model.addAttribute("pages", pages);

	    return "profCourseStud/profCourseStudList";
	}

}
