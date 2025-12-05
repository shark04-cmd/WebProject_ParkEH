package common;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import member.LoginAction;
import board.BoardListAction;
import member.JoinAction;
import member.LogoutAction;
import board.BoardWriteAction;
import member.MainAction;
import board.BoardContentAction;
import member.MemberUpdateAction;

import member.MemberDAO; 
import member.MemberDTO;


@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String command = requestURI.substring(contextPath.length());

		String viewPage = null;

		if (command.equals("/main.do")) {
			MainAction action = new MainAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		else if (command.equals("/board/list.do")) {
			BoardListAction action = new BoardListAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		else if (command.equals("/board/content.do")) {
			BoardContentAction action = new BoardContentAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		else if (command.equals("/board/writeform.do")) {
			viewPage = "/board/writeForm.jsp";
		}

		else if (command.equals("/board/writeaction.do")) {
			BoardWriteAction action = new BoardWriteAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		else if (command.equals("/member/joinform.do")) {
			viewPage = "/member/joinForm.jsp";
		}

		else if (command.equals("/member/joinaction.do")) {
			JoinAction action = new JoinAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		else if (command.equals("/member/loginform.do")) {
			viewPage = "/member/loginForm.jsp";
		}

		else if (command.equals("/member/loginaction.do")) {
			LoginAction action = new LoginAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		else if (command.equals("/member/logoutaction.do")) {
			LogoutAction action = new LogoutAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		// 회원 수정 폼 요청
		else if (command.equals("/member/memberupdateform.do")) {
			if (request.getSession().getAttribute("userID") != null) {
				try {
					String id = (String) request.getSession().getAttribute("userID");
					MemberDAO mdao = new MemberDAO();
					MemberDTO member = mdao.getMember(id); 
					request.setAttribute("member", member); 
				} catch (Exception e) {
					e.printStackTrace();
				}
				viewPage = "/member/memberUpdateForm.jsp"; 
			} else {
				viewPage = "redirect:" + request.getContextPath() + "/member/loginForm.do";
			}
		}
		
		// 회원 수정 처리 요청
		else if (command.equals("/member/memberupdateaction.do")) {
			MemberUpdateAction action = new MemberUpdateAction();
			try {
				viewPage = action.execute(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				viewPage = "/WEB-INF/error.jsp";
			}
		}

		if (viewPage != null) {
			if (viewPage.startsWith("redirect:")) {
				response.sendRedirect(viewPage.substring("redirect:".length()));
			} else {
				request.getRequestDispatcher(viewPage).forward(request, response);
			}
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}
}
