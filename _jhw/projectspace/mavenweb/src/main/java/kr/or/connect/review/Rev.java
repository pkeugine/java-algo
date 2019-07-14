package kr.or.connect.review;

import java.sql.Date;

public class Rev {

	private String place;
	private String userId;
	private Integer grade;
	private String opinion;
	private Date date;
	
	public Rev(String place, String userId, Integer grade, String opinion) {
		super();
		this.place = place;
		this.userId= userId;
		this.grade = grade;
		this.opinion = opinion;
	}
	
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public Integer getGrade() {
		return grade;
	}
	public void setPlace(Integer grade) {
		this.grade = grade;
	}
	
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Rev [place=" + place + ", userId=" + userId + ", grade=" + grade + ", opinion=" + opinion + ", date="
				+ date + "]";
	}

}





