package controller.board;

import java.io.IOException;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경

import board.BoardDAO;
import board.BoardDTO;

public class ViewController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 게시물 번호(num)와 게시판 타입(boardType) 받기
		String num = req.getParameter("num");
		String boardType = req.getParameter("boardType");

		if (boardType == null || boardType.isEmpty()) {
			boardType = "free";
		}

		// 2. DAO를 통해 게시물 조회수 증가
		BoardDAO dao = new BoardDAO(req.getServletContext(), boardType);
		dao.updateVisitCount(num);

		// 3. DAO를 통해 게시물 상세 내용 가져오기
		BoardDTO dto = dao.selectView(num);
		dao.close();

		// 4. 게시판 이름 설정
		String boardName = "";
		if ("free".equals(boardType)) {
			boardName = "자유 게시판";
		} else if ("qna".equals(boardType)) {
			boardName = "Q&A 게시판";
		} else if ("data".equals(boardType)) {
			boardName = "자료실 게시판";
		}

		// 5. request 영역에 저장 후 view.jsp로 포워드
		req.setAttribute("dto", dto);
		req.setAttribute("boardType", boardType);
		req.setAttribute("boardName", boardName);

		req.getRequestDispatcher("/view/02board/view.jsp").forward(req, resp);
	}
}