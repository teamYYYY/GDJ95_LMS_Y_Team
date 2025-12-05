package com.example.lms.mapper.studentCourse;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.StudentGradeDTO;

@Mapper
public interface StudentGradeMapper {

    StudentGradeDTO selectStudentGrade(@Param("courseNo") int courseNo,
                                       @Param("studentUserNo") int studentUserNo);
}
