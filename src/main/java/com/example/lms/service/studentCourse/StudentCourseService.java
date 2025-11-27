package com.example.lms.service.studentCourse;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lms.mapper.studentCourse.StudentCourseMapper;
import com.example.lms.dto.StudentCourseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentCourseService {

    private final StudentCourseMapper studentCourseMapper;

    // 강의 목록 조회
    public List<StudentCourseDTO> getCourseListForStudent(int studentUserNo, int startRow, int rowPerPage) {
        return studentCourseMapper.selectCourseListForStudent(studentUserNo, startRow, rowPerPage);
    }

    // 전체 강의 row
    public int getTotalCourseCount() {
        return studentCourseMapper.countCourseList();
    }
}
