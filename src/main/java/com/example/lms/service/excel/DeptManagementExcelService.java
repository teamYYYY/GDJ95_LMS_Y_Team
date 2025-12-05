package com.example.lms.service.excel;

import com.example.lms.dto.DeptDTO;
import com.example.lms.mapper.excel.DeptManagementExcelMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 2025. 12. 01.
 * Autor - JM
 * 학과관리 - 엑셀 일괄 업로드 기능
 */
@Slf4j
@Service
@Transactional
public class DeptManagementExcelService {

	@Autowired
    private DeptManagementExcelMapper deptManagementExcelMapper;

    @Value("${excel.file.upload-dir}")
    private String uploadDir;

    //  학과 관리 - 검색 조회 리스트️ 가져오기
    public List<DeptDTO> searchDeptInfoListForExcel(@Param("searchDeptCondition") String searchDeptCondition) {

        return deptManagementExcelMapper.searchDeptInfoListForExcel(searchDeptCondition);
    }

    // ⭐️ 기존의 insertDeptExcel 메서드는 새로운 배치 메서드에 통합되므로 제거합니다. ⭐️
    /*
    public Integer insertDeptExcel(DeptDTO deptDTO) {
        return deptManagementExcelMapper.insertDeptExcel(deptDTO);
    }
    */

    // ----------------------------------------------------
    // ⭐️ [변경 사항]: 일괄 삽입 처리 (트랜잭션 적용) ⭐️
    // ----------------------------------------------------
    /**
     * DTO 리스트를 순회하며 DB에 개별 삽입을 시도합니다.
     * DuplicateKeyException 발생 시 @Transactional에 의해 전체 롤백됩니다.
     * * @param deptList 엑셀 파일에서 파싱된 학과 DTO 리스트
     * @return 성공적으로 등록된 학과 수
     */
    @Transactional(rollbackFor = {Exception.class}) // Exception 발생 시 롤백 (DuplicateKeyException 포함)
    public int batchInsertDept(List<DeptDTO> deptList) {
        int successCount = 0;
        
        for (DeptDTO dept : deptList) {
            
            // ⭐️ DuplicateKeyException이 발생하면 catch되지 않고 상위 호출자(Controller)로 던져지며,
            //    @Transactional 덕분에 이 메서드 내의 모든 작업이 롤백됩니다.
            
            // 매퍼를 호출하여 개별 삽입 시도
            deptManagementExcelMapper.insertDeptExcel(dept); 
            successCount++;
        }
        return successCount;
    }


    // ----------------------------------------------------
    // 엑셀 파싱 및 유틸리티 메서드는 그대로 유지
    // ----------------------------------------------------
    
    /**
     * 엑셀 파일을 파싱하여 List<DeptDTO>로 변환하는 유틸리티 메서드
     */
    public List<DeptDTO> parseExcelToDeptDTO(File file) throws IOException {
        List<DeptDTO> list = new ArrayList<>();

        // ⭐️ 파일 시스템에서 파일을 읽기 위한 InputStream을 생성 ⭐️
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
                
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) continue;

                // 엑셀 양식: 0:학과코드, 1:학과명
                String deptCode = getCellValue(row.getCell(0));
                String deptName = getCellValue(row.getCell(1));

                if (deptCode.isEmpty() || deptName.isEmpty()) continue; 

                DeptDTO dept = new DeptDTO();
                dept.setDeptCode(deptCode);
                dept.setDeptName(deptName);

                list.add(dept);
            }
        } // fis와 workbook은 자동으로 닫힘
        return list;
    }

    /**
     * 셀 값 추출 유틸리티
     */
    public String getCellValue(Cell cell) {

        if (cell == null) return "";

        switch (cell.getCellType()) {

            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((int)cell.getNumericCellValue()).trim();
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue()).trim();
            case FORMULA: return cell.getCellFormula();
            default: return "";
        }
    }
}
