package board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BoardWriteAction {

    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        HttpSession session = request.getSession();
        String writer = (String) session.getAttribute("userID");
        
        if (writer == null) {
            String type = request.getParameter("type"); 
            return "redirect:" + request.getContextPath() + "/member/loginform.do?returnUrl=" 
                    + request.getContextPath() + "/board/writeform.do?type=" + type;
        }

        String type = request.getParameter("type");
        String subject = request.getParameter("subject");
        String content = request.getParameter("content");
        
        BoardDTO dto = new BoardDTO();
        dto.setWriter(writer); 
        dto.setType(type);
        dto.setSubject(subject);
        dto.setContent(content);
        
        BoardDAO bdao = new BoardDAO();
        int result = bdao.insertBoard(dto);
        
        String viewPage = null;
        
        if (result == 1) {
            viewPage = "redirect:" + request.getContextPath() + "/board/list.do?type=" + type;
        } else {
            request.setAttribute("msg", "게시글 작성 중 오류가 발생했습니다.");
            request.setAttribute("board", dto);
            viewPage = "/board/writeForm.jsp"; 
        }

        return viewPage;
    }
}