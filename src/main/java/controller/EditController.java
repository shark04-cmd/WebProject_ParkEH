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

	// 회원 정보 수정 폼으로 이동 (GET 요청)
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("UserID");

		// 1. 로그인 체크
		if (userId == null || userId.isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
			return;
		}

		// 2. DB에서 현재 회원 정보 가져오기
		MemberDAO dao = new MemberDAO(getServletContext());
		MemberDTO memberInfo = dao.getMemberDTOById(userId);
		dao.close();

		// 3. 정보가 있다면 request에 저장하고 폼 View로 포워딩
		if (memberInfo != null) {
			req.setAttribute("memberInfo", memberInfo);
			// View 경로로 포워딩
			req.getRequestDispatcher("/view/01member/edit.jsp").forward(req, resp);
		} else {
			// 정보 조회 실패 시 (세션에 아이디는 있는데 DB에 없는 경우)
			session.invalidate();
			resp.sendRedirect(req.getContextPath() + "/member/login.do?error=invalid_session");
		}
	}

	// 회원 정보 수정 처리 (POST 요청)
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");

		HttpSession session = req.getSession();
		String userId = (String) session.getAttribute("UserID");

		// 1. 로그인 체크 (서버 측 검증)
		if (userId == null || userId.isEmpty()) {
			resp.sendRedirect(req.getContextPath() + "/member/login.do");
			return;
		}

		// 2. 폼 데이터 받기
		String currentPass = req.getParameter("currentPass"); // 현재 패스워드
		String newPass = req.getParameter("pass"); // 새 패스워드 (공백일 수 있음)
		String name = req.getParameter("name");
		String email = req.getParameter("email");
		String phone = req.getParameter("phone");

		// 3. 현재 패스워드 일치 여부 확인 (본인 확인)
		MemberDAO dao = new MemberDAO(getServletContext());
		MemberDTO confirmedDTO = dao.getMemberDTO(userId, currentPass); // ID와 현재 PASS로 조회

		if (confirmedDTO == null) {
			// 현재 패스워드가 일치하지 않는 경우

			// 3-1. 입력된 나머지 정보들을 다시 폼에 채우기 위해 DTO에 설정 (pass는 제외)
			MemberDTO reinputDTO = new MemberDTO();
			reinputDTO.setId(userId);
			reinputDTO.setName(name);
			reinputDTO.setEmail(email);
			reinputDTO.setPhone(phone);
			// DB에서 가져온 기존 비밀번호를 설정 (비밀번호 필드는 JSP에서 빈 값으로 보여줌)
			reinputDTO.setPass(dao.getMemberDTOById(userId).getPass());

			dao.close(); // DAO 닫기

			req.setAttribute("memberInfo", reinputDTO);
			req.setAttribute("EditErrorMessage", "현재 비밀번호가 일치하지 않아 정보 수정에 실패했습니다.");
			req.getRequestDispatcher("/view/01member/edit.jsp").forward(req, resp);
			return; // 처리 중단
		}

		// 4. 업데이트할 DTO 설정
		MemberDTO updateDTO = new MemberDTO();
		updateDTO.setId(userId);
		updateDTO.setPass(newPass); // 변경할 패스워드 (공백이면 DAO에서 기존 값 유지)
		updateDTO.setName(name);
		updateDTO.setEmail(email);
		updateDTO.setPhone(phone);

		// 5. DB 업데이트
		int result = dao.updateMember(updateDTO);
		dao.close();

		// 6. 결과 처리
		if (result == 1) {
			// 수정 성공 시
			resp.sendRedirect(req.getContextPath() + "/member/edit.do?editSuccess=true");
		} else {
			// 수정 실패 시 (DB 오류 등)
			req.setAttribute("memberInfo", confirmedDTO); // 기존 정보 또는 입력받은 정보로 대체 가능
			req.setAttribute("EditErrorMessage", "정보 수정 중 DB 오류가 발생했습니다. 다시 시도해 주세요.");
			req.getRequestDispatcher("/view/01member/edit.jsp").forward(req, resp);
		}
	}
}