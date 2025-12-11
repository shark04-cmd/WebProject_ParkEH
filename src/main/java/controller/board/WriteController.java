package controller.board;

import java.io.IOException;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpSession; // ⬅️ jakarta로 변경

import board.BoardDAO;
import board.BoardDTO;
import member.MemberDAO;
import member.MemberDTO;

public class WriteController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 로그인 확인
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("UserID");

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
			return;
		}

		// 2. 게시판 타입 설정
		String boardType = req.getParameter("boardType");
		if (boardType == null || boardType.isEmpty()) {
			boardType = "free";
		}

		// 3. 폼 페이지로 이동
		req.setAttribute("boardType", boardType);
		req.getRequestDispatcher("/view/02board/write.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("UserID");

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
			return;
		}

		// 1. 폼값 받기
		String boardType = req.getParameter("boardType");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String fileName = req.getParameter("fileName");

		// 2. DTO에 저장
		BoardDTO dto = new BoardDTO();
		dto.setBoardType(boardType);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setId(userId);
		dto.setFileName(fileName);

		// 3. 이름(name) 가져오기
		MemberDAO mDao = new MemberDAO(req.getServletContext());
		MemberDTO mDto = mDao.getMemberDTO(userId);
		mDao.close();

		if (mDto != null) {
			dto.setName(mDto.getName());
		} else {
			req.setAttribute("errorMessage", "작성자 정보를 찾을 수 없습니다. 다시 로그인해 주세요.");
			req.setAttribute("boardType", boardType);
			req.getRequestDispatcher("/view/02board/write.jsp").forward(req, resp);
			return;
		}

		// 4. DAO를 통해 DB에 저장
		BoardDAO bDao = new BoardDAO(req.getServletContext(), boardType);
		int result = bDao.insertWrite(dto);
		bDao.close();

		// 5. 결과 처리
		if (result == 1) {
			resp.sendRedirect(req.getContextPath() + "/board/list.do?boardType=" + boardType);
		} else {
			req.setAttribute("errorMessage", "게시물 등록에 실패했습니다. 관리자에게 문의하세요.");
			req.setAttribute("boardType", boardType);
			req.getRequestDispatcher("/view/02board/write.jsp").forward(req, resp);
		}
	}
}