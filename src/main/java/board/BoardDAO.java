package board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import common.JDBConnect;

// 이 클래스는 데이터베이스 연결 기능(JDBConnect)을 상속받아 게시판 데이터 접근 기능이 구현됩니다.
public class BoardDAO extends JDBConnect {

	// 이 필드는 현재 DAO 작업에 사용될 게시판 테이블 이름이 저장됩니다. (예: BOARD_FREE, BOARD_QNA)
	private String tableName;

	// 이 생성자는 객체 초기화 시 요청된 boardType에 기반하여 사용할 테이블 이름이 설정됩니다.
	public BoardDAO(ServletContext application, String boardType) {
		super(application);

		// 게시판 타입(free, qna, data)의 유효성 검사를 통해 테이블 이름이 'BOARD_타입' 형태로 확정됩니다.
		String type = "free";
		if (boardType != null && !boardType.trim().isEmpty()) {
			String lowerCaseType = boardType.trim().toLowerCase();
			if (lowerCaseType.equals("free") || lowerCaseType.equals("qna") || lowerCaseType.equals("data")) {
				type = lowerCaseType;
			}
		}
		this.tableName = "BOARD_" + type.toUpperCase();
	}

	// 이 메서드는 검색 조건에 일치하는 게시물의 총 개수가 데이터베이스에서 조회됩니다.
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;
		String query = "SELECT COUNT(*) FROM " + tableName;
		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField") + " LIKE ?";
		}

		try {
			psmt = con.prepareStatement(query);
			if (map.get("searchWord") != null) {
				psmt.setString(1, "%" + map.get("searchWord") + "%");
			}

			rs = psmt.executeQuery();
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println(tableName + " 게시물 카운트 중 예외 발생");
			e.printStackTrace();
		}
		return totalCount;
	}

	// 이 메서드는 지정된 검색 조건 및 페이지 범위(시작/끝 행 번호) 내의 게시물 목록이 조회됩니다.
	public List<BoardDTO> selectListPaging(Map<String, Object> map) {
		List<BoardDTO> boardList = new ArrayList<BoardDTO>();

		// 🚨 [수정]: 특수 공백 문자를 모두 일반 공백으로 대체하여 ORA-00911 오류 방지
		String query = "SELECT * FROM ( " + "    SELECT ROWNUM rNum, B.* FROM ( " + "        SELECT * FROM "
				+ tableName;

		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField") + " LIKE ?";
		}

		query += " ORDER BY num DESC " + "    ) B " + ") " + "WHERE rNum BETWEEN ? AND ?";

		try {
			psmt = con.prepareStatement(query);
			int index = 1;

			if (map.get("searchWord") != null) {
				psmt.setString(index++, "%" + map.get("searchWord") + "%");
			}

			// setInt를 사용하는 방식은 이미 수정되었으므로 유지
			psmt.setInt(index++, (Integer) map.get("start"));
			psmt.setInt(index, (Integer) map.get("end"));

			rs = psmt.executeQuery();

			// 조회된 각 레코드가 BoardDTO 객체로 변환되어 리스트에 추가됩니다.
			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));

				dto.setName(rs.getString("name"));

				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getInt("visitcount"));
				dto.setLikeCount(rs.getInt("likecount"));
				dto.setBoardType((String) map.get("boardType"));

				// 자료실 게시판(DATA) 테이블인 경우 파일 이름 필드가 추가로 설정됩니다.
				if (tableName.contains("DATA")) {
					dto.setFileName(rs.getString("filename"));
				}

				boardList.add(dto);
			}
		} catch (Exception e) {
			System.out.println(tableName + " 게시물 목록 조회 중 예외 발생");
			e.printStackTrace();
		}

		return boardList;
	}

	// 이 메서드는 새로운 게시물 정보가 데이터베이스에 등록됩니다.
	public int insertWrite(BoardDTO dto) {
		int result = 0;

		// 기본 게시물 등록을 위한 SQL 쿼리입니다.
		String query = "INSERT INTO " + tableName + " (num, title, content, id, name, visitcount, likecount) "
				+ " VALUES (SEQ_" + tableName + ".NEXTVAL, ?, ?, ?, ?, 0, 0)";

		// 자료실 게시판(DATA)인 경우 파일 이름 필드가 포함된 쿼리로 변경됩니다.
		if (tableName.contains("DATA")) {
			query = "INSERT INTO " + tableName + " (num, title, content, id, name, filename, visitcount, likecount) "
					+ " VALUES (SEQ_" + tableName + ".NEXTVAL, ?, ?, ?, ?, ?, 0, 0)";
		}

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getId());
			psmt.setString(4, dto.getName());

			// 자료실 게시판인 경우 파일 이름 파라미터가 설정됩니다.
			if (tableName.contains("DATA")) {
				psmt.setString(5, dto.getFileName());
			}

			result = psmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(tableName + " 게시물 등록 중 SQL 예외 발생");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(tableName + " 게시물 등록 중 예외 발생");
			e.printStackTrace();
		}

		return result;
	}

	// 이 메서드는 특정 게시물 번호의 조회수가 1 증가되도록 데이터베이스가 업데이트됩니다.
	public void updateVisitCount(String num) {
		String query = "UPDATE " + tableName + " SET visitcount = visitcount + 1 WHERE num=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(tableName + " 게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}

	// 이 메서드는 특정 번호의 게시물 상세 정보가 조회되어 DTO에 설정됩니다.
	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO();
		// 게시물 테이블과 회원 테이블을 JOIN하여 작성자 이름이 함께 조회됩니다.
		String query = "SELECT B.*, M.name AS member_name FROM " + tableName + " B "
				+ " INNER JOIN member M ON B.id = M.id " + " WHERE num=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();

			// 조회 결과가 있을 경우, DTO 필드에 데이터가 매핑됩니다.
			if (rs.next()) {
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getInt("visitcount"));
				dto.setLikeCount(rs.getInt("likecount"));
				dto.setName(rs.getString("member_name"));

				// 자료실 게시판인 경우 파일 이름도 함께 설정됩니다.
				if (tableName.contains("DATA")) {
					dto.setFileName(rs.getString("filename"));
				}
			}
		} catch (Exception e) {
			// System.out.println 구문 수정 (오타: System.println -> System.out.println)
			System.out.println(tableName + " 게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		return dto;
	}

	// 이 메서드는 특정 번호의 게시물이 데이터베이스에서 삭제됩니다.
	public int deletePost(String num) {
		int result = 0;
		String query = "DELETE FROM " + tableName + " WHERE num=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(tableName + " 게시물 삭제 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}

//	특정 번호의 게시물 내용이 수정되고, 작성자 ID를 통해 권한을 확이
	public int updateEdit(BoardDTO dto) {
		int result = 0;
		String query = "UPDATE " + tableName + " SET title=?, content=?";

//		자료실 게시판인 경우 파일 이름 필드 수정 옵션이 쿼리에 추가됩니다.
		if (dto.getBoardType() != null && dto.getBoardType().equals("data")) {
			query += ", filename=?";
		}

		query += " WHERE num=? AND id=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());

			int index = 3;
//			자료실 게시판인 경우 파일 이름 파라미터가 설정됩니다.
			if (dto.getBoardType() != null && dto.getBoardType().equals("data")) {
				psmt.setString(index++, dto.getFileName());
			}

			psmt.setString(index++, dto.getNum());
			psmt.setString(index, dto.getId());

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(tableName + " 게시물 수정 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}

//	이 메서드는 특정 번호의 게시물 좋아요 수가 1 증가되도록 데이터베이스가 업데이트 됨. (아직 미구현 보이기만 함)
	public int updateLikeCount(String num) {
		int result = 0;
		String query = "UPDATE " + tableName + " SET likecount = likecount + 1 WHERE num=?";
		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println(tableName + " 좋아요 증가 중 예외 발생");
			e.printStackTrace();
		}
		return result;
	}
}