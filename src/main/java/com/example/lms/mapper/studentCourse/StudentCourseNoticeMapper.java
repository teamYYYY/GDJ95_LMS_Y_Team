package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.StudentCourseNoticeDTO;

@Mapper
public interface StudentCourseNoticeMapper {

    List<StudentCourseNoticeDTO> selectNoticeList(@Param("courseNo") int courseNo,
                                                  @Param("startRow") int startRow,
                                                  @Param("rowPerPage") int rowPerPage);

    StudentCourseNoticeDTO selectNoticeDetail(int courseNoticeNo);

    int selectNoticeTotal(int courseNo);
}
