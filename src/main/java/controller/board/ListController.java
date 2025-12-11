package controller.board;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import board.BoardDAO;
import board.BoardDTO;
import common.BoardPage;

public class ListController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 1. 게시판 타입 설정
		String boardType = req.getParameter("boardType");
		if (boardType == null || boardType.isEmpty()) {
			boardType = "free";
		}

		// 2. DAO 객체 생성 및 맵 설정
		ServletContext application = this.getServletContext();
		// boardType에 따라 DAO가 사용할 테이블 이름(board_free, board_qna, board_data)이 결정됩니다.
		BoardDAO dao = new BoardDAO(application, boardType);

		Map<String, Object> map = new HashMap<String, Object>();

		// 3. 검색 조건 처리
		String searchField = req.getParameter("searchField");
		String searchWord = req.getParameter("searchWord");
		if (searchWord != null && !searchWord.trim().isEmpty()) {
			map.put("searchField", searchField);
			map.put("searchWord", searchWord);
		}

		// 4. 게시물 수 카운트
		int totalCount = dao.selectCount(map);

		// 5. 페이지 처리 설정
		ServletContext context = getServletContext();
		int pageSize = Integer.parseInt(context.getInitParameter("POSTS_PER_PAGE"));
		int blockPage = Integer.parseInt(context.getInitParameter("PAGES_PER_BLOCK"));

		int pageNum = 1;
		String pageTemp = req.getParameter("pageNum");
		if (pageTemp != null && !pageTemp.isEmpty()) {
			pageNum = Integer.parseInt(pageTemp);
		}

		int start = (pageNum - 1) * pageSize + 1;
		int end = pageNum * pageSize;
		map.put("start", start);
		map.put("end", end);

		// 6. 페이지에 해당하는 게시물 목록 조회
		List<BoardDTO> boardList = dao.selectListPaging(map);
		dao.close();

		// 7. 뷰에 전달할 매개변수 추가
		String boardName = "";
		if ("free".equals(boardType)) {
			boardName = "자유 게시판";
		} else if ("qna".equals(boardType)) {
			boardName = "Q&A 게시판";
		} else if ("data".equals(boardType)) {
			boardName = "자료실 게시판";
		}
		map.put("boardType", boardType);
		map.put("boardName", boardName);
		map.put("totalCount", totalCount);

		// 8. 페이징 문자열 생성
		String pagingStr = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum,
				req.getContextPath() + "/board/list.do?boardType=" + boardType);
		map.put("pagingStr", pagingStr);

		// 9. request 영역에 저장 후 포워드
		req.setAttribute("boardList", boardList);
		req.setAttribute("map", map);
		req.getRequestDispatcher("/view/02board/list.jsp").forward(req, resp);
	}
}