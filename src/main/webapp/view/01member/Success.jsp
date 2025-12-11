<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
String successMessage = (String) request.getAttribute("SuccessMessage");

// 메시지가 없으면 기본값 설정 (혹시 모를 직접 접근 방지)
if (successMessage == null) {
	successMessage = "작업이 성공적으로 완료되었습니다.";
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>작업 완료</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f7f6;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
	margin: 0;
}

.message-box {
	background-color: white;
	padding: 40px 60px;
	border-radius: 10px;
	box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
	text-align: center;
	border-left: 5px solid #28a745;
}

.message-box h2 {
	color: #28a745;
	margin-bottom: 20px;
	font-size: 1.8em;
}

.message-box p {
	color: #555;
	font-size: 1.2em;
	margin-bottom: 30px;
}

.message-box a {
	display: inline-block;
	padding: 10px 20px;
	background-color: #007bff;
	color: white;
	text-decoration: none;
	border-radius: 5px;
	font-weight: bold;
	transition: background-color 0.3s;
}

.message-box a:hover {
	background-color: #0056b3;
}
</style>
</head>
<body>
	<div class="message-box">
		<h2>성공</h2>
		<p><%=successMessage%></p>

		<a href="<%=contextPath%>/member/login.do">로그인 페이지로 이동</a>
	</div>
</body>
</html>