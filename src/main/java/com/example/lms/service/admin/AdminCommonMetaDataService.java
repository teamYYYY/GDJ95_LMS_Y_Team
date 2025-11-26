package com.example.lms.service.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.DeptDTO;
import com.example.lms.dto.SysAuthDTO;
import com.example.lms.dto.SysUserGradeDTO;
import com.example.lms.dto.SysUserStatusDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 2025. 11. 26.
 * Autor - JM
 * 관리자 공용 메타 데이터 조회 서비스 (Facade)
 * 관리자 페이지에서 공통적으로 필요한 리스트(학과, 권한 등) 조회 기능을 통합 제공합니다.
 */
@Slf4j
@Service
@Transactional(readOnly = true) // 데이터 변경 없이 조회만 하므로 readOnly=true 설정
public class AdminCommonMetaDataService {

    // 필요한 4가지 하위 서비스를 final 필드로 선언
    private final DeptService deptService;
    private final SysAuthService sysAuthService;
    private final SysUserGradeService sysUserGradeService;
    private final SysUserStatusService sysUserStatusService;

    // 💡 생성자 주입 (Autowired 대신 생성자를 통해 명확하게 의존성을 주입)
    public AdminCommonMetaDataService(
    		
            DeptService deptService,
            SysAuthService sysAuthService,
            SysUserGradeService sysUserGradeService,
            SysUserStatusService sysUserStatusService) {
        this.deptService = deptService;
        this.sysAuthService = sysAuthService;
        this.sysUserGradeService = sysUserGradeService;
        this.sysUserStatusService = sysUserStatusService;
    }

    /**
     * @return 모든 학과 정보 리스트를 반환합니다.
     */
    public List<DeptDTO> getDeptList() {
        return deptService.deptList();
    }
    
    /**
     * @return 모든 시스템 권한 정보 리스트를 반환합니다.
     */
    public List<SysAuthDTO> getSysAuthList() {
    	
        return sysAuthService.sysAuthList(); 
    }
    
    /**
     * @return 모든 사용자 학년 정보 리스트를 반환합니다.
     */
    public List<SysUserGradeDTO> getSysUserGradeList() {
        return sysUserGradeService.sysUserGradeList();
    }
    
    /**
     * @return 모든 사용자 상태 정보 리스트를 반환합니다.
     */
    public List<SysUserStatusDTO> getSysUserStatusList() {
        return sysUserStatusService.sysUserStatusList();
    }
    
    /**
     * 💡 관리자 컨트롤러에서 한번의 호출로 필요한 모든 메타 데이터를 Map으로 묶어 반환합니다.
     * @return 모든 메타 데이터 목록(학과, 권한, 학년, 상태)이 담긴 Map
     */
    public Map<String, Object> getAllSystemMetadata() {
        
    	Map<String, Object> metadata = new HashMap<String, Object>();
        metadata.put("deptList", getDeptList());
        metadata.put("authList", getSysAuthList());
        metadata.put("gradeList", getSysUserGradeList());
        metadata.put("statusList", getSysUserStatusList());
        return metadata;
    }
}
