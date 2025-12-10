<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="dao.MemberDAO"%>
<%@ page import="dto.MemberDTO"%>
<%@ page import="utils.JSFunction"%>
<%
String id = request.getParameter("id");
String pass = request.getParameter("pass");

MemberDAO dao = new MemberDAO(application);
MemberDTO dto = dao.getMemberDTO(id, pass);
dao.close();

if (dto.getId() != null) {
	session.setAttribute("UserId", dto.getId());
	session.setAttribute("UserName", dto.getName());

	response.sendRedirect("../Default.jsp");
} else {
	JSFunction.alertBack("아이디 또는 비밀번호가 일치하지 않습니다.", out);
}
%>