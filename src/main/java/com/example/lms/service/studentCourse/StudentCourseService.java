package com.example.lms.service.studentCourse;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lms.mapper.studentCourse.StudentCourseMapper;
import com.example.lms.dto.CourseTimeDTO;
import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentCourseDetailDTO;
import com.example.lms.dto.StudentTimetableDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentCourseService {
    private final StudentCourseMapper studentCourseMapper;
    // 필터 적용 강의 목록 조회
    public List<StudentCourseDTO> getCourseListForStudentFiltered(
            int studentUserNo,
            Integer yoil,
            String professor,
            String deptCode,
            int startRow,
            int rowPerPage) {

        return studentCourseMapper.selectCourseListForStudentFiltered(
                studentUserNo, yoil, professor, deptCode, startRow, rowPerPage
        );
    }
    
    // 필터 적용된 전체 row 개수
    public int countFilteredCourseList(Integer yoil, String professor, String deptCode) {
        return studentCourseMapper.countCourseListFiltered(yoil, professor, deptCode);
    }
    
    // 학생 시간표 조회
    public List<StudentTimetableDTO> getStudentTimetable(int studentUserNo) {
        return studentCourseMapper.selectStudentTimetable(studentUserNo);
    }

    // 강의 목록 조회
    public List<StudentCourseDTO> getCourseListForStudent(int studentUserNo, int startRow, int rowPerPage) {
        return studentCourseMapper.selectCourseListForStudent(studentUserNo, startRow, rowPerPage);
    }

    // 전체 강의 row
    public int getTotalCourseCount() {
        return studentCourseMapper.countCourseList();
    }
    
    // 강의 상세조회(1개)
    public StudentCourseDetailDTO getStudentCourseDetail(int courseNo) {
    	StudentCourseDetailDTO detail =
                studentCourseMapper.selectStudentCourseDetail(courseNo);
    	
    	if(detail == null) return null;
    	
    	// 시간표 여러개
    	List<CourseTimeDTO> timeList = studentCourseMapper.selectCourseTimeList(courseNo);
    	detail.setCourseTimeList(timeList);
    	
    	return detail;
    }
    
    // 학과별리스트
    public List<DeptDTO> getDeptList() {
        return studentCourseMapper.selectDeptList();
    }

}
