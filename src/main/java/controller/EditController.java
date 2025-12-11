package controller;

import java.io.IOException;
import jakarta.servlet.ServletException; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServlet; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletRequest; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpServletResponse; // ⬅️ jakarta로 변경
import jakarta.servlet.http.HttpSession; // ⬅️ jakarta로 변경

import member.MemberDAO;
import member.MemberDTO;

public class EditController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 세션에서 ID 가져오기 (로그인 여부 확인)
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("UserID");

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
			return;
		}

		// 2. DAO를 통해 기존 정보 조회
		MemberDAO dao = new MemberDAO(req.getServletContext());
		MemberDTO dto = dao.getMemberDTO(userId);
		dao.close();

		// 3. request 영역에 저장 후 폼 페이지로 이동
		req.setAttribute("dto", dto);
		req.getRequestDispatcher("/view/01member/edit.jsp").forward(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("UserID");

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
			return;
		}

		// 1. 폼 값 받기
		String currentPass = req.getParameter("pass");
		String newPass = req.getParameter("newPass");
		String confirmPass = req.getParameter("confirmPass");
		String name = req.getParameter("name");

		// 2. DTO에 저장
		MemberDTO dto = new MemberDTO();
		dto.setId(userId);
		dto.setPass(currentPass); // 현재 비밀번호 (인증용)
		dto.setName(name);

		// 새 비밀번호 유효성 검사
		if (newPass != null && !newPass.isEmpty()) {
			if (!newPass.equals(confirmPass)) {
				// 비밀번호 불일치 에러
				req.setAttribute("EditErrorMessage", "새 비밀번호와 확인이 일치하지 않습니다.");
				// 기존 이름은 다시 세팅 (수정폼 재출력용)
				dto.setName(name);
				req.setAttribute("dto", dto);
				req.getRequestDispatcher("/view/01member/edit.jsp").forward(req, resp);
				return;
			}
			dto.setNewPass(newPass); // 새 비밀번호 DTO에 설정
		} else {
			dto.setNewPass(null); // 비밀번호 수정하지 않음
		}

		// 3. DAO를 통해 수정 처리
		MemberDAO dao = new MemberDAO(req.getServletContext());
		int result = dao.updateMember(dto);
		dao.close();

		// 4. 결과 처리
		if (result == 1) {
			// 수정 성공
			session.setAttribute("EditSuccessMessage", "회원 정보가 성공적으로 수정되었습니다.");
			resp.sendRedirect(req.getContextPath() + "/Default.jsp");
		} else {
			// 수정 실패 (현재 비밀번호 불일치 등)
			req.setAttribute("EditErrorMessage", "정보 수정에 실패했습니다. 현재 비밀번호를 확인해 주세요.");
			dto.setName(name); // 기존 이름은 유지
			req.setAttribute("dto", dto);
			req.getRequestDispatcher("/view/01member/edit.jsp").forward(req, resp);
		}
	}
}