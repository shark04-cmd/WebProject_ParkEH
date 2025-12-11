package board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletContext;

import common.JDBConnect;

public class BoardDAO extends JDBConnect {

	private String tableName;

	// 생성자: 게시판 유형에 따라 테이블 이름을 동적으로 설정합니다.
	public BoardDAO(ServletContext application, String boardType) {
		super(application);
		if ("free".equals(boardType)) {
			this.tableName = "board_free";
		} else if ("qna".equals(boardType)) {
			this.tableName = "board_qna";
		} else if ("data".equals(boardType)) {
			this.tableName = "board_data";
		} else {
			this.tableName = "board_free"; // 기본값
		}
	}

	// 1. 검색 조건에 맞는 게시물의 개수를 반환합니다.
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;
		// 쿼리에 tableName을 적용합니다.
		String query = "SELECT COUNT(*) FROM " + tableName;

		// 검색어가 있을 경우 WHERE 조건 추가
		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'";
		}

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			rs.next();
			totalCount = rs.getInt(1);
		} catch (Exception e) {
			System.out.println("게시물 카운트 중 예외 발생 (tableName: " + tableName + ")");
			e.printStackTrace();
		}
		return totalCount;
	}

	// 3. 검색 조건에 맞는 게시물 목록을 반환합니다. (페이징 적용)
	public List<BoardDTO> selectListPaging(Map<String, Object> map) {
		List<BoardDTO> boardList = new ArrayList<BoardDTO>();

		// 쿼리에 tableName을 적용합니다.
		String query = "SELECT * FROM ( " + " SELECT Tb.*, ROWNUM rNum FROM ( " + "  SELECT * FROM " + tableName;

		// 검색어가 있을 경우 WHERE 조건 추가
		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'";
		}

		query += " ORDER BY num DESC " + " ) Tb " + " ) WHERE rNum BETWEEN ? AND ?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setInt(1, (Integer) map.get("start")); // 시작 번호
			psmt.setInt(2, (Integer) map.get("end")); // 끝 번호
			rs = psmt.executeQuery();

			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getInt("visitcount"));
				dto.setBoardType(rs.getString("boardType"));
				dto.setName(rs.getString("name"));
				dto.setLikeCount(rs.getInt("likeCount"));

				// 자료실일 경우에만 fileName을 DTO에 설정 (DB 구조에 따라)
				if (this.tableName.equals("board_data") || rs.getString("fileName") != null) {
					dto.setFileName(rs.getString("fileName"));
				} else {
					dto.setFileName(null);
				}

				boardList.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 목록(페이징) 조회 중 예외 발생 (tableName: " + tableName + ")");
			e.printStackTrace();
		}

		return boardList;
	}

	// 4. 게시물 상세 내용을 반환합니다.
	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO();

		// DTO에 name 필드가 있으므로, 작성자 이름을 얻기 위해 member 테이블과 JOIN 합니다.
		String query = "SELECT B.*, M.name " + " FROM " + tableName + " B JOIN member M ON B.id=M.id " + " WHERE num=?";

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
				dto.setBoardType(rs.getString("boardType"));
				// M.name 대신 B.name을 사용 (BOARD 테이블에 name 필드가 있으므로)
				dto.setName(rs.getString("B.name"));
				dto.setLikeCount(rs.getInt("likeCount"));

				if (this.tableName.equals("board_data") || rs.getString("fileName") != null) {
					dto.setFileName(rs.getString("fileName"));
				} else {
					dto.setFileName(null);
				}
			}
		} catch (Exception e) {
			System.out.println("게시물 상세 보기 중 예외 발생 (tableName: " + tableName + ")");
			e.printStackTrace();
		}

		return dto;
	}

	// 5. 게시물 조회수를 증가시킵니다.
	public void updateVisitCount(String num) {
		String query = "UPDATE " + tableName + " SET visitcount=visitcount+1 " + " WHERE num=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			psmt.executeUpdate();
		} catch (Exception e) {
			// System.out.println("게시물 조회수 증가 중 예외 발생");
			// e.printStackTrace();
		}
	}

	// 6. 새로운 게시물을 등록합니다.
	public int insertWrite(BoardDTO dto) {
		int result = 0;

		// 쿼리에 tableName을 적용합니다.
		String query = "INSERT INTO " + tableName + " ( " + " num, title, content, id, boardType, name, fileName ) "
				+ " VALUES ( " + " (SELECT NVL(MAX(num), 0) + 1 FROM " + tableName + "), " + " ?, ?, ?, ?, ?, ?)";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getId());
			psmt.setString(4, dto.getBoardType());
			psmt.setString(5, dto.getName());

			String fileName = dto.getFileName();
			// 자료실이 아니거나 파일명이 null인 경우 빈 문자열 또는 null로 처리
			if (!this.tableName.equals("board_data") || fileName == null) {
				fileName = null;
			}
			psmt.setString(6, fileName);

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 등록 중 예외 발생 (tableName: " + tableName + ")");
			e.printStackTrace();
		}

		return result;
	}

	// 7. 게시물을 수정합니다.
	public int updateEdit(BoardDTO dto) {
		int result = 0;

		String query = "UPDATE " + tableName + " SET " + " title=?, content=?";

		// 자료실일 경우에만 fileName 필드 수정 구문을 추가합니다.
		if (this.tableName.equals("board_data")) {
			query += ", fileName=?";
		}

		query += " WHERE num=? AND id=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());

			int index = 3;
			if (this.tableName.equals("board_data")) {
				psmt.setString(index++, dto.getFileName());
			}

			psmt.setString(index++, dto.getNum());
			psmt.setString(index, dto.getId());

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생 (tableName: " + tableName + ")");
			e.printStackTrace();
		}

		return result;
	}

	// 8. 게시물을 삭제합니다.
	public int deletePost(String num) {
		int result = 0;

		String query = "DELETE FROM " + tableName + " WHERE num=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생 (tableName: " + tableName + ")");
			e.printStackTrace();
		}

		return result;
	}

	// 9. 좋아요 수 증가
	public void updateLikeCount(String num) {
		String query = "UPDATE " + tableName + " SET likeCount=likeCount+1 " + " WHERE num=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("좋아요 수 증가 중 예외 발생 (tableName: " + tableName + ")");
			e.printStackTrace();
		}
	}
}