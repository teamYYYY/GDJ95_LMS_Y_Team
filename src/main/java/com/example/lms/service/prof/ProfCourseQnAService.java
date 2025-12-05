package com.example.lms.service.prof;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.mapper.prof.ProfCourseQnAMapper;

@Service
@Transactional
public class ProfCourseQnAService {

	@Autowired
	ProfCourseQnAMapper profCourseQnAMapper;
	
	public List<Map<String, Object>> getQuestionList(int courseNo) {

		return profCourseQnAMapper.selectQuestionList(courseNo);
	}

	public Map<String, Object> getQuestionDetail(int courseQuestionNo) {
		
		return profCourseQnAMapper.selectQuestionDetail(courseQuestionNo);
	}

	public List<Map<String, Object>> getAnswerList(int courseQuestionNo) {

		return profCourseQnAMapper.selectAnswerList(courseQuestionNo);
	}

	public void addAnswer(int courseQuestionNo, int professorUserNo, String answerContent) {

		profCourseQnAMapper.insertAnswer(courseQuestionNo, professorUserNo, answerContent);
		profCourseQnAMapper.updateQuestionStatusAnswered(courseQuestionNo);
	}
	
	public void updateAnswer(int answerNo, String answerContent) {
		profCourseQnAMapper.updateAnswer(answerNo, answerContent);
	}

	public void deleteAnswer(int answerNo, int courseQuestionNo) {

		profCourseQnAMapper.deleteAnswer(answerNo);

        // 답변이 존재하는지 다시 체크
        int count = profCourseQnAMapper.answerCount(courseQuestionNo);
        if (count == 0) {
        	profCourseQnAMapper.updateQuestionStatusReopen(courseQuestionNo);
        }
	}


}
