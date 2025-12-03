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

    private final CourseQuestionMapper courseQuestionMapper;

    // 질문 목록
    public List<CourseQuestionDTO> getQuestionList(int courseNo, SysUserDTO loginUser) {

        List<CourseQuestionDTO> list = courseQuestionMapper.selectQuestionList(courseNo);

        boolean isProfessor = "PROFESSOR".equalsIgnoreCase(loginUser.getUserAuth());

        for (CourseQuestionDTO q : list) {
            boolean isOwner = q.getWriterUserNo() == loginUser.getUserNo();
            boolean canView = !q.isPrivate() || isOwner || isProfessor;
            q.setCanView(canView);
        }

        return list;
    }

    // 질문 + 댓글 상세
    public CourseQuestionDTO getQuestionDetail(int courseQuestionNo, SysUserDTO loginUser) {

    	CourseQuestionDTO question =
    	        courseQuestionMapper.selectQuestionDetail(courseQuestionNo);
        List<CourseQuestionAnswerDTO> answerList =
                courseQuestionMapper.selectAnswerList(courseQuestionNo);
        question.setAnswerList(answerList);

        boolean isProfessor = "PROFESSOR".equalsIgnoreCase(loginUser.getUserAuth());
        boolean isOwner = question.getWriterUserNo() == loginUser.getUserNo();
        boolean canView = !question.isPrivate() || isOwner || isProfessor;
        question.setCanView(canView);

        return question;
    }

    // 질문 등록
    public int insertQuestion(CourseQuestionDTO dto) {
        return courseQuestionMapper.insertQuestion(dto);
    }

    // 댓글 등록
    public int addAnswer(CourseQuestionAnswerDTO dto) {
        return courseQuestionMapper.insertAnswer(dto);
    }
}
