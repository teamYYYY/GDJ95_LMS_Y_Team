package com.example.lms.mapper.courseQuestion;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.lms.dto.CourseQuestionAnswerDTO;
import com.example.lms.dto.CourseQuestionDTO;

@Mapper
public interface CourseQuestionMapper {
	// 질문 목록 조회
	List<CourseQuestionDTO> selectQuestionList(int courseNo);

	// 질문 1건 조회
	CourseQuestionDTO selectQuestionDetail(int courseQuestionNo);
	
	// 질문에 달린 댓글 리스트
	List<CourseQuestionAnswerDTO> selectAnswerList(int courseQuestionNo);
	
	// 질문 등록
	int insertQuestion(CourseQuestionDTO dto);
		
	// 댓글 등록
	int insertAnswer(CourseQuestionAnswerDTO dto);	
}
