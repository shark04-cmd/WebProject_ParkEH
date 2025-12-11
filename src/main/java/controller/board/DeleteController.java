package controller.board;

import java.io.IOException;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpSession; // ⬅️ jakarta로 변경

import board.BoardDAO;
import board.BoardDTO;

public class DeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 세션에서 로그인 ID 확인
		HttpSession session = req.getSession();
		String sessionId = (String) session.getAttribute("UserID");

		// 2. 게시물 번호(num)와 게시판 타입(boardType) 받기
		String num = req.getParameter("num");
		String boardType = req.getParameter("boardType");

		if (boardType == null || boardType.isEmpty()) {
			boardType = "free";
		}

		BoardDAO dao = new BoardDAO(req.getServletContext(), boardType);
		BoardDTO dto = dao.selectView(num);

		// 3. 권한 확인: 로그인 ID와 게시물 작성자 ID가 일치하는지 확인
		if (sessionId == null || dto == null || !sessionId.equals(dto.getId())) {
			dao.close();
			resp.sendRedirect(req.getContextPath() + "/board/view.do?boardType=" + boardType + "&num=" + num);
			return;
		}

		// 4. 게시물 삭제
		int result = dao.deletePost(num);
		dao.close();

		// 5. 결과에 따른 리다이렉트
		if (result == 1) {
			resp.sendRedirect(req.getContextPath() + "/board/list.do?boardType=" + boardType);
		} else {
			resp.sendRedirect(req.getContextPath() + "/board/view.do?boardType=" + boardType + "&num=" + num);
		}
	}
}