package com.example.lms.service.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.UniversityNoticeDTO;
import com.example.lms.mapper.admin.UniversityNoticeMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 2025. 11. 28.
 * Autor - JM
 * 시스템 공지사항 서비스
 */
@Slf4j
@Service
@Transactional
public class UniversityNoticeService {
	
	@Autowired
	private UniversityNoticeMapper universityNoticeMapper;
	
	// 학교 공지사항 전체 리스트 조회
	public List<UniversityNoticeDTO> universityNoticeList(Integer startRow, Integer limit) {
		
		List<UniversityNoticeDTO> universityNoticeList = universityNoticeMapper.universityNoticeList(startRow, limit);
		
		return universityNoticeList;
	};	
	
	// 학교 공지사항 전체 리스트 페이징
	public Integer universityNoticeListCnt() {
		
		return universityNoticeMapper.universityNoticeListCnt();
	}
	
	// 학교 공지사항 검색 조회
	public List<UniversityNoticeDTO> searchUniversityNoticeInfoList(Map<String, Object> searchParams) {
		
		List<UniversityNoticeDTO> universityNoticeList = universityNoticeMapper.searchUniversityNoticeInfoList(searchParams);
		
		//assignPriorityColorClass(universityNoticeList);
		
		return universityNoticeList;
	}
	
	// 학교 공지사항 검색 조회 카운트
	public Integer searchUniversityNoticeInfoListCnt(String searchUniversityNoticeCondition) {
		
		return universityNoticeMapper.searchUniversityNoticeInfoListCnt(searchUniversityNoticeCondition);
	};
	
	// 학교 공지사항 상세정보 조회
	public UniversityNoticeDTO selectUniversityNoticeDetail(Integer universityNoticeNo) {
		
		return universityNoticeMapper.selectUniversityNoticeDetail(universityNoticeNo);
	}
	
	// 학교 공지사항 수정 및 삭제 기능 사용시 검증기능 (직접 작성한 사용자인가?)
	// 세션에서 userNo, userId를 가져와서 비교해야함 
	public Integer updateRemoveUniversityNoticeValidate(Map<String, Object> userValidateParams) {
		
		return universityNoticeMapper.updateRemoveUniversityNoticeValidate(userValidateParams);
	}
	
	// 학교 공지사항 수정 및 삭제 기능 사용시 검증기능 (접속한 사람이 혹시 관리자인가?)
	// 세션에서 userNo, userId를 가져와서 비교해야함 
	public Integer insertUpdateRemoveUniversityNoticeValidate(Map<String, Object> userValidateParams) {
		
		return universityNoticeMapper.insertUpdateRemoveUniversityNoticeValidate(userValidateParams);
	}
	
	// 학교 공지사항 입력 시 입력정보 우선순위 셀렉박스리스트
	public List<UniversityNoticeDTO> selectUniversityNoticePriorityList() {
		
		return universityNoticeMapper.selectUniversityNoticePriorityList();
	}

	// 학교 공지사항 등록, 수정, 삭제 전에 한번 검증 하기
	public Integer insertUpdRemvUniversityNoticeNoValidate(Integer universityNoticeNo) {

		return universityNoticeMapper.insertUpdRemvUniversityNoticeNoValidate(universityNoticeNo);
	}

	//======================================================
	// 학교 공지사항 등록 기능
	//======================================================
	public Integer insertUniversityNotice(UniversityNoticeDTO universityNoticeDTO) {
		
		return universityNoticeMapper.insertUniversityNotice(universityNoticeDTO);
	}
	
	//======================================================
	// 학교 공지사항 수정 기능
	//======================================================
	
	public Integer updateUniversityNotice(UniversityNoticeDTO universityNoticeDTO) {
		
		return universityNoticeMapper.updateUniversityNotice(universityNoticeDTO);
	}
	
	//======================================================
	// 학교 공지사항 삭제 기능
	//======================================================
	
	public Integer removeUniversityNotice(Integer universityNoticeNo) {
		
		return universityNoticeMapper.removeUniversityNotice(universityNoticeNo);
	}
	
	// 페이지 최초 로딩 시 호출되는 메서드 (또는 Controller에서 직접 호출할 메서드)
	public void assignPriorityColorClassForMustache(List<UniversityNoticeDTO> noticeList) {
	    if (noticeList == null) return;

	    for (UniversityNoticeDTO notice : noticeList) {
	        String colorClass = "bg-gray-100 text-gray-800 px-2 py-0.5 rounded-full"; // 기본값

	        int priorityCode = notice.getUniversityNoticePriorityCode();

	        if (priorityCode == 10) {
	            colorClass = "bg-red-100 text-red-700 px-2 py-0.5 rounded-full"; 
	        } else if (priorityCode == 20) {
	            colorClass = "bg-yellow-100 text-orange-700 px-2 py-0.5 rounded-full"; 
	        }
	        
	        // ⭐️ 이 Setter 호출이 누락되면 에러가 발생합니다. ⭐️
	        notice.setPriorityColorClass(colorClass); 
	    }
	}
}
