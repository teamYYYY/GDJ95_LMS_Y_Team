package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.CourseTimeDTO;
import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentCourseDetailDTO;

@Mapper
public interface StudentCourseMapper {
	// 상세보기
	StudentCourseDetailDTO selectStudentCourseDetail(@Param("courseNo") int courseNo);

	// 상세보기 - 시간표 리스트
	List<CourseTimeDTO> selectCourseTimeList(@Param("courseNo") int courseNo);
	
    // 학생용 수강신청 리스트 조회 (학생번호 + 페이징)
    List<StudentCourseDTO> selectCourseListForStudent(
            @Param("studentUserNo") int studentUserNo,
            @Param("startRow") int startRow,
            @Param("rowPerPage") int rowPerPage
    );

    // 전체 강의 수
    int countCourseList();
}