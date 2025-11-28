package com.example.lms.dto;

import lombok.Data;

@Data
public class ProfCourseAttendDTO {
	private int studentUserNo;    
    private String studentName;
    private String studentId;
    private String deptName;
    private String studentPhone;

    private int attendanceStatus; // 0출석 1결석 2지각
    
    private boolean isAttend;
    private boolean isAbsent;
    private boolean isLate;
}
