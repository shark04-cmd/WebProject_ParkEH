package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경

import member.MemberDAO;

public class IdCheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 파라미터로 ID 받기
		String id = req.getParameter("id");

		// 응답 설정
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");

		PrintWriter out = resp.getWriter();

		// 유효성 검사 (길이 등)
		if (id == null || id.length() < 4) {
			out.print("INVALID"); // ID가 너무 짧음
			return;
		}

		// DAO를 통해 중복 확인
		MemberDAO dao = new MemberDAO(req.getServletContext());
		boolean isDuplicate = dao.isIdDuplicate(id);
		dao.close();

		if (isDuplicate) {
			out.print("DUPLICATE");
		} else {
			out.print("AVAILABLE");
		}
	}
}