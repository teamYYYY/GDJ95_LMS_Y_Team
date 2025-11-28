package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.CourseTimeDTO;
import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentCourseDetailDTO;
import com.example.lms.dto.StudentTimetableDTO;

@Mapper
public interface StudentCourseMapper {
	// 학과 리스트 조회
	List<DeptDTO> selectDeptList();
	
	// 학생 수강신청 목록 조회(필터링 포함)
	List<StudentCourseDTO> selectCourseListForStudentFiltered(
            @Param("studentUserNo") int studentUserNo,
            @Param("yoil") Integer yoil,
            @Param("professor") String professor,
            @Param("deptCode") String deptCode,
            @Param("startRow") int startRow,
            @Param("rowPerPage") int rowPerPage
    );
	
	// 필터링된 강의 개수 조회(페이징용)
	int countCourseListFiltered(
            @Param("yoil") Integer yoil,
            @Param("professor") String professor,
            @Param("deptCode") String deptCode
    );
	
	// 학생 시간표 조회
	List<StudentTimetableDTO> selectStudentTimetable(@Param("studentUserNo") int studentUserNo);
	
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