package controller;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.MemberDAO;

// AJAX 요청을 받아 ID 중복 여부를 JSON으로 반환합니다.
public class IdCheckController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 요청 파라미터 받기 (확인할 아이디)
		String id = req.getParameter("id");

		// 2. DAO를 통해 DB에서 아이디 중복 확인
		MemberDAO dao = new MemberDAO(getServletContext());
		// idCheck()는 중복이면 true, 사용 가능이면 false를 반환합니다.
		boolean isDuplicated = dao.idCheck(id);
		dao.close();

		// 3. 결과를 클라이언트로 응답 (JSON 형식)
		resp.setContentType("application/json;charset=UTF-8");
		PrintWriter out = resp.getWriter();

		String responseJson;
		if (isDuplicated) {
			// 중복된 경우: isDuplicated: true
			responseJson = "{\"isDuplicated\": true, \"message\": \"아이디가 중복됐습니다\"}";
		} else {
			// 사용 가능한 경우: isDuplicated: false
			responseJson = "{\"isDuplicated\": false, \"message\": \"사용 가능한 아이디입니다\"}";
		}

		out.print(responseJson);
		out.flush();
	}
}