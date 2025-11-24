package com.example.lms.service.course;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.CourseDTO;
import com.example.lms.mapper.course.CourseMapper;

@Service
@Transactional
public class CourseService {
	@Autowired
	CourseMapper courseMapper;
	
	public List<CourseDTO> getCourseListByPage(int currentPage) {
		int rowPerPage = 10;
		int startRow = (currentPage - 1) * rowPerPage;
		return courseMapper.selectCourseListByPage(startRow, rowPerPage);
	}
	
	public CourseDTO getCourseDetail(int courseNo) {
		return courseMapper.selectCourseDetail(courseNo);
	}
}
