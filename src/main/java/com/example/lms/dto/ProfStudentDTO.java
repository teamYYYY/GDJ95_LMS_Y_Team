package com.example.lms.dto;

import lombok.Data;

/**
 * 2025. 11. 25.
 * Author - SH
 * TB_ENROLLMENT, TB_SYSUSER, TB_COURSE TABLE DTO
 * 학생 과제 제출 정보
 */

@Data
public class ProfStudentDTO {
	private int enrollmentNo;      // 수강신청번호 
	private int studentUserNo;     // 학생번호
	
	private String studentId;	   // 학생아이디     user_id
	private String studentName;    // 학생이름      user_name
	private String studentEmail;   // 학생이메일     user_email
	private String studentPhone;   // 학생전화번호   user_phone
	private int studentGrade;      // 학생학년		  user_grade
	private String deptCode;       // 전공(학과코드) dept_code
	private String enrollmentDate; // createdate
}
