package member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class MemberDAO {

	public Connection getConnection() throws Exception {
		Context initCtx = new InitialContext();
		Context envCtx = (Context) initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/webproject_db");
		Connection con = ds.getConnection();
		return con;
	}

	private void closeResources(Connection con, PreparedStatement pstmt, ResultSet rs) {
		if (rs != null) try { rs.close(); } catch(Exception e) {}
		if (pstmt != null) try { pstmt.close(); } catch(Exception e) {}
		if (con != null) try { con.close(); } catch(Exception e) {}
	}

	public MemberDTO getMember(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberDTO member = null;
		String sql = "SELECT * FROM member WHERE id = ?";

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				member = new MemberDTO();
				member.setId(rs.getString("ID"));
				member.setPass(rs.getString("PASS"));
				member.setName(rs.getString("NAME"));
				member.setEmail(rs.getString("EMAIL"));
				member.setPhoneNum(rs.getString("PHONENUM"));
				member.setRegidate(rs.getDate("REGIDATE"));
			}
		} catch (Exception e) {
			System.err.println("DB 조회 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(con, pstmt, rs);
		}
		return member;
	}

	public int insertMember(MemberDTO member) {
		Connection con = null;
		PreparedStatement pstmt = null;

		String sql = "INSERT INTO member (ID, PASS, NAME, EMAIL, PHONENUM) VALUES (?, ?, ?, ?, ?)";
		int result = 0;

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPass());
			pstmt.setString(3, member.getName());
			pstmt.setString(4, member.getEmail());
			pstmt.setString(5, member.getPhoneNum());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.err.println("DB 삽입 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(con, pstmt, null);
		}
		return result;
	}

	public int updateMember(MemberDTO member) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "UPDATE member SET PASS = ?, NAME = ?, EMAIL = ?, PHONENUM = ? WHERE ID = ?";

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, member.getPass());
			pstmt.setString(2, member.getName());
			pstmt.setString(3, member.getEmail());
			pstmt.setString(4, member.getPhoneNum());
			pstmt.setString(5, member.getId());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.err.println("회원 정보 수정 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(con, pstmt, null);
		}
		return result;
	}
}