package board;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

public class BoardListAction {
    
    public String execute(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String type = request.getParameter("type");
        if (type == null || type.isEmpty()) {
            type = "free"; 
        }

        String search = request.getParameter("search");
        
        BoardDAO bdao = new BoardDAO();
        List<BoardDTO> boardList = bdao.getBoardList(type, search);
        
        request.setAttribute("boardType", type);
        request.setAttribute("boardList", boardList);
        
        return "/member/main.jsp"; 
    }
}