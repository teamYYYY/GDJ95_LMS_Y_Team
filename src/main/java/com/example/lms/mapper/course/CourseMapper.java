package com.example.lms.mapper.course;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.CourseDTO;


@Mapper
public interface CourseMapper {
	List<CourseDTO> selectCourseListByPage(int startRow, int rowPerPage);
	
	CourseDTO selectCourseDetail(int courseNo);
}
