package controller;

import java.io.IOException;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpSession; // ⬅️ jakarta로 변경

public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 세션 가져오기
		HttpSession session = req.getSession();

		// 2. 로그아웃 메시지 설정
		String userName = (String) session.getAttribute("UserName");
		if (userName != null) {
			session.setAttribute("LogoutSuccessMessage", userName + "님, 로그아웃 되었습니다.");
		}

		// 3. 세션 무효화
		session.invalidate();

		// 4. 메인 페이지로 리다이렉트
		resp.sendRedirect(req.getContextPath() + "/Default.jsp");
	}
}