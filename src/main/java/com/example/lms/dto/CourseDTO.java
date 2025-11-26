package com.example.lms.dto;

import lombok.Data;

@Data
public class CourseDTO {
	private int courseNo;
	private int professorUserNo;
	private int courseYear;
	private int courseSemester;
	private String courseName;
	private String courseDescription;
	private int courseCapacity;
	private int courseScore;
	private String courseClassroom;
	private int courseStatus;
	private String createdate;
	private String updatedate;
}