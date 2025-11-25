package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.StudentCourseDTO;

@Mapper
public interface StudentCourseMapper {
	// 수강 신청 가능 강의
	List<StudentCourseDTO> selectCourseListForStudent();
	 
	// 강의 정원 조회
	int getCourseCapacity(@Param("courseNo") int courseNo);
	
	// 특정 강의의 현재 신청 인원 조회
	int getCurrentEnrollment(@Param("courseNo") int courseNo);
}
