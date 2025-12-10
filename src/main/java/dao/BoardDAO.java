package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import jakarta.servlet.ServletContext;
import dto.BoardDTO;

public class BoardDAO {
	private Connection con;
	private PreparedStatement psmt;
	private ResultSet rs;
	private Statement stmt;

	public BoardDAO(ServletContext application) {
		try {
			String driver = application.getInitParameter("OracleDriver");
			String url = application.getInitParameter("OracleURL");
			String id = application.getInitParameter("OracleId");
			String pwd = application.getInitParameter("OraclePwd");

			Class.forName(driver);
			con = java.sql.DriverManager.getConnection(url, id, pwd);
			System.out.println("DB 연결 성공(BoardDAO)");
		} catch (Exception e) {
			System.out.println("DB 연결 실패(BoardDAO)");
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

	public int selectCount(Map<String, Object> map) {
		int totalCount = 0;
		String query = "SELECT COUNT(*) FROM BOARD";

		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%'";
		}

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			if (rs.next()) {
				totalCount = rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("게시물 카운트 중 예외 발생");
			e.printStackTrace();
		}

		return totalCount;
	}

	public List<BoardDTO> selectListPage(Map<String, Object> map) {
		List<BoardDTO> board = new Vector<BoardDTO>();

		String query = " SELECT * FROM ( " + "    SELECT ROWNUM AS rNum, T.* FROM ( " + "        SELECT * FROM BOARD ";

		if (map.get("searchWord") != null) {
			query += " WHERE " + map.get("searchField") + " LIKE '%" + map.get("searchWord") + "%' ";
		}

		query += " ORDER BY NUM DESC " + "    ) T " + " ) " + " WHERE rNum BETWEEN ? AND ?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, map.get("start").toString());
			psmt.setString(2, map.get("end").toString());

			rs = psmt.executeQuery();

			while (rs.next()) {
				BoardDTO dto = new BoardDTO();

				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("regdate"));
				dto.setVisitcount(rs.getString("readcount"));

				board.add(dto);
			}
		} catch (Exception e) {
			System.out.println("게시물 목록 페이징 조회 중 예외 발생");
			e.printStackTrace();
		}

		return board;
	}

	public int insertWrite(BoardDTO dto) {
		int result = 0;

		try {
			String query = "INSERT INTO BOARD (NUM, SUBJECT, CONTENT, ID, REGDATE, READCOUNT, TYPE) "
					+ "VALUES (SEQ_BOARD_NUM.NEXTVAL, ?, ?, ?, SYSDATE, 0, 'free')";

			psmt = con.prepareStatement(query);
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getContent());
			psmt.setString(3, dto.getId());

			result = psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("게시물 등록 중 예외 발생");
			e.printStackTrace();
		}

		return result;
	}

	// --- 상세 보기 기능 추가 (Start) ---

	// 조회수 증가
	public void updateVisitCount(String num) {
		String query = "UPDATE BOARD SET READCOUNT = READCOUNT + 1 WHERE NUM = ?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			psmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}

	// 게시물 상세 정보 조회 (회원 이름 포함)
	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO();
		String query = "SELECT B.*, M.NAME " + "FROM BOARD B JOIN MEMBER M " + "ON B.ID=M.ID " + "WHERE NUM=?";

		try {
			psmt = con.prepareStatement(query);
			psmt.setString(1, num);
			rs = psmt.executeQuery();

			if (rs.next()) {
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("regdate"));
				dto.setVisitcount(rs.getString("readcount"));
				dto.setName(rs.getString("name")); // MEMBER 테이블에서 가져온 NAME
			}
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생");
			e.printStackTrace();
		}

		return dto;
	}

	// --- 상세 보기 기능 추가 (End) ---
}