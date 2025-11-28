package com.example.lms.mapper.profCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.ProfCourseDTO;
import com.example.lms.dto.ProfCourseTimeDTO;

@Mapper
public interface CourseMapper {
	
	// 교수별 강의 리스트
	List<ProfCourseDTO> selectCourseListByProf(int professorUserNo);
	
	// 등록
	int insertCourse(ProfCourseDTO c);
	
	// 강의 시간 등록
	int insertCourseTime(ProfCourseTimeDTO pct);
	
	// 시간 중복 체크
	int checkDuplicateTime(
	        @Param("yoil") int yoil,
	        @Param("start") int start,
	        @Param("end") int end,
	        @Param("classroom") String classroom
	);
	
	// 학과 목록 조회
	List<DeptDTO> selectDeptList();
	
	// 대시보드 (강의 + 학과 + 강의시간)
	List<ProfCourseDTO> selectCourseDetail(int courseNo);
	
	// 수정
    int updateCourse(ProfCourseDTO c);

    // 삭제
    int deleteCourse(int courseNo);
    
    // 강의 시간 삭제
    int deleteCourseTime(int courseNo);
}
