package com.example.lms.service.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.SysAuthDTO;
import com.example.lms.mapper.excel.SysAuthManagementExcelMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
public class SysAuthManagementExcelService {
	
	@Autowired 
	private SysAuthManagementExcelMapper sysAuthManagementExcelMapper;
	
	// 사용자 권한 코드 검색 조회
	public List<SysAuthDTO> searchSysAuthInfoListForExcel(String searchSysAuthCondition) {

		return sysAuthManagementExcelMapper.searchSysAuthInfoListForExcel(searchSysAuthCondition);
	}
	
	// 사용자 권한 등록전에 권한코드가 이미 있을 경우에는 Detail만 인서트하면 된다.
	private Integer insertSysAuthChkExcel(SysAuthDTO sysAuthDTO) {
		
		return sysAuthManagementExcelMapper.insertSysAuthChkExcel(sysAuthDTO);
	}
	
	// 사용자 세부권한 등록 시 동일한 세부권한코드나 세부권한명이 존재하면 안된다.
	private Integer insertSysAuthDetailChkExcel(SysAuthDTO sysAuthDTO) {
		
		return sysAuthManagementExcelMapper.insertSysAuthDetailChkExcel(sysAuthDTO);
	}
	
	//======================================================
	// 사용자 권한 등록 기능 insert 순서 sysAuth -> sysAuthDetail
	//======================================================
	public Integer insertSysAuthExcel(SysAuthDTO sysAuthDTO) throws Exception {
		
		return sysAuthManagementExcelMapper.insertSysAuthExcel(sysAuthDTO);
	}

	public Integer insertSysAuthDetailExcel(SysAuthDTO sysAuthDTO) throws Exception {
		
		return sysAuthManagementExcelMapper.insertSysAuthDetailExcel(sysAuthDTO);
	}
	
	
	// ----------------------------------------------------
    // 3. 일괄 삽입 처리 (트랜잭션 분기 로직 적용) ⭐️ 핵심 수정 ⭐️
    // ----------------------------------------------------
    /**
     * DTO 리스트를 순회하며 DB에 개별 삽입을 시도합니다.
     * 1. 권한코드(Auth) 존재 시 -> Detail만 삽입 시도
     * 2. 권한코드(Auth) 미 존재 시 -> Auth, Detail 모두 삽입 시도
     * (DuplicateKeyException 발생 시 @Transactional에 의해 전체 롤백됩니다.)
     * * @param sysAuthList 엑셀 파일에서 파싱된 DTO 리스트
     * @return 성공적으로 처리된 권한 수
     */
    @Transactional(rollbackFor = {Exception.class})
    public int batchInsertSysAuth(List<SysAuthDTO> sysAuthList) throws Exception {
        int successCount = 0;
        
        for (SysAuthDTO sysAuth : sysAuthList) {
            
            // 1. 권한 코드 (SysAuth) 중복 체크
            int insertSysAuthExistChkExcel = insertSysAuthChkExcel(sysAuth); 
            
            // 2. 세부 권한 코드 (SysAuthDetail) 중복 체크
            // (권한 코드와 관계없이 세부 권한 코드가 테이블 전체에서 중복되는지 체크)
            int insertSysAuthDetailExistChkExcel = insertSysAuthDetailChkExcel(sysAuth);

            if (insertSysAuthDetailExistChkExcel > 0) {
                
            	// 세부 권한 코드가 이미 DB에 존재하면 전체 롤백
                throw new DuplicateKeyException(
                    "중복된 세부 권한 코드 또는 세부 권한명이 발견되었습니다: " + sysAuth.getAuthDetailCode() + ", " + sysAuth.getAuthDetailName()  + ". 업로드를 중단하고 전체 롤백됩니다.");
            }
            
            // 3. 권한 테이블 삽입 처리
            if (insertSysAuthExistChkExcel == 0) {
            	
                // 권한 코드가 DB에 존재하지 않으면 SysAuth 등록
            	try {
            		
                    insertSysAuthExcel(sysAuth);
            	} catch (DuplicateKeyException e) {
            		
                    // (극히 드물게 경쟁 상태로 인해 여기서 발생해도 롤백 유도)
            		log.warn("경쟁 상태로 인한 권한 코드 중복 발생: {}", sysAuth.getAuthCode());
            	}
            } 
            // else: 권한 코드가 이미 존재하면 SysAuth 삽입은 건너뜁니다.
            
            // 4. 세부 권한 테이블 삽입 (Detail 중복 체크는 위에서 이미 처리했으므로 바로 삽입)
            insertSysAuthDetailExcel(sysAuth);

            successCount++;
        }
        return successCount;
    }

    // ----------------------------------------------------
    // 엑셀 파싱 및 유틸리티 메서드는 그대로 유지
    // ----------------------------------------------------
    
    /**
     * 엑셀 파일을 파싱하여 List<SysAuthDTO>로 변환하는 유틸리티 메서드
     */
    public List<SysAuthDTO> parseExcelToSysAuthDTO(File file) throws IOException {
        List<SysAuthDTO> list = new ArrayList<>();

        // ⭐️ 파일 시스템에서 파일을 읽기 위한 InputStream을 생성 ⭐️
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
                
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) continue;

                // 엑셀 양식: 0:세부권한코드, 1:세부권한명, 2:권한코드, 3:권한코드명
                String authDetailCode = getCellValue(row.getCell(0));
                String authDetailName = getCellValue(row.getCell(1));
                String authCode = getCellValue(row.getCell(2));
                String authName = getCellValue(row.getCell(3));

                if (authDetailCode.isEmpty() || authDetailName.isEmpty() || 
                		authCode.isEmpty() || authName.isEmpty() ) continue; 

                SysAuthDTO sysAuth = new SysAuthDTO();
                sysAuth.setAuthDetailCode(authDetailCode);
                sysAuth.setAuthDetailName(authDetailName);
                sysAuth.setAuthCode(authCode);
                sysAuth.setAuthName(authName);
                
                list.add(sysAuth);
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
