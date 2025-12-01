package com.example.lms.mapper.excel;

import com.example.lms.dto.DeptDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * 2025. 12. 01.
 * Author - jm
 * 학과관리 엑셀 Mapper
 */
@Mapper
public interface DeptManagementExcelMapper {

    //  학과 관리 - 검색 조회 리스트️ 가져오기
    List<DeptDTO> searchDeptInfoListForExcel(@Param("searchDeptCondition") String searchDeptCondition);

    // 학과 관리 - 학과 코드 등록
    Integer insertDeptExcel(DeptDTO deptDTO);
}
