package com.example.lms.service.studentCourse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.lms.dto.AttendanceSummaryDTO;
import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.GradeSummaryDTO;
import com.example.lms.dto.StudentAssignmentDTO;
import com.example.lms.dto.StudentCourseDTO;
import com.example.lms.dto.StudentCourseDetailDTO;
import com.example.lms.dto.StudentCourseHomeDTO;
import com.example.lms.dto.StudentCourseNoticeDTO;
import com.example.lms.dto.StudentQuestionDTO;
import com.example.lms.dto.StudentTimetableDTO;
import com.example.lms.mapper.studentCourse.StudentCourseMapper;

@Service
public class StudentCourseService {

    @Autowired
    private StudentCourseMapper studentCourseMapper;
    
    public List<StudentQuestionDTO> getRecentQuestionList(int courseNo, int studentUserNo) {

        List<StudentQuestionDTO> list = studentCourseMapper.selectRecentQuestions(courseNo);

        for (StudentQuestionDTO q : list) {

            boolean isPrivate = Boolean.TRUE.equals(q.getPrivatePost());
            boolean isWriter = q.getWriterUserNo() == studentUserNo;

            boolean canView = !isPrivate || isWriter;
            q.setCanView(canView);

            if (!canView) {
                q.setQuestionTitle("비밀글입니다.");
            }
        }

        return list;
    }

    // 공지 목록
    public List<StudentCourseNoticeDTO> getStudentCourseNoticeList(int courseNo, int startRow, int rowPerPage) {
        return studentCourseMapper.selectStudentCourseNoticeList(courseNo, startRow, rowPerPage);
    }

    // total
    public int getStudentCourseNoticeTotal(int courseNo) {
        return studentCourseMapper.selectStudentCourseNoticeTotal(courseNo);
    }

    // 상세
    public StudentCourseNoticeDTO getStudentCourseNoticeDetail(int courseNoticeNo) {

        // 조회수 증가
        studentCourseMapper.updateStudentCourseNoticeViewCount(courseNoticeNo);

        // 상세 조회
        return studentCourseMapper.selectStudentCourseNoticeDetail(courseNoticeNo);
    }
    
    // ---------------------------------------------------------
    // 강의 홈 화면 (studentCourseHome)
    // ---------------------------------------------------------
    public StudentCourseHomeDTO getStudentCourseHome(int courseNo, int studentUserNo) {

        StudentCourseHomeDTO dto = new StudentCourseHomeDTO();

        // 1) 기본 정보
        StudentCourseHomeDTO baseInfo = studentCourseMapper.selectCourseBasicInfo(courseNo);
        if (baseInfo != null) {
            dto.setCourseNo(baseInfo.getCourseNo());
            dto.setCourseName(baseInfo.getCourseName());
            dto.setProfessorName(baseInfo.getProfessorName());
            dto.setCourseScore(baseInfo.getCourseScore());
            dto.setClassroom(baseInfo.getClassroom());
            dto.setCourseTimeYoil(baseInfo.getCourseTimeYoil());
            dto.setCourseTimeStart(baseInfo.getCourseTimeStart());
            dto.setCourseTimeEnd(baseInfo.getCourseTimeEnd());
        }

        // 2) 최근 공지 3개
        List<StudentCourseNoticeDTO> notices = studentCourseMapper.selectRecentNotices(courseNo);

        if (notices.size() > 0) {
            dto.setNoticeNo1(notices.get(0).getCourseNoticeNo());
            dto.setNoticeTitle1(notices.get(0).getCourseNoticeTitle());
            dto.setNoticeDate1(notices.get(0).getCreatedate());
        }
        if (notices.size() > 1) {
            dto.setNoticeNo2(notices.get(1).getCourseNoticeNo());
            dto.setNoticeTitle2(notices.get(1).getCourseNoticeTitle());
            dto.setNoticeDate2(notices.get(1).getCreatedate());
        }
        if (notices.size() > 2) {
            dto.setNoticeNo3(notices.get(2).getCourseNoticeNo());
            dto.setNoticeTitle3(notices.get(2).getCourseNoticeTitle());
            dto.setNoticeDate3(notices.get(2).getCreatedate());
        }

        // 3) 과제 요약 (미제출 or 최신)
        StudentAssignmentDTO ass = studentCourseMapper.selectAssignmentSummary(courseNo, studentUserNo);
        if (ass != null) {
            dto.setAssignmentNo(ass.getAssignmentNo());
            dto.setAssignmentTitle(ass.getAssignmentTitle());
            dto.setAssignmentDeadline(ass.getAssignmentDeadline());
            dto.setAssignmentSubmitted(ass.getSubmitted());
            dto.setAssignmentScore(ass.getAssignmentScore());
        }

        // 4) 출석 요약
        AttendanceSummaryDTO attend = studentCourseMapper.selectAttendanceSummary(courseNo, studentUserNo);
        if (attend != null) {
            dto.setAttendanceCount(attend.getAttendanceCount());
            dto.setAbsentCount(attend.getAbsentCount());
            dto.setLateCount(attend.getLateCount());
            dto.setAttendanceRate(attend.getAttendanceRate());
        }

        // 5) 성적 요약
        GradeSummaryDTO grade = studentCourseMapper.selectGradeSummary(courseNo, studentUserNo);
        if (grade != null) {
            dto.setGradeValue(grade.getGradeValue());
            dto.setGradeScore(grade.getGradeScore());
        }

        // 6) 최근 질문 3개
        List<StudentQuestionDTO> questions = getRecentQuestionList(courseNo, studentUserNo);

        if (questions.size() > 0) {
            dto.setQuestionNo1(questions.get(0).getQuestionNo());
            dto.setQuestionTitle1(questions.get(0).getQuestionTitle());
            dto.setQuestionDate1(questions.get(0).getCreatedate());
            dto.setQuestionAnswered1(questions.get(0).getAnswered());
        }
        if (questions.size() > 1) {
            dto.setQuestionNo2(questions.get(1).getQuestionNo());
            dto.setQuestionTitle2(questions.get(1).getQuestionTitle());
            dto.setQuestionDate2(questions.get(1).getCreatedate());
            dto.setQuestionAnswered2(questions.get(1).getAnswered());
        }
        if (questions.size() > 2) {
            dto.setQuestionNo3(questions.get(2).getQuestionNo());
            dto.setQuestionTitle3(questions.get(2).getQuestionTitle());
            dto.setQuestionDate3(questions.get(2).getCreatedate());
            dto.setQuestionAnswered3(questions.get(2).getAnswered());
        }

        return dto;
    }

    // ---------------------------------------------------------
    // 내 수강과목 목록
    // ---------------------------------------------------------
    public List<StudentCourseDTO> getMyCourseList(int studentUserNo) {
        return studentCourseMapper.selectMyCourseList(studentUserNo);
    }

    // ---------------------------------------------------------
    // 학생용 강의 상세보기
    // ---------------------------------------------------------
    public StudentCourseDetailDTO getStudentCourseDetail(int courseNo) {
        return studentCourseMapper.selectStudentCourseDetail(courseNo);
    }

    // ---------------------------------------------------------
    // 학생 시간표 조회
    // ---------------------------------------------------------
    public List<StudentTimetableDTO> getStudentTimetable(int studentUserNo) {
        return studentCourseMapper.selectStudentTimetable(studentUserNo);
    }

    // ---------------------------------------------------------
    // 수강신청 목록 조회 (필터 + 페이징)
    // ---------------------------------------------------------
    public List<StudentCourseDTO> getCourseListForStudentFiltered(
            int studentUserNo,
            Integer yoil,
            String professor,
            String deptCode,
            int startRow,
            int rowPerPage) {

        return studentCourseMapper.selectCourseListForStudentFiltered(
                studentUserNo, yoil, professor, deptCode, startRow, rowPerPage);
    }

    // ---------------------------------------------------------
    // 수강신청 목록 TOTAL COUNT (필터 적용)
    // ---------------------------------------------------------
    public int countFilteredCourseList(
            Integer yoil,
            String professor,
            String deptCode) {

        return studentCourseMapper.countCourseListFiltered(yoil, professor, deptCode);
    }

    // ---------------------------------------------------------
    // 학과 목록 조회 (수강신청 필터용)
    // ---------------------------------------------------------
    public List<DeptDTO> getDeptList() {
        return studentCourseMapper.selectDeptList();
    }
}
