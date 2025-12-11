package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import member.MemberDAO;
import member.MemberDTO;

public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 회원가입 폼으로 이동 (GET 요청)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// /member/register.do 요청 시 View 경로로 포워딩
		req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
	}

	// 회원가입 처리 (POST 요청)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 요청 파라미터 받기 (아이디, 패스워드, 이름, 이메일, 전화번호)
		MemberDTO dto = new MemberDTO();
		dto.setId(req.getParameter("id"));
		dto.setPass(req.getParameter("pass"));
		dto.setName(req.getParameter("name"));
		dto.setEmail(req.getParameter("email"));
		dto.setPhone(req.getParameter("phone"));

		// 2. DAO를 통해 DB에 회원 정보 삽입
		MemberDAO dao = new MemberDAO(getServletContext());
		int result = dao.insertMember(dto);
		dao.close(); // DB 자원 반납

		// 3. 결과 처리
		if (result == 1) {
			// 회원가입 성공 시 로그인 페이지로 리다이렉트
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
		} else {
			// 회원가입 실패 시 (예: 아이디 중복)
			req.setAttribute("RegisterErrorMessage", "회원가입에 실패했습니다. (아이디가 이미 존재하거나 DB 오류)");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
		}
	}
}