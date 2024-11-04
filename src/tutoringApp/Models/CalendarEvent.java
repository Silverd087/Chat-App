package tutoringApp.Models;


public class CalendarEvent {
	private int id;
    private String tuteeName;
    private String tutorName;
    private String course;
    private String date;
    private String time;
	
    
    public CalendarEvent(String tuteeName, String tutorName, String course, String date, String time) {
		this.tuteeName = tuteeName;
		this.tutorName = tutorName;
		this.course = course;
		this.date = date;
		this.time = time;
	}
    public CalendarEvent() {}
    
    
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTuteeName() {
		return tuteeName;
	}
	public void setTuteeName(String tuteeName) {
		this.tuteeName = tuteeName;
	}
	public String getTutorName() {
		return tutorName;
	}
	public void setTutorName(String tutorName) {
		this.tutorName = tutorName;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}


	 @Override
	 public String toString() {
	    return String.format("%s has a tutoring session about %s with %s at %s", tuteeName, course, tutorName, time);
	   }

	
	
    
}