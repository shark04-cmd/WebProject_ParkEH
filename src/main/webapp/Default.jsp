<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
// 로그인 상태 확인을 위한 임시 세션 변수 사용
// 실제로는 MemberDTO 객체를 세션에 저장하는 방식으로 구현됩니다.
String userID = (String) session.getAttribute("UserID");
boolean isLoggedIn = (userID != null);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>WevProject_PaekEH</title>
<style>
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
	min-height: calc(100vh - 44px); /* header 높이만큼 제외 */
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
	display: flex; /* 중앙 정렬을 위해 flex 사용 */
	justify-content: center;
	align-items: center;
	text-align: center;
}

.main-content h2 {
	color: #0056b3;
	margin-bottom: 30px;
}

.message-box {
	padding: 40px;
	border: 1px solid #ccc;
	border-radius: 8px;
	display: inline-block;
	background-color: #ffffff;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
}

.message-box p {
	font-size: 1.2em;
	color: #555;
	margin: 0 0 20px 0;
}

.message-box a {
	display: inline-block;
	padding: 10px 20px;
	background-color: #28a745;
	color: white;
	text-decoration: none;
	border-radius: 4px;
	font-weight: bold;
}
</style>
</head>
<body>

	<div class="header">
		<div class="header-left">
			<a href="Default.jsp" style="color: white; text-decoration: none;">WevProject_PaekEH</a>
		</div>
		<div class="header-right">
			<%
			if (isLoggedIn) {
			%>
			<a href="member/edit.do">회원정보수정</a> <a href="member/logout.do">로그아웃</a>
			<%
			} else {
			%>
			<a href="member/login.do">로그인</a> <a href="member/register.do">회원가입</a>
			<%
			}
			%>
		</div>
	</div>

	<div class="container">
		<div class="sidebar">
			<h4>게시판 메뉴</h4>
			<ul class="menu-list">
				<li><a href="board/list.do?boardType=free">자유 게시판</a></li>
				<li><a href="board/list.do?boardType=qna">Q&A 게시판</a></li>
				<li><a href="board/list.do?boardType=data">자료실 게시판</a></li>
			</ul>
		</div>

		<div class="main-content">
			<div class="message-box">
				<%
				if (isLoggedIn) {
				%>
				<p>
					**<%=userID%>**님 환영합니다!
				</p>
				<a href="board/list.do?boardType=free">자유게시판으로!</a>
				<%
				} else {
				%>
				<p>로그인 해주세요!</p>
				<a href="member/login.do">로그인</a>
				<%
				}
				%>
			</div>
		</div>
	</div>

</body>
</html>