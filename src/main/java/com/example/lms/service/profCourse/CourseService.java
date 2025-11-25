package com.example.lms.service.profCourse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.CourseDTO;
import com.example.lms.mapper.profCourse.CourseMapper;

@Service
@Transactional
public class CourseService {
	@Autowired
	CourseMapper courseMapper;
	
	// 교수별 강의 리스트
	public List<CourseDTO> getCourseListByProfessor(int professorUserNo) {
	    return courseMapper.selectCourseListByProfessor(professorUserNo);
	}
	
	// 등록
	public int addCourse(CourseDTO course) {
		return courseMapper.insertCourse(course);
	}
	
	// 대시보드
    public CourseDTO getCourseDetail(int courseNo) {
        return courseMapper.selectCourseDetail(courseNo);
    }
	
	// 수정
    public int modifyCourse(CourseDTO c) {
        return courseMapper.updateCourse(c);
    }

    // 삭제
    public int removeCourse(int courseNo) {
        return courseMapper.deleteCourse(courseNo);
    }
}
     