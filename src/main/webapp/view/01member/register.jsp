<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
String errorMessage = (String) request.getAttribute("RegisterErrorMessage");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 - WevProject_PaekEH</title>
<style>
/* Default.jsp와 동일한 스타일을 적용합니다. */
body {
	margin: 0;
	font-family: Arial, sans-serif;
	background-color: #f4f7f6;
}

.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 10px 20px;
	background-color: #0056b3;
	color: white;
	border-bottom: 1px solid #004085;
}

.header-left {
	font-size: 1.2em;
	font-weight: bold;
	color: white;
}

.header-right a {
	margin-left: 15px;
	text-decoration: none;
	color: white;
	font-weight: bold;
	font-size: 1.1em;
}

.container {
	display: flex;
	width: 100%;
	margin: 0;
	background-color: white;
	min-height: calc(100vh - 44px);
}

.sidebar {
	width: 200px;
	padding: 20px 0;
	background-color: #e6f0ff;
	border-right: 1px solid #ddd;
}

.sidebar h4 {
	margin: 0 20px 10px;
	color: #333;
	padding-top: 0;
}

.menu-list {
	list-style: none;
	padding: 0;
	margin: 0;
}

.menu-list li {
	margin-bottom: 0;
}

.menu-list>li>a {
	display: block;
	padding: 10px 20px;
	text-decoration: none;
	color: #333;
}

.menu-list>li>a:hover {
	background-color: #d1e2ff;
}

.menu-list .current>a {
	background-color: #007bff;
	color: white;
	font-weight: bold;
}

.main-content {
	flex-grow: 1;
	padding: 50px;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	text-align: center;
}

.main-content h2 {
	color: #0056b3;
	margin-bottom: 30px;
}

.register-form-box { /* 폼 박스 스타일 */
	padding: 30px;
	border: 1px solid #ddd;
	border-radius: 8px;
	background-color: #ffffff;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	width: 350px;
	text-align: left;
}

.register-form-box label {
	display: block;
	margin-top: 10px;
	margin-bottom: 3px;
	font-weight: bold;
	color: #333;
}

.register-form-box input[type="text"], .register-form-box input[type="password"],
	.register-form-box input[type="email"], .register-form-box input[type="tel"]
	{
	width: 100%;
	padding: 8px;
	margin-bottom: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.register-form-box input[type="submit"] {
	width: 100%;
	padding: 10px;
	background-color: #28a745; /* 가입은 초록색 */
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1.1em;
	margin-top: 25px;
}

.register-form-box input[type="submit"]:hover {
	background-color: #1e7e34;
}

.login-link {
	display: block;
	margin-top: 20px;
	text-align: center;
	color: #007bff;
	text-decoration: none;
}

.login-link:hover {
	text-decoration: underline;
}

.error-message {
	color: #cc0000;
	font-weight: bold;
	margin-bottom: 15px;
	text-align: center;
}
</style>
</head>
<body>

	<div class="header">
		<div class="header-left">
			<a href="<%=contextPath%>/Default.jsp"
				style="color: white; text-decoration: none;">WevProject_PaekEH</a>
		</div>
		<div class="header-right">
			<a href="<%=contextPath%>/member/login.do">로그인</a> <a
				href="<%=contextPath%>/member/register.do">회원가입</a>
		</div>
	</div>

	<div class="container">
		<div class="sidebar">
			<h4>게시판 메뉴</h4>
			<ul class="menu-list">
				<li><a href="<%=contextPath%>/board/list.do?boardType=free">자유
						게시판</a></li>
				<li><a href="<%=contextPath%>/board/list.do?boardType=qna">Q&A
						게시판</a></li>
				<li><a href="<%=contextPath%>/board/list.do?boardType=data">자료실
						게시판</a></li>
			</ul>
		</div>

		<div class="main-content">
			<h2>회원 가입</h2>

			<%
			if (errorMessage != null) {
			%>
			<p class="error-message"><%=errorMessage%></p>
			<%
			}
			%>

			<div class="register-form-box">
				<form action="<%=contextPath%>/member/register.do" method="post">
					<label for="id">아이디</label> <input type="text" id="id" name="id"
						required placeholder="아이디" maxlength="20"
						value="<%=(request.getParameter("id") != null) ? request.getParameter("id") : ""%>">

					<label for="pass">패스워드</label> <input type="password" id="pass"
						name="pass" required placeholder="비밀번호" maxlength="20"> <label
						for="name">이름</label> <input type="text" id="name" name="name"
						required placeholder="이름" maxlength="30"> <label
						for="email">이메일</label> <input type="email" id="email"
						name="email" required placeholder="example@domain.com"
						maxlength="100"> <label for="phone">전화번호</label> <input
						type="tel" id="phone" name="phone" placeholder="010-0000-0000"
						maxlength="20"> <input type="submit" value="회원가입 완료">
				</form>
				<a href="<%=contextPath%>/member/login.do" class="login-link">이미
					회원이신가요? 로그인</a>
			</div>
		</div>
	</div>

</body>
</html>