package com.example.lms.service.studentCourse;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.lms.dto.CourseDTO;
import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.mapper.studentCourse.StudentCourseMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentCourseService {

    private final StudentCourseMapper studentCourseMapper;

    public List<StudentCourseDTO> getCourseListForStudent() {
        return studentCourseMapper.selectCourseListForStudent();
    }
}