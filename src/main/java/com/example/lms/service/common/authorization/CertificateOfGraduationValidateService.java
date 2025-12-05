package com.example.lms.service.common.authorization;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.lms.dto.MainDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 2025. 12. 05.
 * Autor - JM
 * 졸업증명서 발급 검증관련 서비스
 */
@Slf4j
@Service
@Transactional
public class CertificateOfGraduationValidateService {

    public boolean certificateOfGraduationValidate(MainDTO mainDTO) {

        boolean certificateValidate = false;
        String authCode = mainDTO.getAuthCode();
        Integer userGrade = mainDTO.getUserGrade();
		String authDetailName = mainDTO.getAuthDetailName();
		
		log.info("authCodeauthCodeauthCode" + authCode);
		log.info("userGradeuserGradeuserGradeuserGradeuserGrade" + userGrade);
		log.info("authDetailNameauthDetailNameauthDetailNameauthDetailName" + authDetailName);
		
		
        // 학생이고
        if (authCode.equals("S001")) {

            // 4학년이면
            if ( String.valueOf(userGrade).equals("4") ) {

                if (authDetailName.equals("졸업생")) {
					
					certificateValidate = true;
				}
            }
        }

        return certificateValidate;
    }
}
