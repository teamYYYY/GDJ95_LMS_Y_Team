package com.example.lms.mapper.prof;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.ProfCourseGradeDTO;

@Mapper
public interface ProfCourseGradeMapper {

	public void updateGrade(@Param("studentUserNo") int studentUserNo,
					        @Param("courseNo") int courseNo,
					        @Param("examScore") double examScore,
					        @Param("assignmentScore") double assignmentScore,
					        @Param("gradeValue") String gradeValue,
					        @Param("finalScore") double finalScore);
	
	public ProfCourseGradeDTO selectGradeData(@Param("courseNo") int courseNo,  @Param("studentUserNo") int studentUserNo);

	public int existsGrade(int studentUserNo, int courseNo);

	public void insertGrade(int studentUserNo, int courseNo, double examScore, double assignmentScore, String gradeValue, double finalScore);
}
