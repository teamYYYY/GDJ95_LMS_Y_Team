package com.example.lms.mapper.profCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.CourseDTO;

@Mapper
public interface CourseMapper {
	
	// 교수별 강의 리스트
	List<CourseDTO> selectCourseListByProfessor(int professorUserNo);
	
	// 등록
	int insertCourse(CourseDTO c);
	
	// 대시보드
    CourseDTO selectCourseDetail(int courseNo);
	
	// 수정
    int updateCourse(CourseDTO c);

    // 삭제
    int deleteCourse(int courseNo);
	
}
