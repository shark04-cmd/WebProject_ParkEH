package member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MainAction {
    
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        // 메인 페이지 요청 시, 기본 게시판 목록(/board/list.do?type=free)으로 리다이렉트
        return "redirect:" + request.getContextPath() + "/board/list.do?type=free";
    }
}