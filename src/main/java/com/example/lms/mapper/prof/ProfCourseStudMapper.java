package com.example.lms.mapper.prof;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.ProfCourseStudDTO;

@Mapper
public interface ProfCourseStudMapper {
	
	// 강의별 수강생 리스트
	List<ProfCourseStudDTO> selectStudentListByProf(@Param("courseNo") int courseNo,
											        @Param("startRow") int startRow,
											        @Param("rowPerPage") int rowPerPage);
	
	int selectStudentCountByProf(@Param("courseNo") int courseNo);
	
	double selectAttendanceRate(@Param("courseNo") int courseNo, @Param("studentUserNo") int studentUserNo);
}
