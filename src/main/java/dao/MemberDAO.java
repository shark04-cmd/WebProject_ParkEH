package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletContext;
import dto.MemberDTO;

public class MemberDAO {
	private Connection con;
	private PreparedStatement psmt;
	private ResultSet rs;

	public MemberDAO(ServletContext application) {
		try {
			String driver = application.getInitParameter("OracleDriver");
			String url = application.getInitParameter("OracleURL");
			String id = application.getInitParameter("OracleId");
			String pwd = application.getInitParameter("OraclePwd");

			Class.forName(driver);
			con = java.sql.DriverManager.getConnection(url, id, pwd);
			System.out.println("DB 연결 성공(MemberDAO)");
		} catch (Exception e) {
			System.out.println("DB 연결 실패(MemberDAO)");
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (psmt != null)
				psmt.close();
			if (con != null)
				con.close();
			System.out.println("DB 자원 반납");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MemberDTO getMemberDTO(String uid, String upass) {
		MemberDTO dto = new MemberDTO();
		String query = "SELECT * FROM MEMBER WHERE ID=? AND PASS=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, uid);
			psmt.setString(2, upass);
			rs = psmt.executeQuery();

			if (rs.next()) {
				dto.setId(rs.getString("id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString("name"));
				dto.setRegidate(rs.getDate("regidate"));
			} else {
				dto.setId(null);
			}
		} catch (Exception e) {
			System.out.println("로그인 처리 중 예외 발생");
			e.printStackTrace();
		}
		return dto;
	}
}