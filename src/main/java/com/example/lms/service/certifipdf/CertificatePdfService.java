package com.example.lms.service.certifipdf;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.example.lms.dto.MainDTO;
import com.example.lms.service.admin.AdminCommonMetaDataService;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import lombok.extern.slf4j.Slf4j;

/**
 * 2025. 11. 26.
 * Autor - JM
 * 관리자 공용 메타 데이터 조회 서비스 (Facade)
 * 관리자 페이지에서 공통적으로 필요한 리스트(학과, 권한 등) 조회 기능을 통합 제공합니다.
 */
@Slf4j
@Service
@Transactional
public class CertificatePdfService {
	
	@Autowired
    private ResourceLoader resourceLoader;
	
	// 만약 Mustache 라이브러리(samskivert)를 사용하신다면:
    @Autowired
    private Mustache.Compiler mustacheCompiler;
	
	
 // 날짜 포맷터 정의: YYYY-MM-DD 형식 지정
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * DTO 리스트를 받아 날짜 필드를 'YYYY-MM-DD' 형식의 문자열로 가공하고 Map 리스트로 반환합니다.
     * 이 가공된 Map 리스트가 Mustache 템플릿에 전달됩니다.
     * @param certificateOfGraduationList 원본 DTO 리스트
     * @return 날짜 필드가 문자열로 포맷된 Map 리스트
     */
    public List<Map<String, Object>> formatCertificateDates(List<MainDTO> certificateOfGraduationList) {
        // Stream API를 사용하여 List<MainDTO>를 List<Map<String, Object>>로 변환
        return certificateOfGraduationList.stream().map(dto -> {
            Map<String, Object> map = new HashMap<>();
            
            // DTO의 필드를 Map에 복사하고 날짜 필드를 포맷합니다.
            map.put("userName", dto.getUserName());
            map.put("userId", dto.getUserId());
            map.put("deptName", dto.getDeptName());
            // 필요한 다른 필드도 여기에 추가합니다.
            
            // ⭐️ 생년월일 포맷 적용
            if (dto.getUserBirth() != null) {
                 map.put("userBirth", DATE_FORMAT.format(dto.getUserBirth()));
            } else {
                 map.put("userBirth", "");
            }
            
            // ⭐️ 입학일 (userCreatedate) 포맷 적용
            if (dto.getUserCreatedate() != null) {
                 map.put("userCreatedate", DATE_FORMAT.format(dto.getUserCreatedate()));
            } else {
                 map.put("userCreatedate", "");
            }
            
            return map;
        }).collect(Collectors.toList());
    }
	
    /**
     * 데이터를 이용하여 Mustache 템플릿을 HTML 문자열로 렌더링합니다.
     * 이 메서드는 포맷팅된 Map 리스트를 인자로 받습니다.
     * @param formattedDataList 날짜 포맷이 완료된 Map 리스트
     * @param certificateValidate 권한 유효성
     * @return 렌더링된 HTML 문자열
     */
	public String renderCertificateHtml(List<Map<String, Object>> formattedDataList, boolean certificateValidate) {
		
		Map<String, Object> model = new HashMap<>();
		// ⭐️ 포맷된 데이터 리스트 사용
		model.put("certificateOfGraduationList", formattedDataList);
		model.put("certificateValidate", certificateValidate);

        StringWriter writer = new StringWriter();
        
        try (Reader templateReader = new InputStreamReader(
                resourceLoader.getResource("classpath:templates/certificate/certificateOfGraduation.mustache")
                        .getInputStream(), StandardCharsets.UTF_8)) {

            Template template = mustacheCompiler.compile(templateReader);
            template.execute(model, writer);
            
		} catch (IOException e) {
            log.error("Mustache 템플릿 로드/렌더링 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("증명서 템플릿을 읽어오거나 렌더링하는 데 실패했습니다.", e);
        }
        
		return writer.toString(); // HTML 문자열
	}

    /**
     * HTML 문자열을 Flying Saucer 라이브러리를 사용하여 PDF 바이트 배열로 변환합니다.
     * @param html 렌더링된 HTML 문자열
     * @return PDF 바이트 배열
     */
	public byte[] generatePdfFromHtml(String html) throws IOException, DocumentException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ITextRenderer renderer = new ITextRenderer();
        
        // ⚠️ 중요: 한글 폰트 등록
        // 이 코드가 없으면 PDF 생성 시 한글이 깨집니다. 
        // 'classpath:static/fonts/NotoSansKR-Regular.ttf' 경로에 실제 폰트 파일이 있어야 합니다.
        try {
            String fontPath = "classpath:static/fonts/NotoSansKR-Regular.ttf"; 
            renderer.getFontResolver().addFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            log.warn("한글 폰트 로드 실패. PDF에 한글이 깨질 수 있습니다.", e);
        }

		renderer.setDocumentFromString(html);
		renderer.layout();
		renderer.createPDF(baos); // DocumentException을 throws 선언으로 처리

		return baos.toByteArray();
	}
}
