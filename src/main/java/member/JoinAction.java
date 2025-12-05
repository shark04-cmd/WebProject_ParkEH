package member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JoinAction { 
    
    public String execute(HttpServletRequest request, HttpServletResponse response) 
    		throws Exception {
        
        String id = request.getParameter("id");
        String pass = request.getParameter("pass");
        String pass2 = request.getParameter("pass2");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNum = request.getParameter("phoneNum");
        
        // 1. 비밀번호 일치 확인
        if (!pass.equals(pass2)) {
            request.setAttribute("msg", "비밀번호 확인이 일치하지 않습니다.");
            MemberDTO tempMember = new MemberDTO();
            tempMember.setId(id);
            tempMember.setName(name);
            tempMember.setEmail(email);
            tempMember.setPhoneNum(phoneNum);
            request.setAttribute("member", tempMember);
            
            return "/member/joinForm.jsp";
        }
        
        MemberDAO mdao = new MemberDAO();
        
        // 2. 아이디 중복 확인
        if (mdao.getMember(id) != null) {
            request.setAttribute("msg", "이미 존재하는 아이디입니다.");
            return "/member/joinForm.jsp";
        }
        
        // 3. DB 삽입
        MemberDTO member = new MemberDTO();
        member.setId(id);
        member.setPass(pass);
        member.setName(name);
        member.setEmail(email);
        member.setPhoneNum(phoneNum);
        
        int result = mdao.insertMember(member);
        
        String viewPage = null; 
        
        if (result == 1) {
            // 성공 시 로그인 폼으로 리다이렉트
            viewPage = "redirect:" + request.getContextPath() + "/member/loginForm.do"; 
        } else {
            request.setAttribute("msg", "회원가입 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.");
            viewPage = "/member/joinForm.jsp";
        }

        return viewPage;
    }
}
