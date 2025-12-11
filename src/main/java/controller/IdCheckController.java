package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import member.MemberDAO;

public class IdCheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// JSP에서 AJAX로 POST 요청을 보내므로 doPost를 구현해야 합니다.
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json;charset=UTF-8");

		String id = req.getParameter("id");
		boolean isDuplicated = true;
		String message = "";

		if (id == null || id.trim().isEmpty() || id.length() < 4) {
			message = "아이디를 4자 이상 입력해 주세요.";
			isDuplicated = true;
		} else {
			MemberDAO dao = new MemberDAO(req.getServletContext());
			isDuplicated = dao.isIdDuplicate(id);
			dao.close();

			if (isDuplicated) {
				message = "이미 사용 중인 아이디입니다.";
			} else {
				message = "사용 가능한 아이디입니다.";
			}
		}

		// JSON 형식으로 응답 생성
		String jsonResponse = String.format("{\"isDuplicated\": %b, \"message\": \"%s\"}", isDuplicated, message);

		PrintWriter out = resp.getWriter();
		out.print(jsonResponse);
		out.flush();
	}

	// doGet 요청이 올 경우를 대비하여 doPost로 포워딩
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}
}