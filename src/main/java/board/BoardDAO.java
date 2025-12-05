package board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	private Connection getConnection() throws Exception {
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

	// 1. 게시글 목록 조회 메서드
	public List<BoardDTO> getBoardList(String type, String search) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardDTO> boardList = new ArrayList<>();

		String searchCondition = (search != null && !search.isEmpty()) ? " AND subject LIKE ? " : "";

		String sql = "SELECT * FROM board WHERE type = ? " + searchCondition + " ORDER BY num DESC";

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, type);

			if (searchCondition.length() > 0) {
				pstmt.setString(2, "%" + search + "%");
			}

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardDTO dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setWriter(rs.getString("writer"));
				dto.setSubject(rs.getString("subject"));
				dto.setReadcount(rs.getInt("readcount"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setType(rs.getString("type"));
				boardList.add(dto);
			}
		} catch (Exception e) {
			System.err.println("게시판 목록 조회 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(con, pstmt, rs);
		}
		return boardList;
	}

	// 2. 게시글 저장 메서드 (Oracle 시퀀스 사용)
	public int insertBoard(BoardDTO dto) {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "INSERT INTO board (num, writer, subject, content, type, regdate, readcount) "
					+ "VALUES (board_seq.NEXTVAL, ?, ?, ?, ?, SYSDATE, 0)";

		try {
			con = getConnection();

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getWriter());
			pstmt.setString(2, dto.getSubject());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getType());

			result = pstmt.executeUpdate();

		} catch (Exception e) {
			System.err.println("게시글 삽입 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(con, pstmt, null);
		}
		return result;
	}

	// 3. 게시글 조회수 증가 (★★★ 이 메서드가 누락되었을 수 있습니다 ★★★)
	public void updateReadCount(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;

		String sql = "UPDATE board SET readcount = readcount + 1 WHERE num = ?";

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.err.println("조회수 증가 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(con, pstmt, null);
		}
	}

	// 4. 게시글 상세 정보 조회
	public BoardDTO getBoard(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardDTO dto = null;

		String sql = "SELECT * FROM board WHERE num = ?";

		try {
			con = getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new BoardDTO();
				dto.setNum(rs.getInt("num"));
				dto.setWriter(rs.getString("writer"));
				dto.setSubject(rs.getString("subject"));
				dto.setContent(rs.getString("content"));
				dto.setReadcount(rs.getInt("readcount"));
				dto.setRegdate(rs.getTimestamp("regdate"));
				dto.setType(rs.getString("type"));
			}
		} catch (Exception e) {
			System.err.println("게시글 조회 중 예외 발생: " + e.getMessage());
			e.printStackTrace();
		} finally {
			closeResources(con, pstmt, rs);
		}
		return dto;
	}
}