package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // ⚠️ 추가: 세션 사용을 위해 import

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
		String id = req.getParameter("id");
		String pass = req.getParameter("pass");
		String passConfirm = req.getParameter("passConfirm"); // ⚠️ 수정: JSP의 새로운 필드 이름으로 변경
		String name = req.getParameter("name");
		String email = req.getParameter("email"); // ⚠️ 추가: email 파라미터 받기
		String phone = req.getParameter("phone"); // ⚠️ 추가: phone 파라미터 받기

		MemberDAO dao = new MemberDAO(req.getServletContext());

		// 오류 발생 시 입력 값 유지를 위한 헬퍼 함수
		// DTO 사용 대신, request 속성을 사용합니다.
		req.setAttribute("id", id);
		req.setAttribute("name", name);
		req.setAttribute("email", email);
		req.setAttribute("phone", phone);

		// 2. 비밀번호 일치 검증 (가장 먼저 실행)
		if (passConfirm == null || !pass.equals(passConfirm)) {
			dao.close();
			req.setAttribute("RegisterErrorMessage", "비밀번호와 비밀번호 확인이 일치하지 않습니다.");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
			return;
		}

		// 3. 아이디 중복 검사 (AJAX로 했지만, 서버에서 최종 검사)
		if (dao.isIdDuplicate(id)) {
			dao.close();
			req.setAttribute("RegisterErrorMessage", "이미 사용 중인 아이디입니다.");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
			return;
		}

		// 4. DTO에 저장
		MemberDTO dto = new MemberDTO();
		dto.setId(id);
		dto.setPass(pass);
		dto.setName(name);
		dto.setEmail(email); // ⚠️ 추가: email 설정
		dto.setPhone(phone); // ⚠️ 추가: phone 설정

		// 5. DB에 등록
		int result = dao.insertMember(dto);
		dao.close();

		// 6. 결과 처리
		if (result == 1) {
			// 회원가입 성공 후 로그인 페이지로 리다이렉트하며 메시지 전달
			HttpSession session = req.getSession();
			session.setAttribute("LoginSuccessMessage", dto.getName() + "님, 회원가입을 축하합니다! 로그인 해주세요.");
			resp.sendRedirect(req.getContextPath() + "/member/login.do");

		} else {
			// 등록 실패 (DB 오류 등)
			req.setAttribute("RegisterErrorMessage", "회원가입 중 오류가 발생했습니다. 관리자에게 문의하세요.");
			req.getRequestDispatcher("/view/01member/register.jsp").forward(req, resp);
		}
	}
}