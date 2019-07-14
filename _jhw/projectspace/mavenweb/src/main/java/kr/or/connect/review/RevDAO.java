package kr.or.connect.review;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RevDAO {
	private static RevDAO instance = new RevDAO();
	private RevDAO() {};
	public static RevDAO getInstance() {
		return instance;
	}
	

	String driver = "oracle.jdbc.driver.OracleDriver";
	String dburl = "jdbc:oracle:thin:@localhost:1521:xe";
	String dbuser = "hr";
	String dbpassword = "hr";
	
	
	
	public List<Rev> getRevs() {
		List<Rev> list = new ArrayList<>();
		try {
			Class.forName(driver);
			System.out.println("jdbc driver �ε� ����");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String select = "SELECT * FROM REVIEWS order by DATE_CRE desc";
		try (Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
				PreparedStatement ps = conn.prepareStatement(select)) {
			try (ResultSet rs = ps.executeQuery()){
				while(rs.next()) {
					String place = rs.getString("place");
					String userId = rs.getString("user_id");
					int grade = rs.getInt("grade");
					String opinion = rs.getString("opinion");
					Date date = rs.getDate("date_cre");

					Rev rev = new Rev(place,userId,grade,opinion);
					rev.setDate(date);
					list.add(rev);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	public int addRev(Rev rev) {
		int insertCount = 0;
		
		try {
			Class.forName(driver);
			System.out.println("jdbc driver �ε� ����");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String insert = "INSERT INTO REVIEWS(place,user_id,grade,opinion) VALUES ( ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
				PreparedStatement ps = conn.prepareStatement(insert)){
			ps.setString(1,rev.getPlace());
			ps.setString(2,rev.getUserId());
			ps.setInt(3,rev.getGrade());
			ps.setString(4,rev.getOpinion());
			insertCount = ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return insertCount;
	}
		
	
	

}