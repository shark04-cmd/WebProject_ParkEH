package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.ServletContext;

// DB 연결을 위한 공통 클래스
public class JDBConnect {
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;

	// 기본 생성자
	public JDBConnect() {
		// web.xml에 정의된 DB 접속 정보를 가져와 연결합니다. (직접 변수 사용)
		try {
			// 드라이버 로드
			Class.forName("oracle.jdbc.OracleDriver");

			// 연결
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String id = "webproject_db";
			String pw = "1234";
			con = DriverManager.getConnection(url, id, pw);

			System.out.println("Oracle DB 연결 성공 (JDBConnect)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// web.xml의 컨텍스트 파라미터를 사용한 생성자
	public JDBConnect(ServletContext application) {
		try {
			// JDBC 드라이버 로드
			String driver = application.getInitParameter("OracleDriver");
			Class.forName(driver);

			// DB 연결 정보 얻기
			String url = application.getInitParameter("OracleURL");
			String id = application.getInitParameter("OracleId");
			String pw = application.getInitParameter("OraclePwd");

			// DB 연결
			con = DriverManager.getConnection(url, id, pw);

			System.out.println("Oracle DB 연결 성공 (JDBConnect - web.xml param)");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 자원 해제
	public void close() {
		try {
			if (rs != null)
				rs.close();
			if (psmt != null)
				psmt.close();
			if (stmt != null)
				stmt.close();
			if (con != null)
				con.close();

			System.out.println("DB 자원 반납");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}