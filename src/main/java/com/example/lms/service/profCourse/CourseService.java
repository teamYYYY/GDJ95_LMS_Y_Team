package com.example.lms.service.profCourse;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.ProfCourseDTO;
import com.example.lms.dto.ProfCourseTimeDTO;
import com.example.lms.mapper.profCourse.CourseMapper;

@Service
@Transactional
public class CourseService {
	          
	@Autowired
	private CourseMapper courseMapper;
	
	// 교수별 강의 리스트
	public List<ProfCourseDTO> getCourseListByProfessor(int professorUserNo) {
		return courseMapper.selectCourseListByProf(professorUserNo);
	}
	
	// 강의 + 강의 시간 등록
	public void addCourse(ProfCourseDTO course, List<ProfCourseTimeDTO> timeList) {
		
		// 1) 강의 등록
		courseMapper.insertCourse(course);
		int courseNo = course.getCourseNo();
		
		// 2) 강의 시간 등록
		for (ProfCourseTimeDTO t : timeList) {
			
			// 중복 체크
			int dup = courseMapper.checkDuplicateTime(
					t.getCourseTimeYoil(),
					t.getCourseTimeStart(),
					t.getCourseTimeEnd(),
					course.getCourseClassroom()
			);
			
			if (dup > 0) {
                throw new RuntimeException("강의 시간 중복 발생");
            }

            t.setCourseNo(courseNo);
            t.setProfessorUserNo(course.getProfessorUserNo());

            courseMapper.insertCourseTime(t);
		}
	}
	
	// 대시보드 (강의 + 강의시간 리스트로 묶기)
	public ProfCourseDTO getCourseDetail(int courseNo) {

	    List<ProfCourseDTO> raw = courseMapper.selectCourseDetail(courseNo);

	    if (raw == null || raw.isEmpty()) return null;

	    // 첫 행에 기본 강의 정보, 학과 정보가 다 들어있음
	    ProfCourseDTO course = raw.get(0);
	    course.setTimeList(new ArrayList<>());

	    for (ProfCourseDTO row : raw) {
	        if (row.getCourseTimeYoil() != null) {
	            ProfCourseTimeDTO t = new ProfCourseTimeDTO();
	            t.setCourseTimeYoil(row.getCourseTimeYoil());
	            t.setCourseTimeStart(row.getCourseTimeStart());
	            t.setCourseTimeEnd(row.getCourseTimeEnd());
	            course.getTimeList().add(t);
	        }
	    }

	    return course;
	}

	
	// 수정 (강의 + 강의 시간 전체 갈아끼우기)
    public void modifyCourse(ProfCourseDTO course, List<ProfCourseTimeDTO> timeList) {

        // 1) 강의 정보 수정
        courseMapper.updateCourse(course);

        // 2) 기존 시간 삭제
        courseMapper.deleteCourseTime(course.getCourseNo());

        // 3) 새 시간 등록
        for (ProfCourseTimeDTO t : timeList) {

            int dup = courseMapper.checkDuplicateTime(
            		t.getCourseTimeYoil(),
            		t.getCourseTimeStart(),
            		t.getCourseTimeEnd(),
            		course.getCourseClassroom()
            );

            if (dup > 0) {
                throw new RuntimeException("시간 중복 발생");
            }

            t.setCourseNo(course.getCourseNo());
            t.setProfessorUserNo(course.getProfessorUserNo());

            courseMapper.insertCourseTime(t);
        }
    }

    // 삭제
    public void removeCourse(int courseNo) {

        // 강의 시간 삭제
        courseMapper.deleteCourseTime(courseNo);

        // 강의 삭제
        courseMapper.deleteCourse(courseNo);
    }
}
