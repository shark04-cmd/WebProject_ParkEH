package member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class MemberUpdateAction {

	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		HttpSession session = request.getSession();
		String id = (String) session.getAttribute("userID");
		
		if (id == null) {
			return "redirect:" + request.getContextPath() + "/member/loginform.do";
		}
		
		String pass = request.getParameter("pass");
		String pass2 = request.getParameter("pass2");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String phoneNum = request.getParameter("phoneNum");
		
		if (pass == null || pass2 == null || !pass.equals(pass2)) {
			request.setAttribute("msg", "비밀번호 확인이 일치하지 않거나 비밀번호가 누락되었습니다.");
			
			MemberDTO tempMember = new MemberDTO();
			tempMember.setId(id);
			tempMember.setPass(pass);
			tempMember.setName(name);
			tempMember.setEmail(email);
			tempMember.setPhoneNum(phoneNum);
			request.setAttribute("member", tempMember);
			
			return "/member/memberUpdateForm.jsp";
		}
		
		MemberDAO mdao = new MemberDAO();
		MemberDTO member = new MemberDTO();
		
		member.setId(id);
		member.setPass(pass);
		member.setName(name);
		member.setEmail(email);
		member.setPhoneNum(phoneNum);
		
		int result = mdao.updateMember(member);
		
		String viewPage = null;
		
		if (result == 1) {
			session.setAttribute("userName", member.getName());
			viewPage = "redirect:" + request.getContextPath() + "/main.do";
		} else {
			request.setAttribute("msg", "회원 정보 수정 중 DB 오류가 발생했습니다. 다시 시도해 주세요.");
			request.setAttribute("member", member); 
			viewPage = "/member/memberUpdateForm.jsp";
		}

		return viewPage;
	}
}
