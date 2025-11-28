package com.example.lms.service.profCourseNotice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.CourseNoticeDTO;
import com.example.lms.mapper.profCourseNotice.CourseNoticeMapper;

@Service
@Transactional
public class CourseNoticeService {

	@Autowired
	CourseNoticeMapper courseNoticeMapper;
	
	// 공지사항 리스트
	public List<CourseNoticeDTO> getCourseNoticeListByPage(int courseNo, int startRow, int rowPerPage) {
		
		return courseNoticeMapper.selectCourseNoticeListByPage(courseNo, startRow, rowPerPage);
	}
	
	public int getCourseNoticeCount(int courseNo) {
	   
		return courseNoticeMapper.selectCourseNoticeCount(courseNo);
	}
	
	// 상세보기 + 조회수
	public CourseNoticeDTO getCourseNoticeDetail(int courseNoticeNo) {
		
		courseNoticeMapper.updateCourseNoticeViewCount(courseNoticeNo); // 조회수
		
		return courseNoticeMapper.selectCourseNoticeDetail(courseNoticeNo);
	}
	
	// 등록
	public int addCourseNotice(CourseNoticeDTO cn) {
		
		return courseNoticeMapper.insertCourseNotice(cn);
	}
	
	// 수정
	public int modifyCourseNotice(CourseNoticeDTO cn) {
		 
		return courseNoticeMapper.updateCourseNotice(cn);
	}
	
	// 삭제
	public int removeCourseNotice(int courseNoticeNo) {
		
		return courseNoticeMapper.deleteCourseNotice(courseNoticeNo);
	}
}
