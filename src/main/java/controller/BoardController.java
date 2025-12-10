package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dao.BoardDAO;
import dto.BoardDTO;
import utils.BoardPage;
import utils.JSFunction;

public class BoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		process(req, resp);
	}

	private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uri = req.getRequestURI();
		String command = uri.substring(req.getContextPath().length());

		BoardDAO dao = new BoardDAO(getServletContext());

		if (command.equals("/list.do")) {
			list(req, resp, dao);
		} else if (command.equals("/write.do")) {
			if (req.getSession().getAttribute("UserId") == null) {
				JSFunction.alertLocation(resp, "로그인 후 이용 가능합니다.", req.getContextPath() + "/view/member/LoginForm.jsp");
				return;
			}

			if (req.getMethod().equals("GET")) {
				req.getRequestDispatcher("/view/Board/Free/Write.jsp").forward(req, resp);
			} else if (req.getMethod().equals("POST")) {
				write(req, resp, dao);
			}
		}
		// --- 상세 보기 기능 추가 (Start) ---
		else if (command.equals("/view.do")) {
			view(req, resp, dao);
		}
		// --- 상세 보기 기능 추가 (End) ---
		else {
			resp.sendRedirect(req.getContextPath() + "/view/Default.jsp");
		}

		dao.close();
	}

	private void list(HttpServletRequest req, HttpServletResponse resp, BoardDAO dao)
			throws ServletException, IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		String searchField = req.getParameter("searchField");
		String searchWord = req.getParameter("searchWord");

		if (searchWord != null && !searchWord.trim().isEmpty()) {
			map.put("searchField", searchField);
			map.put("searchWord", searchWord);
		}

		int totalCount = dao.selectCount(map);

		ServletContext application = getServletContext();
		int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE"));
		int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));

		int pageNum = 1;
		String pageTemp = req.getParameter("pageNum");
		if (pageTemp != null && !pageTemp.equals("")) {
			pageNum = Integer.parseInt(pageTemp);
		}

		int start = (pageNum - 1) * pageSize + 1;
		int end = pageNum * pageSize;
		map.put("start", start);
		map.put("end", end);

		List<BoardDTO> boardLists = dao.selectListPage(map);

		String pagingImg = BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum,
				req.getContextPath() + "/list.do", map);

		map.put("pagingImg", pagingImg);
		map.put("totalCount", totalCount);
		map.put("pageSize", pageSize);
		map.put("pageNum", pageNum);

		req.setAttribute("boardLists", boardLists);
		req.setAttribute("map", map);

		req.getRequestDispatcher("/view/Board/Free/List.jsp").forward(req, resp);
	}

	private void write(HttpServletRequest req, HttpServletResponse resp, BoardDAO dao)
			throws ServletException, IOException {
		BoardDTO dto = new BoardDTO();
		dto.setTitle(req.getParameter("title"));
		dto.setContent(req.getParameter("content"));

		String userId = (String) req.getSession().getAttribute("UserId");
		dto.setId(userId);

		int result = dao.insertWrite(dto);

		if (result == 1) {
			resp.sendRedirect(req.getContextPath() + "/list.do");
		} else {
			JSFunction.alertBack(resp, "글쓰기에 실패하였습니다.");
		}
	}

	// --- 상세 보기 기능 추가 (Start) ---
	private void view(HttpServletRequest req, HttpServletResponse resp, BoardDAO dao)
			throws ServletException, IOException {
		String num = req.getParameter("num");

		// 1. 조회수 증가
		if (num != null && !num.isEmpty()) {
			dao.updateVisitCount(num);
		}

		// 2. 게시물 정보 조회
		BoardDTO dto = dao.selectView(num);

		// 3. 줄 바꿈 처리를 <br> 태그로 변경
		if (dto.getContent() != null) {
			dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		}

		// 4. request 영역에 저장
		req.setAttribute("dto", dto);

		// 5. view.jsp로 포워드
		req.getRequestDispatcher("/view/Board/Free/View.jsp").forward(req, resp);
	}
	// --- 상세 보기 기능 추가 (End) ---
}