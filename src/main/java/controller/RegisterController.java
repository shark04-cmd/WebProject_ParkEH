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
		req.setCharacterEncoding("UTF-8");

		// 1. 요청 파라미터 받기 및 DTO 설정
		MemberDTO dto = new MemberDTO();
		String id = req.getParameter("id"); // ID 값 미리 저장
		dto.setId(id);
		dto.setPass(req.getParameter("pass"));
		dto.setName(req.getParameter("name"));
		dto.setEmail(req.getParameter("email"));
		dto.setPhone(req.getParameter("phone"));

		MemberDAO dao = new MemberDAO(getServletContext());

		// 2-1. 서버 측 아이디 중복 최종 확인
		boolean isDuplicated = dao.idCheck(id);

		if (isDuplicated) {
			// 아이디가 중복되면 회원가입 실패 처리
			dao.close();
			// 요청하신 실패 메시지
			req.setAttribute("RegisterErrorMessage", "회원가입에 실패하였습니다");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
			return; // 중복되면 여기서 요청 처리를 중단하고 회원가입 창으로 이동
		}

		// 2-2. 정상적인 회원 정보 삽입
		int result = dao.insertMember(dto);
		dao.close();

		// 3. 결과 처리
		if (result == 1) {
			// 회원가입 성공 시: 세션에 메시지를 저장하고 로그인 페이지로 리다이렉트
			// 세션에 저장된 메시지는 로그인 페이지에서 한번만 표시됩니다.
			req.getSession().setAttribute("LoginSuccessMessage", "회원가입 되셨습니다!");

			// 로그인 페이지로 리다이렉트 (경로 수정 없음)
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
		} else {
			// 회원가입 실패 시 (DB 오류 등)
			req.setAttribute("RegisterErrorMessage", "회원가입에 실패했습니다. 다시 시도해 주세요.");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
		}
	}
}