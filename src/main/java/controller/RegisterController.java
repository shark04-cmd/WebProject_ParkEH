package controller;

import java.io.IOException;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경

import member.MemberDAO;
import member.MemberDTO;

public class RegisterController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// GET 요청 시 회원가입 폼으로 이동
		req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		// 1. 폼 값 받기
		String userId = req.getParameter("id");
		String userPass = req.getParameter("pass");
		String confirmPass = req.getParameter("confirmPass");
		String name = req.getParameter("name");

		// 2. 유효성 검사
		if (!userPass.equals(confirmPass)) {
			req.setAttribute("RegisterErrorMessage", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
			return;
		}

		// 3. DTO에 저장
		MemberDTO dto = new MemberDTO();
		dto.setId(userId);
		dto.setPass(userPass);
		dto.setName(name);

		// 4. DAO를 통해 회원 가입 처리
		MemberDAO dao = new MemberDAO(req.getServletContext());
		int result = dao.insertMember(dto);
		dao.close();

		// 5. 결과 처리
		if (result == 1) {
			// 성공 시 성공 페이지로 이동
			req.setAttribute("SuccessMessage", name + "님, 회원가입이 완료되었습니다!");
			req.setAttribute("LinkUrl", req.getContextPath() + "/member/login.do");
			req.setAttribute("LinkText", "로그인 하러 가기");
			req.getRequestDispatcher("/view/01member/Success.jsp").forward(req, resp);
		} else {
			// 실패 시 (ID 중복 등)
			req.setAttribute("RegisterErrorMessage", "회원가입에 실패했습니다. 아이디 중복 또는 관리자에게 문의하세요.");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
		}
	}
}