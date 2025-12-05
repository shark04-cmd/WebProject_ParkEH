package board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class BoardContentAction {

	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String sNum = request.getParameter("num");
		int num = 0;
		if (sNum != null) {
			try {
				num = Integer.parseInt(sNum);
			} catch (NumberFormatException e) {
				num = 0;
			}
		}

		String pageNum = request.getParameter("pageNum");
		if (pageNum == null) pageNum = "1";

		if (num == 0) {
			request.setAttribute("msg", "잘못된 접근입니다.");
			return "redirect:" + request.getContextPath() + "/board/list.do?type=free";
		}

		BoardDAO bdao = new BoardDAO();

		// 1. 조회수 증가
		bdao.updateReadCount(num);

		// 2. 게시글 상세 정보 조회
		BoardDTO board = bdao.getBoard(num);

		if (board == null) {
			request.setAttribute("msg", "해당 게시글이 존재하지 않습니다.");
			return "redirect:" + request.getContextPath() + "/board/list.do?type=free";
		}

		request.setAttribute("board", board);
		request.setAttribute("pageNum", pageNum);
		
		return "/board/content.jsp";
	}
}