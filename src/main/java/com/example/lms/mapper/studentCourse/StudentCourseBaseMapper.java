package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentTimetableDTO;

@Mapper
public interface StudentCourseBaseMapper {

    // ----------------------------
    // 내 수강과목 목록
    // ----------------------------
    List<StudentCourseDTO> selectMyCourseList(int studentUserNo);

    // ----------------------------
    // 학생 시간표
    // ----------------------------
    List<StudentTimetableDTO> selectStudentTimetable(int studentUserNo);
}
