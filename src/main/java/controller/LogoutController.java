package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 세션 가져오기 (세션이 없으면 새로 만들지 않음)
		HttpSession session = req.getSession(false);

		// 2. 세션이 존재하면 메시지 저장 및 무효화
		if (session != null) {
			// [추가] 로그아웃 성공 메시지를 세션에 저장 (리다이렉트 후에도 유지되도록)
			session.setAttribute("LogoutSuccessMessage", "로그아웃되셨습니다!");

			session.invalidate(); // 세션의 모든 속성 제거 및 세션 무효화 (이후 Default.jsp에서 메시지를 읽기 전에 새 세션이 생성됨)
		}

		// 3. 메인 페이지(Default.jsp)로 리다이렉트
		// Default.jsp에서 메시지를 출력할 때 세션이 이미 초기화되어 새 세션이 생길 수 있으므로,
		// 메시지 저장을 리다이렉트 직전에 수행하도록 합니다.
		resp.sendRedirect(req.getContextPath() + "/Default.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
}