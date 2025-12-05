package com.example.lms.service.courseQuestion;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.CourseQuestionAnswerDTO;
import com.example.lms.dto.CourseQuestionDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.mapper.courseQuestion.CourseQuestionMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseQuestionService {

    private final CourseQuestionMapper mapper;

    // 전체 문의 개수 조회 (페이징용)
    public int getTotalQuestionCount(int courseNo) {
        return mapper.countQuestion(courseNo);
    }

 // 페이징 목록 + 번호 매기기 + 비밀글 권한 적용
    public List<CourseQuestionDTO> getPagedQuestionList(
            int courseNo, SysUserDTO loginUser, int currentPage, int rowPerPage) {

        int startRow = (currentPage - 1) * rowPerPage;

        List<CourseQuestionDTO> list = mapper.selectPagedQuestionList(courseNo, startRow, rowPerPage);

        boolean isProfessor = "PROFESSOR".equalsIgnoreCase(loginUser.getUserAuth());

        // 최신글이 1번이 되도록 번호 부여 (페이지 기준)
        int displayNumber = startRow + 1;

        for (CourseQuestionDTO q : list) {

            // 번호 부여
            q.setIndex(displayNumber++);

            // 권한 체크
            boolean isOwner = q.getWriterUserNo() == loginUser.getUserNo();
            boolean canView = !q.isPrivatePost() || isOwner || isProfessor;
            q.setCanView(canView);

            if (!canView) {
                q.setCourseQuestionTitle("비밀글입니다.");
                q.setWriterName("비공개");
                q.setCourseQuestionContent(null);
            }
        }

        return list;
    }

    // 문의 상세 조회 + 댓글 + 비밀글 체크
    public CourseQuestionDTO getQuestionDetail(int courseQuestionNo, SysUserDTO loginUser) {

        CourseQuestionDTO question = mapper.selectQuestionDetail(courseQuestionNo);
        List<CourseQuestionAnswerDTO> answerList = mapper.selectAnswerList(courseQuestionNo);
        question.setAnswerList(answerList);

        boolean isProfessor = "PROFESSOR".equalsIgnoreCase(loginUser.getUserAuth());
        boolean isOwner = question.getWriterUserNo() == loginUser.getUserNo();
        boolean canView = !question.isPrivatePost() || isOwner || isProfessor;
        question.setCanView(canView);

        if (!canView) {
            question.setCourseQuestionTitle("비밀글입니다.");
            question.setWriterName("비공개");
            question.setCourseQuestionContent(null);
        }

        return question;
    }

    // 문의글 작성
    public int insertQuestion(CourseQuestionDTO dto) {
        return mapper.insertQuestion(dto);
    }

    // 문의글 수정 (본인만 가능)
    public boolean updateQuestion(CourseQuestionDTO dto, SysUserDTO loginUser) {

        boolean isOwner = mapper.isOwner(dto.getCourseQuestionNo(), loginUser.getUserNo());
        if (!isOwner) return false;

        return mapper.updateQuestion(dto) == 1;
    }

    // 문의글 삭제 (본인만 가능)
    public boolean deleteQuestion(int courseQuestionNo, SysUserDTO loginUser) {

        boolean isOwner = mapper.isOwner(courseQuestionNo, loginUser.getUserNo());
        if (!isOwner) return false;

        return mapper.deleteQuestion(courseQuestionNo) == 1;
    }

    // 댓글 등록 (교수 전용)
    public int addAnswer(CourseQuestionAnswerDTO dto) {
        return mapper.insertAnswer(dto);
    }

    // 댓글 수정 (작성자 = 교수 본인)
    public boolean updateAnswer(CourseQuestionAnswerDTO dto, SysUserDTO loginUser) {

        boolean isOwner = mapper.isAnswerOwner(dto.getAnswerNo(), loginUser.getUserNo());
        if (!isOwner) return false;

        return mapper.updateAnswer(dto) == 1;
    }
    
    public int getCourseNoByQuestion(int courseQuestionNo) {
        return mapper.selectCourseNoByQuestion(courseQuestionNo);
    }

}
