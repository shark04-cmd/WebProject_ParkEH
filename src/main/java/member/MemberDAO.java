package member;

import common.JDBConnect;
import jakarta.servlet.ServletContext;
import java.sql.SQLException;

// 회원 데이터베이스 접근 객체 (DAO)
public class MemberDAO extends JDBConnect {

	// web.xml의 DB 설정 정보를 받아 연결합니다.
	public MemberDAO(ServletContext application) {
		super(application);
	}

	/**
	 * 명시한 아이디와 패스워드에 해당하는 회원을 찾아 반환합니다.
	 * 
	 * @param id   사용자 아이디
	 * @param pass 사용자 패스워드
	 * @return 일치하는 회원이 없으면 null, 있으면 MemberDTO 객체를 반환
	 */
	public MemberDTO getMemberDTO(String id, String pass) {
		MemberDTO dto = null;
		String query = "SELECT * FROM member WHERE id=? AND pass=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			psmt.setString(2, pass);

			rs = psmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("id"));
				dto.setPass(rs.getString("pass"));
				dto.setName(rs.getString("name"));
				dto.setEmail(rs.getString("email"));
				dto.setPhone(rs.getString("phone"));
				dto.setRegidate(rs.getDate("regidate"));
			}
		} catch (Exception e) {
			System.out.println("getMemberDTO 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	/**
	 * 새로운 회원 정보를 DB에 삽입합니다.
	 * 
	 * @param dto 삽입할 회원 정보 (아이디, 패스워드, 이름, 이메일, 전화번호)
	 * @return 성공하면 1, 실패하면 0 반환
	 */
	public int insertMember(MemberDTO dto) {
		int result = 0;

		try {
			// 쿼리문 준비
			String query = "INSERT INTO member (id, pass, name, email, phone) VALUES (?, ?, ?, ?, ?)";

			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getId());
			psmt.setString(2, dto.getPass());
			psmt.setString(3, dto.getName());
			psmt.setString(4, dto.getEmail());
			psmt.setString(5, dto.getPhone());

			// 쿼리 실행
			result = psmt.executeUpdate();
		} catch (SQLException e) {
			// SQL 예외 발생 시 (예: PK 제약조건 위반으로 인한 아이디 중복)
			System.out.println("insertMember 중 SQL 예외 발생 (아이디 중복 가능성)");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("insertMember 중 일반 예외 발생");
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 아이디 중복 여부를 확인합니다.
	 * 
	 * @param id 확인할 아이디
	 * @return 중복이면 true, 사용 가능하면 false
	 */
	public boolean idCheck(String id) {
		boolean result = true; // 기본값: true (중복)
		String query = "SELECT COUNT(*) FROM member WHERE id=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					result = false; // 카운트가 0이면 사용 가능 (중복 아님)
				}
			}
		} catch (Exception e) {
			System.out.println("ID 중복 확인 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
			// DB 오류 시 안전하게 중복으로 처리하여 가입을 막습니다.
			result = true;
		}
		return result;
	}
}