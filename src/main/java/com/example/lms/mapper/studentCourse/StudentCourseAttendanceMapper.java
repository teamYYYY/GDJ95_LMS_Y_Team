package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.AttendanceSummaryDTO;
import com.example.lms.dto.StudentAttendanceDTO;

@Mapper
public interface StudentCourseAttendanceMapper {

    List<StudentAttendanceDTO> selectAttendanceDetailList(@Param("courseNo") int courseNo,
                                                          @Param("studentUserNo") int studentUserNo);

    AttendanceSummaryDTO selectAttendanceSummary(@Param("courseNo") int courseNo,
                                                 @Param("studentUserNo") int studentUserNo);
}
