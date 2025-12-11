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
		String query = "SELECT ID, PASS, NAME, EMAIL, PHONE, REGIDATE FROM MEMBER WHERE ID=? AND PASS=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			psmt.setString(2, pass);

			rs = psmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("ID"));
				dto.setPass(rs.getString("PASS"));
				dto.setName(rs.getString("NAME"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setPhone(rs.getString("PHONE"));
				dto.setRegidate(rs.getDate("REGIDATE"));
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
			String query = "INSERT INTO MEMBER (ID, PASS, NAME, EMAIL, PHONE, REGIDATE) VALUES (?, ?, ?, ?, ?, sysdate)";

			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getId());
			psmt.setString(2, dto.getPass());
			psmt.setString(3, dto.getName());
			psmt.setString(4, dto.getEmail());
			psmt.setString(5, dto.getPhone());

			result = psmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("insertMember 중 SQL 예외 발생 (아이디 중복 가능성 또는 DB 연결 오류)");
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
		boolean result = true;
		String query = "SELECT COUNT(*) FROM MEMBER WHERE ID=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);
			rs = psmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);
				if (count == 0) {
					result = false;
				}
			}
		} catch (Exception e) {
			System.out.println("ID 중복 확인 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
			result = true;
		}
		return result;
	}

	/**
	 * [추가] 특정 아이디에 해당하는 회원 정보를 찾아 반환합니다. (패스워드 검사 제외)
	 * 
	 * @param id 사용자 아이디
	 * @return 일치하는 회원이 없으면 null, 있으면 MemberDTO 객체를 반환
	 */
	public MemberDTO getMemberDTOById(String id) {
		MemberDTO dto = null;
		String query = "SELECT ID, PASS, NAME, EMAIL, PHONE, REGIDATE FROM MEMBER WHERE ID=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, id);

			rs = psmt.executeQuery();

			if (rs.next()) {
				dto = new MemberDTO();
				dto.setId(rs.getString("ID"));
				dto.setPass(rs.getString("PASS")); // 현재 비밀번호도 가져옴
				dto.setName(rs.getString("NAME"));
				dto.setEmail(rs.getString("EMAIL"));
				dto.setPhone(rs.getString("PHONE"));
				dto.setRegidate(rs.getDate("REGIDATE"));
			}
		} catch (Exception e) {
			System.out.println("getMemberDTOById 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	/**
	 * [추가] 회원 정보를 업데이트합니다.
	 * 
	 * @param dto 업데이트할 회원 정보 (ID, 새 PASS(null 가능), NAME, EMAIL, PHONE)
	 * @return 성공하면 1, 실패하면 0 반환
	 */
	public int updateMember(MemberDTO dto) {
		int result = 0;
		// 패스워드가 비어있지 않은 경우에만 PASS 필드를 업데이트하는 쿼리.
		// NVL 함수로 PASS가 null이면 기존 PASS를 유지합니다. (오라클 기준)
		String query = "UPDATE MEMBER SET NAME=?, EMAIL=?, PHONE=?, PASS=NVL(?, PASS) WHERE ID=?";

		// DTO의 Pass가 null이거나 비어있으면 null로 설정하여 NVL이 기존 PASS를 유지하게 함.
		String passToUpdate = (dto.getPass() != null && !dto.getPass().trim().isEmpty()) ? dto.getPass() : null;

		try {
			psmt = con.prepareStatement(query);

			psmt.setString(1, dto.getName());
			psmt.setString(2, dto.getEmail());
			psmt.setString(3, dto.getPhone());
			psmt.setString(4, passToUpdate);
			psmt.setString(5, dto.getId());

			result = psmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("updateMember 중 SQL 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("updateMember 중 일반 예외 발생");
			e.printStackTrace();
		}

		return result;
	}
}