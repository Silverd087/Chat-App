package tutoringApp.Models;

import java.sql.Date;
import java.sql.Time;

public class TutoringSession {
	private int id;
	private String tutorName;
	private String course;
	private String chapter;
	private String time;
	private String date;
	private String status;
	
	public TutoringSession(int id,String name, String course, String chapter, String time, String date) {
		this.id = id;
		this.tutorName = name;
		this.course = course;
		this.chapter = chapter;
		this.time = time;
		this.date = date;
		this.status = "not booked";
	}

	public TutoringSession(int id,String name, String course, String chapter, String time, String date,String status) {
		this.id = id;
		this.tutorName = name;
		this.course = course;
		this.chapter = chapter;
		this.time = time;
		this.date = date;
		this.status = status;
	}
	public  int getId() {
		return id;
	}

	public  void setId(int id) {
		this.id = id;
	}

	public String getTutorName() {
		return tutorName;
	}

	public void setTutorName(String name) {
		this.tutorName = name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getChapter() {
		return chapter;
	}

	public void setChapter(String chapter) {
		this.chapter = chapter;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


	@Override
	public String toString() {
		return "TutoringSession [name=" + tutorName + ", course=" + course + ", chapter=" + chapter + ", time=" + time
				+ ", date=" + date + ", status=" + status + "]";
	}
	
	
	
	
	
	


	
}
