package com.example.lms.mapper.prof;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.AttendanceDTO;
import com.example.lms.dto.ProfCourseAttendDTO;

@Mapper
public interface ProfCourseAttendMapper {
	
	// 주차 리스트
	List<Integer> selectWeekList(@Param("courseNo") int courseNo);
	
	// 특정 주차별 학생 출석 리스트
	List<ProfCourseAttendDTO> selectCourseAttendList(@Param("courseNo") int courseNo,
            										@Param("weekNo") int weekNo);
	
	// 출석 저장
	int upsertCourseAttend(AttendanceDTO attendance);

}
