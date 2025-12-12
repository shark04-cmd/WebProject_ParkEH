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
	protected void service(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		HttpSession session = req.getSession();

		String userName = (String) session.getAttribute("UserName");
		if (userName != null) {
			session.setAttribute("LogoutSuccessMessage"
					, userName + "님, 로그아웃 되셨습니다!");
		}
		resp.sendRedirect(req.getContextPath() + "/Default.jsp");
	}
}