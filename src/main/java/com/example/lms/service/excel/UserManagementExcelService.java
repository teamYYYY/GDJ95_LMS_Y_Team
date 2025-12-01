package com.example.lms.service.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.SysAuthDTO;
import com.example.lms.dto.SysUserDTO;
import com.example.lms.mapper.excel.UserManagementExcelMapper;

import lombok.extern.slf4j.Slf4j;

/**
*
* 2025. 12. 01.
* Autor - JM
* 사용자관리 - 엑셀 기능
*/
@Slf4j
@Transactional
@Service
public class UserManagementExcelService {
	
	@Autowired
	private UserManagementExcelMapper userManagementExcelMapper;
	
	// 시스템사용자 관리 - 시스템 사용자 검색 조회
	public List<Map<String, Object>> searchUserInfoMapListForExcel(@Param("searchUserCondition") String searchUserCondition) {
		
		return userManagementExcelMapper.searchUserInfoMapListForExcel(searchUserCondition);
	}
	
	// ============================= 
	// 엑셀로 다수 사용자 등록 시 검증 필요함
	// =============================
	
	// 1. 권한코드 검증 필요
	public Integer sysAuthDetailCdValidateExcel(String authDetailCode) {
		
		return userManagementExcelMapper.sysAuthDetailCdValidateExcel(authDetailCode);
	}
	
	// 2. 학년코드 검증 필요
	public Integer sysUserGradeCdValidateExcel(String gradeCode) {
		
		return userManagementExcelMapper.sysUserGradeCdValidateExcel(gradeCode);
	}
	
	// 2_1. 학생인지 검증 필요
	public Integer sysUserGradeCdValidateExcel2(String authCode) {
		
		return userManagementExcelMapper.sysUserGradeCdValidateExcel2(authCode);
	}
	
	// 3. 학과,소속코드 검증 필요
	public Integer deptCdValidateExcel(String deptCode) {
		
		return userManagementExcelMapper.deptCdValidateExcel(deptCode);
	}
	
	// 4. 계정상태코드 검증 필요
	public Integer sysUserStatusCdValidateExcel(String statusCode) {
		
		return userManagementExcelMapper.sysUserStatusCdValidateExcel(statusCode);
	}
	
	// =============================
	// 검증 리스트 종료
	// =============================
	
	// 시스템사용자관리 - 사용자 등록
	public Integer insertUserInfoForExcel(SysUserDTO sysUserDTO) {
		
		return userManagementExcelMapper.insertUserInfoForExcel(sysUserDTO);
	}
	
	// ----------------------------------------------------
    // 엑셀 파싱 및 유틸리티 메서드는 그대로 유지
    // ----------------------------------------------------
    
    /**
     * 엑셀 파일을 파싱하여 List<SysUserDTO>로 변환하는 유틸리티 메서드
     */
    public List<SysUserDTO> parseExcelToSysAuthDTO(File file) throws IOException {
        List<SysUserDTO> list = new ArrayList<>();

        // ⭐️ 파일 시스템에서 파일을 읽기 위한 InputStream을 생성 ⭐️
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
                
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) continue;

                /*
                 * 원본 컬럼명 (Snake Case)	별칭 (Camel Case)	설명
					user_no	userNo	사용자 번호 (PK)
					user_id	userId	사용자 ID
					user_auth	userAuth	사용자 권한 코드
					user_status	userStatus	사용자 상태 (활성/비활성 등)
					user_name	userName	사용자 이름
					user_password	userPassword	사용자 비밀번호
					user_email	userEmail	사용자 이메일
					user_phone	userPhone	사용자 전화번호
					user_birth	userBirth	사용자 생년월일
					dept_code	deptCode	부서 코드
					user_grade	userGrade	사용자 등급/직급
					user_addr	userAddr	사용자 주소
					user_addr_detail	userAddrDetail	사용자 상세 주소
					user_zipcode	userZipcode	사용자 우편번호
					user_last_login	userLastLogin	사용자 최종 로그인 시각
					user_login_fail_cnt	userLoginFailCnt	사용자 로그인 실패 횟수
					user_createdate	userCreatedate	사용자 생성일
					useruser_updatedate	userUserUpdatedate	사용자 정보 최종 수정일
                 * 
                 */
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
                
                //list.add(SysUserDTO);
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
