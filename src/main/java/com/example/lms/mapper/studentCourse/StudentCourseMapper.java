package com.example.lms.mapper.studentCourse;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.AttendanceSummaryDTO;
import com.example.lms.dto.CourseTimeDTO;
import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.GradeSummaryDTO;
import com.example.lms.dto.StudentAssignmentDTO;
import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentCourseDetailDTO;
import com.example.lms.dto.StudentCourseHomeDTO;
import com.example.lms.dto.StudentCourseNoticeDTO;
import com.example.lms.dto.StudentQuestionDTO;
import com.example.lms.dto.StudentTimetableDTO;

@Mapper
public interface StudentCourseMapper {

	// 학생용 공지 목록
    List<StudentCourseNoticeDTO> selectStudentCourseNoticeList(
            @Param("courseNo") int courseNo,
            @Param("startRow") int startRow,
            @Param("rowPerPage") int rowPerPage);

    // 학생용 공지 total
    int selectStudentCourseNoticeTotal(int courseNo);

    // 학생용 공지 상세
    StudentCourseNoticeDTO selectStudentCourseNoticeDetail(int courseNoticeNo);

    // 조회수 증가
    int updateStudentCourseNoticeViewCount(int courseNoticeNo);
    
    // 강의 기본 정보 조회
    StudentCourseHomeDTO selectCourseBasicInfo(@Param("courseNo") int courseNo);

    // 최근 공지사항 3개 조회
    List<StudentCourseNoticeDTO> selectRecentNotices(@Param("courseNo") int courseNo);

    // 과제 요약 (학생 기준)
    StudentAssignmentDTO selectAssignmentSummary(
            @Param("courseNo") int courseNo,
            @Param("studentUserNo") int studentUserNo);

    // 출석 요약 (학생 기준)
    AttendanceSummaryDTO selectAttendanceSummary(
            @Param("courseNo") int courseNo,
            @Param("studentUserNo") int studentUserNo);

    // 성적 요약 (학생 기준)
    GradeSummaryDTO selectGradeSummary(
            @Param("courseNo") int courseNo,
            @Param("studentUserNo") int studentUserNo);

    // 최근 질문 3개 조회
    List<StudentQuestionDTO> selectRecentQuestions(
            @Param("courseNo") int courseNo,
            @Param("studentUserNo") int studentUserNo);

    // 내 수강 과목 목록
    List<StudentCourseDTO> selectMyCourseList(@Param("studentUserNo") int studentUserNo);

    // 수강신청 목록 조회 (필터 + 페이징)
    List<StudentCourseDTO> selectCourseListForStudentFiltered(
            @Param("studentUserNo") int studentUserNo,
            @Param("yoil") Integer yoil,
            @Param("professor") String professor,
            @Param("deptCode") String deptCode,
            @Param("startRow") int startRow,
            @Param("rowPerPage") int rowPerPage);

    // 필터링된 강의 TOTAL COUNT
    int countCourseListFiltered(
            @Param("yoil") Integer yoil,
            @Param("professor") String professor,
            @Param("deptCode") String deptCode);

    // 전체 강의 목록 조회 (페이징)
    List<StudentCourseDTO> selectCourseListForStudent(
            @Param("studentUserNo") int studentUserNo,
            @Param("startRow") int startRow,
            @Param("rowPerPage") int rowPerPage);

    // 전체 강의 TOTAL COUNT
    int countCourseList();

    // 학생 시간표 조회
    List<StudentTimetableDTO> selectStudentTimetable(@Param("studentUserNo") int studentUserNo);

    // 강의 상세 정보 조회
    StudentCourseDetailDTO selectStudentCourseDetail(@Param("courseNo") int courseNo);

    // 강의 시간표 리스트 조회
    List<CourseTimeDTO> selectCourseTimeList(@Param("courseNo") int courseNo);

    // 학과 목록 조회
    List<DeptDTO> selectDeptList();

}
