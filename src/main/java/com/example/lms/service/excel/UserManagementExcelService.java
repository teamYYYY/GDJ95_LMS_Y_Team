package com.example.lms.service.excel;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.SysAuthDTO;
import com.example.lms.dto.SysUserExcelDTO;
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
	public List<SysUserExcelDTO> searchUserInfoMapListForExcel(@Param("searchUserCondition") String searchUserCondition) {
		
		return userManagementExcelMapper.searchUserInfoMapListForExcel(searchUserCondition);
	}
	
	// ============================= 
	// 엑셀로 다수 사용자 등록 시 검증 필요함
	// =============================
	
	// 1. 권한코드 검증 필요
	private Integer sysAuthDetailCdValidateExcel(String userAuth) {
		
		return userManagementExcelMapper.sysAuthDetailCdValidateExcel(userAuth);
	}
	
	// 2. 학년코드 검증 필요
	private Integer sysUserGradeCdValidateExcel(Integer userGrade) {
		
		return userManagementExcelMapper.sysUserGradeCdValidateExcel(userGrade);
	}
	
	// 2_1. 학생인지 검증 필요
	private Integer sysUserGradeCdValidateExcel2(String userAuth) {
		
		return userManagementExcelMapper.sysUserGradeCdValidateExcel2(userAuth);
	}
	
	// 3. 학과,소속코드 검증 필요
	private Integer deptCdValidateExcel(String deptCode) {
		
		return userManagementExcelMapper.deptCdValidateExcel(deptCode);
	}
	
	// 4. 계정상태코드 검증 필요
	private Integer sysUserStatusCdValidateExcel(String userStatus) {
		
		return userManagementExcelMapper.sysUserStatusCdValidateExcel(userStatus);
	}
	
	// =============================
	// 검증 리스트 종료
	// =============================
	
	// 시스템사용자관리 - 사용자 등록
	public Integer insertSysUserExcel(SysUserExcelDTO sysUserExcelDTO) {
		
		return userManagementExcelMapper.insertSysUserExcel(sysUserExcelDTO);
	}
	
	/**
	 * 엑셀 업로드 시 필수 코드 값 연계 검증 및 데이터 무결성 검사
	 * @param sysUser DTO 객체
	 * @throws Exception 검증 실패 시 발생
	 */
	private void insertUserExcelUploadValidate(SysUserExcelDTO sysUser) throws Exception {
	    
	    // 1. 세부 권한 코드(FK) 유효성 검증
	    if (sysAuthDetailCdValidateExcel(sysUser.getUserAuth()) == 0) { 
	        throw new Exception("세부 권한 코드가 유효하지 않습니다: [" + sysUser.getUserAuth() + "]");
	    }
	    
	    // 2. 부서/소속 코드(FK) 유효성 검증
	    if (deptCdValidateExcel(sysUser.getDeptCode()) == 0) { 
	        throw new Exception("부서 코드가 유효하지 않습니다: [" + sysUser.getDeptCode() + "]");
	    }
	    
	    // 3. 계정 상태 코드(FK) 유효성 검증
	    if (sysUserStatusCdValidateExcel(sysUser.getUserStatus()) == 0) { 
	        throw new Exception("계정 상태 코드가 유효하지 않습니다: [" + sysUser.getUserStatus() + "]");
	    }
	    
	    // 4. 학년 코드 (선택적 필드) 유효성 및 학생 연계 검증
	    if (sysUser.getUserGrade() != null) {
	    	
	        if (sysUserGradeCdValidateExcel(sysUser.getUserGrade()) == 0) {
	        	
	            throw new Exception("학년 코드가 유효하지 않습니다: [" + sysUser.getUserGrade() + "]");
	        } else {
	        	
	        	if ( sysUserGradeCdValidateExcel2(sysUser.getUserAuth()) == 0 ) {
	        		
	        		throw new Exception("학년 코드가 입력되었으나, 해당 세부 권한은 학생 권한이 아닙니다: [" + sysUser.getUserAuth() + "]");
	        	}
	        }
	    }
	}
	
	// ----------------------------------------------------
    //  일괄 삽입 처리 (트랜잭션 분기 로직 적용) 
    // ----------------------------------------------------
    /**
     * DTO 리스트를 순회하며 DB에 개별 삽입을 시도합니다.
     * (DuplicateKeyException 발생 시 @Transactional에 의해 전체 롤백됩니다.)
     * * @param sysUserList 엑셀 파일에서 파싱된 DTO 리스트
     * @return 성공적으로 처리된 권한 수
     */
	@Transactional(rollbackFor = {Exception.class})
	public int batchInsertSysUser(List<SysUserExcelDTO> sysUserList) throws Exception {
	    
	    int successCount = 0;
	    
	    for (int i = 0; i < sysUserList.size(); i++) {
	        
	        SysUserExcelDTO sysUser = sysUserList.get(i);
	        int excelRowNumber = i + 2; // 엑셀에서 실제로 보이는 행 번호
	        
	        try {
	            // 1. 필수 코드 연계 검증 및 유효성 검사
	            insertUserExcelUploadValidate(sysUser);
    
	            // 3. 사용자 정보 삽입 처리
	            insertSysUserExcel(sysUser);

	            successCount++;
	            
	        } catch (DuplicateKeyException e) {
	            // ⭐️ 1. DuplicateKeyException 명시적 처리 ⭐️
	            // 로그에는 상세 정보를 남깁니다. (e.getMessage()에 쿼리 포함 가능)
	            log.error("엑셀 업로드 중 사용자 ID 중복 오류 발생 (행: {}): {}", excelRowNumber, e.getMessage(), e);
	            
	            // 사용자에게는 쿼리가 없는, 정제된 메시지를 던집니다.
	            throw new Exception("엑셀 " + excelRowNumber + "번째 행 처리 중 오류 발생: **사용자 ID [" + sysUser.getUserId() + "]**가 이미 존재하여 등록할 수 없습니다.", e);
	            
	        } catch (Exception e) {
	            
	            // 2. 기타 오류(코드 유효성, SQL 문법 오류 등) 처리: 
	            
	            String userFriendlyMessage;
	            
	            // 만약 SQL 관련 예외(BadSqlGrammarException 등)가 원인으로 있다면, 상세 메시지 대신 일반 메시지로 대체합니다.
	            if (e.getCause() != null && e.getCause().getClass().getName().contains("SQLException")) {
	                userFriendlyMessage = "**시스템 설정 오류** 또는 **DB 오류**가 발생했습니다. 관리자에게 문의하세요.";
	            } else {
	                // 개발자가 직접 던진 검증 오류(e.g., "세부 권한 코드가 유효하지 않습니다")는 메시지를 사용합니다.
	                userFriendlyMessage = e.getMessage();
	            }

	            log.error("엑셀 업로드 중 일반 오류 발생 (행: {}): {}", excelRowNumber, e.getMessage(), e);
	            
	            // 사용자 친화적인 메시지로 포장하여 던집니다.
	            throw new Exception("엑셀 " + excelRowNumber + "번째 행 처리 중 오류 발생: " + userFriendlyMessage, e);
	        }
	    }
	    
	    return successCount;
	}
	
	// ----------------------------------------------------
    // 엑셀 파싱 및 유틸리티 메서드는 그대로 유지
    // ----------------------------------------------------
    
    /**
     * 엑셀 파일을 파싱하여 List<SysUserExcelDTO>로 변환하는 유틸리티 메서드
     * @throws Exception 
     */
    public List<SysUserExcelDTO> parseExcelToSysUserExcelDTO(File file) throws Exception {
        List<SysUserExcelDTO> list = new ArrayList<>();

        // ⭐️ 파일 시스템에서 파일을 읽기 위한 InputStream을 생성 ⭐️
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {
                
            Sheet sheet = workbook.getSheetAt(0);
            
            

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                
            	Row row = sheet.getRow(i);
                
                // 1. 초기 안전 검사: 행 자체가 없으면 건너뛰기
                if (row == null) {
                 
                	continue;
                }
                
                SysUserExcelDTO userDTO = new SysUserExcelDTO();
                
                try {
                	
                	String strUserId = getCellValue(row.getCell(0));            // 사용자 ID
                    String strUserAuth = getCellValue(row.getCell(1));          // 사용자 권한 코드
                    String strUserStatus = getCellValue(row.getCell(2));        // 사용자계정상태
                    String strUserName = getCellValue(row.getCell(3));          // 사용자 이름
                    String strUserPassword = getCellValue(row.getCell(4));      // 사용자 비밀번호
                    String strUserEmail = getCellValue(row.getCell(5));         // 사용자 이메일
                    String strUserPhone = getCellValue(row.getCell(6));         // 사용자 전화번호
                    String strUserBirth = getCellValue(row.getCell(7));         // 사용자 생년월일
                    String strDeptCode = getCellValue(row.getCell(8));          // 부서 코드
                    String strUserGrade = getCellValue(row.getCell(9));         // 사용자 등급/직급
                    String strUserAddr = getCellValue(row.getCell(10));         // 사용자 주소
                    String strUserAddrDetail = getCellValue(row.getCell(11));   // 사용자 상세 주소
                    String strUserZipcode = getCellValue(row.getCell(12));      // 사용자 우편번호
                    
                    // 2. 필수 값 검증 및 String 필드 DTO에 설정 (앞서 정의된 헬퍼 메서드 사용)
                    // 검증 실패 시 Exception 발생
                    validateAndSetString(userDTO::setUserId, strUserId, "사용자 ID", i);
                    validateAndSetString(userDTO::setUserAuth, strUserAuth, "사용자 권한 코드", i);
                    validateAndSetString(userDTO::setUserStatus, strUserStatus, "사용자 상태", i);
                    validateAndSetString(userDTO::setUserName, strUserName, "사용자 이름", i);
                    validateAndSetString(userDTO::setUserPassword, strUserPassword, "사용자 비밀번호", i);
                    validateAndSetString(userDTO::setUserEmail, strUserEmail, "사용자 이메일", i);
                    validateAndSetString(userDTO::setUserPhone, strUserPhone, "사용자 전화번호", i);
                    validateAndSetString(userDTO::setDeptCode, strDeptCode, "부서 코드", i);
                    validateAndSetString(userDTO::setUserAddr, strUserAddr, "사용자 주소", i);
                    validateAndSetString(userDTO::setUserAddrDetail, strUserAddrDetail, "사용자 상세 주소", i);
                    validateAndSetString(userDTO::setUserZipcode, strUserZipcode, "사용자 우편번호", i);
                    
                    // 3-1. userBirth (String -> Date) - 필수 값
                    processUserBirth(userDTO, strUserBirth, i);
                    
                    // 3-2. userGrade (String -> Integer) - Nullable
                    processUserGrade(userDTO, strUserGrade, i);

                    // 4. 리스트에 DTO 추가
                    list.add(userDTO);
                	
                } catch (Exception e) {
                	
                	throw new Exception("엑셀 " + (i + 1) + "번째 행 처리 중 오류 발생: " + e.getMessage(), e);
                }
            }
        } // fis와 workbook은 자동으로 닫힘
        return list;
    }

    /**
     * 셀 값 추출 유틸리티
     */
    public String getCellValue(Cell cell) {

        if (cell == null) return "";

        // DataFormatter 인스턴스 생성 (Thread-safe 하지 않으므로, 유틸리티 클래스 외부에서 ThreadLocal로 관리하거나,
        // 이처럼 메서드 내에서 생성하는 것이 간단한 사용에는 안전합니다.)
        DataFormatter formatter = new DataFormatter(); 
        
        try {
            CellType cellType = cell.getCellType();
            
            // 수식 결과가 실제 값으로 처리되도록 함
            if (cellType == CellType.FORMULA) {
                cellType = cell.getCachedFormulaResultType();
            }

            switch (cellType) {
                case STRING:
                    return cell.getStringCellValue().trim();
                    
                case NUMERIC:
                    // ⭐️ 날짜 형식 체크 (YYYY-MM-DD 형식으로 명시적 포맷팅) ⭐️
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // DateUtil을 사용하여 Date 객체로 변환
                        Date date = cell.getDateCellValue();
                        // 원하는 형식("yyyy-MM-dd")으로 포맷팅
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
                        return sdf.format(date);
                    } else {
                        // 일반 숫자인 경우: DataFormatter를 사용하여 소수점 처리 없이 깔끔하게 추출
                        // DataFormatter가 셀의 포맷을 따르므로, #,##0 같은 포맷은 적용됩니다.
                        // 강제로 정수로 변환하려면 String.valueOf((long)cell.getNumericCellValue()).trim(); 사용
                        return formatter.formatCellValue(cell).trim();
                    }

                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue()).trim();
                    
                case BLANK:
                default:
                    return "";
            }
        } catch (Exception e) {
            // 셀 타입 처리 중 오류가 발생하면, DataFormatter를 사용하여 문자열로 강제 변환 시도
            // Deprecated된 setCellType 대신 DataFormatter를 사용하는 방식
            return formatter.formatCellValue(cell).trim();
        }
    }
    
    
    /**
     * 필수(Not Null) 필드가 비어있는지 검증합니다.
     * @param value 검증할 문자열 값
     * @param fieldName 오류 메시지에 표시할 필드 이름
     * @throws CustomValidationException 값이 비어있을 경우 발생
     */
    private void validateAndSetString(java.util.function.Consumer<String> setter, String value, String fieldName, int rowIndex) throws Exception {
        if (value == null || value.trim().isEmpty()) {
        	
            throw new Exception("[" + fieldName + "]은/는 필수 입력 항목입니다. 값을 확인해 주세요.");
        }
        setter.accept(value.trim());
    }
    

    /**
     * userBirth 필드의 String 값을 Date로 변환하고 DTO에 설정합니다. (필수)
     */
    private void processUserBirth(SysUserExcelDTO userDTO, String strUserBirth, int rowIndex) throws Exception {
        if (strUserBirth == null || strUserBirth.trim().isEmpty()) {
             // 이미 validateAndSetString에서 검증되었으나, Date 변환 전 재검증 (안전성 확보)
            throw new Exception("[사용자 생년월일]은/는 필수 입력 항목입니다.");
        }
        try {
        	
            // 엑셀에서 'YYYY-MM-DD' 형태로 입력된 것을 가정합니다.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(strUserBirth.trim(), formatter);
            Date userBirth = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            userDTO.setUserBirth(userBirth);
        } catch (DateTimeParseException e) {
        	
            throw new Exception("사용자 생년월일의 형식이 올바르지 않습니다. (YYYY-MM-DD)", e);
        }
    }

    /**
     * userGrade 필드의 String 값을 Integer로 변환하고 DTO에 설정합니다. (선택적)
     */
    private void processUserGrade(SysUserExcelDTO userDTO, String strUserGrade, int rowIndex) throws Exception {
        
    	if (strUserGrade != null && !strUserGrade.trim().isEmpty()) {
            
    		try {
    			
                Integer userGrade = Integer.parseInt(strUserGrade.trim());
                userDTO.setUserGrade(userGrade);
            } catch (NumberFormatException e) {
            	
                throw new Exception("사용자 등급(학년)의 형식이 올바르지 않습니다. (숫자만 가능)", e);
            }
        } else {
            // 값이 없으면 DTO 필드를 null로 설정
            userDTO.setUserGrade(null); 
        }
    }

}
