package controller;

import java.io.IOException;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpSession; // ⬅️ jakarta로 변경

import member.MemberDAO;
import member.MemberDTO;

public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// GET 요청 시 로그인 폼으로 이동
		req.getRequestDispatcher("/view/01member/login.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		// 1. 폼 값 받기
		String userId = req.getParameter("id");
		String userPass = req.getParameter("pass");
		String referer = req.getParameter("referer"); // 로그인 후 돌아갈 페이지

		// 2. DAO를 통해 회원 인증
		MemberDAO dao = new MemberDAO(req.getServletContext());
		MemberDTO dto = dao.getMemberDTO(userId, userPass);
		dao.close();

		// 3. 인증 결과 처리
		if (dto != null) {
			// 로그인 성공
			HttpSession session = req.getSession();
			session.setAttribute("UserID", dto.getId());
			session.setAttribute("UserName", dto.getName());

			// 4. 리다이렉트 처리
			if (referer != null && !referer.isEmpty()) {
				resp.sendRedirect(referer);
			} else {
				resp.sendRedirect(req.getContextPath() + "/Default.jsp");
			}
		} else {
			// 로그인 실패
			req.setAttribute("LoginErrorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
			req.getRequestDispatcher("/view/01member/login.jsp").forward(req, resp);
		}
	}
}