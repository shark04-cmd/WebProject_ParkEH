package member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutAction {

    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        // 세션 가져오기 (세션이 없으면 새로 만들지 않음)
        HttpSession session = request.getSession(false); 
        
        if (session != null) {
            // 세션의 모든 속성 제거 (로그아웃 처리)
            session.invalidate(); 
        }
        
        // 로그아웃 후 메인 페이지로 리다이렉트
        return "redirect:" + request.getContextPath() + "/main.do";
    }
}