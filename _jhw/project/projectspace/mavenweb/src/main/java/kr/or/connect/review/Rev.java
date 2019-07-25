package kr.or.connect.review;


public class Rev {

	private String place;
	private String userId;
	private Integer grade;
	private String opinion;
	private String date;
	private String imagePath;

	
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
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
	public String getOpinion() {
		return opinion;
	}
	public void setOpinion(String opinion) {
		this.opinion = opinion;
	}
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}

	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	@Override
	public String toString() {
		return "Rev [place=" + place + ", userId=" + userId + ", grade=" + grade + ", opinion=" + opinion + ", date="
				+ date + ", imagePath=" + imagePath + "]";
	}


}






