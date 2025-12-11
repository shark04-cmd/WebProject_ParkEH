package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import member.MemberDAO;
import member.MemberDTO;

public class LoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 로그인 폼으로 이동 (GET 요청)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// /member/login.do 요청 시 View 경로로 포워딩
		req.getRequestDispatcher("/view/01member/login.jsp").forward(req, resp);
	}

	// 로그인 처리 (POST 요청)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 요청 파라미터 받기
		String id = req.getParameter("id");
		String pass = req.getParameter("pass");

		// 2. DAO를 통해 DB에서 회원 정보 확인
		MemberDAO dao = new MemberDAO(getServletContext());
		MemberDTO dto = dao.getMemberDTO(id, pass);

		// **중요: DAO에서 사용한 Connection을 닫습니다.**
		dao.close();

		// 3. 로그인 처리
		if (dto != null) {
			// 회원 정보가 일치하는 경우 (로그인 성공)

			// 세션에 정보 저장 (Default.jsp에서 사용하기 위해)
			HttpSession session = req.getSession();
			session.setAttribute("UserID", dto.getId());
			session.setAttribute("UserName", dto.getName());

			// Default.jsp로 리다이렉트 (메인 화면)
			resp.sendRedirect(req.getContextPath() + "/Default.jsp");
		} else {
			// 회원 정보가 일치하지 않는 경우 (로그인 실패)

			// 로그인 화면으로 돌아가며 에러 메시지를 전달
			req.setAttribute("LoginErrorMessage", "아이디 또는 비밀번호가 일치하지 않습니다.");
			req.getRequestDispatcher("/view/01member/login.jsp").forward(req, resp);
		}
	}
}