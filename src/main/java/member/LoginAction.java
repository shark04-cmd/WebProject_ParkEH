package member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginAction {  
    
    public String execute(HttpServletRequest request, HttpServletResponse response)  
    		throws Exception {
        
        String id = request.getParameter("id");
        String pass = request.getParameter("pass");
        
        MemberDAO mdao = new MemberDAO();
        MemberDTO member = mdao.getMember(id);
        
        String viewPage = null; 
        
        if (member == null) {
            // ID가 존재하지 않음
            request.setAttribute("msg", "존재하지 않는 아이디입니다.");
            viewPage = "/member/loginForm.jsp"; 
            
        } else if (!member.getPass().equals(pass)) {
            // 비밀번호 불일치
            request.setAttribute("msg", "비밀번호가 일치하지 않습니다.");
            viewPage = "/member/loginForm.jsp";
            
        } else {
            // 로그인 성공!
            HttpSession session = request.getSession();
            session.setAttribute("userID", member.getId());
            session.setAttribute("userName", member.getName());
            
            
            // 쿠키 처리 (아이디 저장)
            String saveId = request.getParameter("saveid");
            
            if (saveId != null && saveId.equals("true")) {
                Cookie idCookie = new Cookie("savedID", id);
                idCookie.setMaxAge(60 * 60 * 24 * 30);  
                idCookie.setPath("/");  
                response.addCookie(idCookie);
            } else {
                // 기존 쿠키 삭제
                Cookie[] cookies = request.getCookies();
                if (cookies != null) {
                    for (Cookie c : cookies) {
                        if (c.getName().equals("savedID")) {
                            c.setMaxAge(0); 
                            c.setPath("/");
                            response.addCookie(c);
                            break;
                        }
                    }
                }
            }
            
            // 메인 페이지로 리다이렉트
            viewPage = "redirect:" + request.getContextPath() + "/main.do"; 
        }

        return viewPage;
    }
}