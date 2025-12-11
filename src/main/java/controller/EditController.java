package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import member.MemberDAO;
import member.MemberDTO;

public class EditController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("UserID");

		if (userId == null) {
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
			return;
		}

		MemberDAO dao = new MemberDAO(req.getServletContext());
		MemberDTO dto = dao.getMemberDTO(userId);
		dao.close();

		if (dto == null) {
			resp.sendRedirect(req.getContextPath() + "/member/logout.do");
			return;
		}

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

		String name = req.getParameter("name");
		String pass = req.getParameter("pass"); // 현재 비밀번호 (인증용)
		String newPass = req.getParameter("newPass"); // ⚠️ 수정: 새 비밀번호 파라미터 추가

		// DTO에 저장
		MemberDTO dto = new MemberDTO();
		dto.setId(userId);
		dto.setName(name);
		dto.setPass(pass);
		dto.setNewPass(newPass); // ⚠️ 수정: newPass 설정

		// DAO를 통해 DB에 수정
		MemberDAO dao = new MemberDAO(req.getServletContext());
		int result = dao.updateMember(dto);
		dao.close();

		// 결과 처리
		if (result == 1) {
			session.setAttribute("LoginSuccessMessage", "회원 정보가 성공적으로 수정되었습니다.");
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
		} else {
			req.setAttribute("EditErrorMessage", "정보 수정에 실패했거나 현재 비밀번호가 일치하지 않습니다.");
			dto.setPass(null); // 보안을 위해 현재 비밀번호는 다시 뷰로 보내지 않음
			req.setAttribute("dto", dto);
			req.getRequestDispatcher("/view/01member/edit.jsp").forward(req, resp);
		}
	}
}