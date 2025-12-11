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

public class EditController extends HttpServlet {
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

		// 2. 파라미터 받기 및 DAO 생성
		String num = req.getParameter("num");
		String boardType = req.getParameter("boardType");
		if (boardType == null || boardType.isEmpty()) {
			boardType = "free";
		}

		BoardDAO dao = new BoardDAO(req.getServletContext(), boardType);
		BoardDTO dto = dao.selectView(num);
		dao.close();

		// 3. 권한 확인
		if (dto == null || !userId.equals(dto.getId())) {
			resp.sendRedirect(req.getContextPath() + "/board/view.do?boardType=" + boardType + "&num=" + num);
			return;
		}

		// 4. request 영역에 저장 후 폼 페이지로 이동
		req.setAttribute("dto", dto);
		req.setAttribute("boardType", boardType);

		req.getRequestDispatcher("/view/02board/edit.jsp").forward(req, resp);
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
		String num = req.getParameter("num");
		String boardType = req.getParameter("boardType");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		String fileName = req.getParameter("fileName");

		// 2. DTO에 저장
		BoardDTO dto = new BoardDTO();
		dto.setNum(num);
		dto.setBoardType(boardType);
		dto.setTitle(title);
		dto.setContent(content);
		dto.setId(userId);
		dto.setFileName(fileName);

		// 3. 이름 가져오기
		MemberDAO mDao = new MemberDAO(req.getServletContext());
		MemberDTO mDto = mDao.getMemberDTO(userId);
		mDao.close();

		if (mDto != null) {
			dto.setName(mDto.getName());
		}

		// 4. DAO를 통해 DB에 수정
		BoardDAO bDao = new BoardDAO(req.getServletContext(), boardType);
		int result = bDao.updateEdit(dto);
		bDao.close();

		// 5. 결과 처리
		if (result == 1) {
			resp.sendRedirect(req.getContextPath() + "/board/view.do?boardType=" + boardType + "&num=" + num);
		} else {
			dto.setName(mDto.getName());

			req.setAttribute("EditErrorMessage", "게시물 수정에 실패했거나 권한이 없습니다.");
			req.setAttribute("dto", dto);
			req.setAttribute("boardType", boardType);
			req.getRequestDispatcher("/view/02board/edit.jsp").forward(req, resp);
		}
	}
}