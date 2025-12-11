package member;

import java.sql.SQLException;
import jakarta.servlet.ServletContext;

import common.JDBConnect;

public class MemberDAO extends JDBConnect {

	public MemberDAO(ServletContext application) {
		super(application);
	}

	// 1. 로그인 시도
	public MemberDTO getMemberDTO(String uid, String upass) {
		MemberDTO dto = null;
		String query = "SELECT * FROM member WHERE id=? AND pass=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, uid);
			psmt.setString(2, upass);
			rs = psmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString("name"));
				dto.setRegidate(rs.getDate("regidate"));
			}
		} catch (Exception e) {
			System.out.println("로그인 멤버 조회 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	// 2. ID로 회원 정보 조회 (글쓰기, 정보 수정 등에 사용)
	public MemberDTO getMemberDTO(String uid) {
		MemberDTO dto = null;
		String query = "SELECT * FROM member WHERE id=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, uid);
			rs = psmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString("name"));
				dto.setRegidate(rs.getDate("regidate"));
			}
		} catch (Exception e) {
			System.out.println("멤버 정보 조회 중 예외 발생");
			e.printStackTrace();
		}
		return dto;
	}

	// 3. 회원 가입
	public int insertMember(MemberDTO dto) {
		int result = 0;
		// regidate는 DB의 기본값(sysdate) 사용
		String query = "INSERT INTO member (id, pass, name) VALUES (?, ?, ?)";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getId());
			psmt.setString(2, dto.getPass());
			psmt.setString(3, dto.getName());

			result = psmt.executeUpdate();
		} catch (SQLException e) {
			// ID 중복 등 SQL 예외 처리
			System.out.println("회원 가입 중 SQL 예외 발생");
			e.printStackTrace();
			result = -1; // -1: SQL 오류
		} catch (Exception e) {
			System.out.println("회원 가입 중 예외 발생");
			e.printStackTrace();
		}

		return result;
	}

	// 4. ID 중복 확인 (AJAX)
	public boolean isIdDuplicate(String id) {
		boolean isDuplicate = false;
		String query = "SELECT COUNT(*) FROM member WHERE id=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next() && rs.getInt(1) > 0) {
				isDuplicate = true;
			}
		} catch (Exception e) {
			System.out.println("ID 중복 확인 중 예외 발생");
			e.printStackTrace();
		}

		return isDuplicate;
	}

	// 5. 회원 정보 수정
	public int updateMember(MemberDTO dto) {
		int result = 0;
		// 이름은 필수 수정, 비밀번호는 입력했을 때만 수정 (Oracle NVL 사용)
		String query = "UPDATE member SET name=?, pass=NVL(?, pass) WHERE id=? AND pass=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getName());
			// 패스워드가 빈 문자열/null이면 NVL에 의해 기존 pass 유지
			psmt.setString(2, dto.getNewPass() != null && !dto.getNewPass().isEmpty() ? dto.getNewPass() : null);
			psmt.setString(3, dto.getId());
			psmt.setString(4, dto.getPass()); // 현재 비밀번호로 인증

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("회원 정보 수정 중 예외 발생");
			e.printStackTrace();
		}

		return result;
	}
}