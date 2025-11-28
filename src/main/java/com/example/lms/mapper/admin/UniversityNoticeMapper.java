package com.example.lms.mapper.admin;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.lms.dto.UniversityNoticeDTO;


/**
 * 
 * 2025. 11. 28.
 * Author - jm
 * 학교 공지사항 Mapper
 */
@Mapper
public interface UniversityNoticeMapper {

	// 학교 공지사항 전체 리스트 조회
	List<UniversityNoticeDTO> universityNoticeList(@Param("startRow") Integer startRow, @Param("limit") Integer limit);	
	
	// 학교 공지사항 전체 리스트 페이징
	Integer universityNoticeListCnt();
	
	// 학교 공지사항 검색 조회
	List<UniversityNoticeDTO> searchUniversityNoticeInfoList(Map<String, Object> searchParams);
	
	// 학교 공지사항 검색 조회 카운트
	Integer searchUniversityNoticeInfoListCnt(@Param("searchUniversityNoticeCondition") String searchUniversityNoticeCondition);
	
	// 학교 공지사항 상세정보 조회
	UniversityNoticeDTO selectUniversityNoticeDetail(Integer universityNoticeNo);
	
	// 학교 공지사항 수정 및 삭제 기능 사용시 검증기능 (직접 작성한 사용자인가?)
	// 세션에서 userNo, userId를 가져와서 비교해야함 
	Integer updateRemoveUniversityNoticeValidate(Map<String, Object> userValidateParams);
	
	// 학교 공지사항 수정 및 삭제 기능 사용시 검증기능 (접속한 사람이 혹시 관리자인가?)
	// 세션에서 userNo, userId를 가져와서 비교해야함 
	Integer updateRemoveUniversityNoticeValidate2(Map<String, Object> userValidateParams);
	
	// 학교 공지사항 입력 시 입력정보 우선순위 셀렉박스리스트
	List<UniversityNoticeDTO> selectUniversityNoticePriorityList();
	
	//======================================================
	// 학교 공지사항 등록 기능
	//======================================================
	Integer insertUniversityNotice(UniversityNoticeDTO universityNoticeDTO);
	
	//======================================================
	// 학교 공지사항 수정 기능
	//======================================================
	
	Integer updateUniversityNotice(UniversityNoticeDTO universityNoticeDTO);
	
	//======================================================
	// 학교 공지사항 삭제 기능
	//======================================================
	
	Integer removeUniversityNotice(Integer universityNoticeNo);
}
