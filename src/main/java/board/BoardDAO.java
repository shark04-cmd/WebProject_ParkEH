package board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import common.JDBConnect;

public class BoardDAO extends JDBConnect {

	private String tableName;

	public BoardDAO(ServletContext application, String boardType) {
		super(application);

		// 🔴 문제의 핵심 수정 부분: boardType 유효성 검사 강화
		String type = "free";
		if (boardType != null && !boardType.trim().isEmpty()) {
			String lowerCaseType = boardType.trim().toLowerCase();
			// 정해진 타입(free, qna, data)만 허용하고 아니면 기본값 'free' 유지
			if (lowerCaseType.equals("free") || lowerCaseType.equals("qna") || lowerCaseType.equals("data")) {
				type = lowerCaseType;
			}
		}
		this.tableName = "BOARD_" + type.toUpperCase();
	}

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

	public List<BoardDTO> selectListPaging(Map<String, Object> map) {
		List<BoardDTO> boardList = new ArrayList<BoardDTO>();

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

			psmt.setString(index++, map.get("start").toString());
			psmt.setString(index, map.get("end").toString());

			rs = psmt.executeQuery();

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

	public int insertWrite(BoardDTO dto) {
		int result = 0;

		String query = "INSERT INTO " + tableName + " (num, title, content, id, name, visitcount, likecount) "
				+ " VALUES (SEQ_" + tableName + ".NEXTVAL, ?, ?, ?, ?, 0, 0)";

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

	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO();
		// B.name 대신 M.name(member_name)을 사용하여 ID가 아닌 이름 필드를 가져옴
		String query = "SELECT B.*, M.name AS member_name FROM " + tableName + " B "
				+ " INNER JOIN member M ON B.id = M.id " + " WHERE num=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();

			if (rs.next()) {
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getInt("visitcount"));
				dto.setLikeCount(rs.getInt("likecount"));
				// 상세 보기에서는 member 테이블과 JOIN한 별칭 member_name을 사용
				dto.setName(rs.getString("member_name"));

				if (tableName.contains("DATA")) {
					dto.setFileName(rs.getString("filename"));
				}
			}
		} catch (Exception e) {
			System.out.println(tableName + " 게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}
		return dto;
	}

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

	public int updateEdit(BoardDTO dto) {
		int result = 0;
		String query = "UPDATE " + tableName + " SET title=?, content=?";

		if (dto.getBoardType() != null && dto.getBoardType().equals("data")) {
			query += ", filename=?";
		}

		query += " WHERE num=? AND id=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());

			int index = 3;
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