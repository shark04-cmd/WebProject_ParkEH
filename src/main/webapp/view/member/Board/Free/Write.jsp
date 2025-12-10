<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유 게시판 - 글쓰기</title>
<script type="text/javascript">
	function validateForm(form) {
		if (form.title.value == "") {
			alert("제목을 입력하세요.");
			form.title.focus();
			return false;
		}
		if (form.content.value == "") {
			alert("내용을 입력하세요.");
			form.content.focus();
			return false;
		}
	}
</script>
<style>
/* [Default.jsp와 동일한 스타일] */
body {
	margin: 0;
	font-family: Arial, sans-serif;
	background-color: #f4f7f6;
}

.header {
	padding: 10px 20px;
	text-align: right;
	background-color: white;
	border-bottom: 1px solid #ddd;
}

.header a {
	margin-left: 15px;
	text-decoration: none;
	color: #333;
}

.container {
	display: flex;
	width: 90%;
	margin: 20px auto;
	background-color: white;
	border: 1px solid #ccc;
	min-height: 700px;
}

.sidebar {
	width: 200px;
	padding: 20px;
	background-color: #e6f0ff;
	border-right: 1px solid #ccc;
}

.sidebar h4 {
	margin-top: 0;
	color: #333;
}

.menu-list {
	list-style: none;
	padding: 0;
}

.menu-list li {
	margin-bottom: 10px;
}

.menu-list>li>a {
	display: block;
	padding: 10px;
	text-decoration: none;
	color: #333;
	border-radius: 4px;
}

.menu-list>li>a:hover {
	background-color: #d1e2ff;
}

.menu-list .current>a {
	background-color: #007bff;
	color: white;
	font-weight: bold;
}

.submenu-list {
	list-style: none;
	padding: 0;
	margin-top: 5px;
	margin-left: 10px;
}

.submenu-list a {
	display: block;
	padding: 5px 10px;
	text-decoration: none;
	color: #555;
	font-size: 0.9em;
	border-left: 3px solid #ccc;
}

.submenu-list a:hover {
	background-color: #f0f0f0;
}

.content {
	flex-grow: 1;
	padding: 30px;
}

.login-status {
	width: 200px;
	padding: 20px;
	background-color: #f8f9fa;
	border: 1px solid #ccc;
	margin-left: 20px;
}

.main-area {
	display: flex;
}

.board-write {
	flex-grow: 1;
	padding-right: 20px;
}

.board-write table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.board-write th, .board-write td {
	border: 1px solid #ddd;
	padding: 10px;
	text-align: left;
}

.board-write th {
	width: 15%;
	background-color: #f2f2f2;
}

.board-write input[type="text"] {
	width: 95%;
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.board-write textarea {
	width: 95%;
	height: 300px;
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	resize: none;
}

.button-group {
	text-align: right;
	margin-top: 20px;
}

.button-group button {
	padding: 10px 20px;
	margin-left: 10px;
	border: none;
	cursor: pointer;
	border-radius: 4px;
	font-weight: bold;
}

.submit-btn {
	background-color: #007bff;
	color: white;
}

.cancel-btn {
	background-color: #6c757d;
	color: white;
}
</style>
</head>
<body>
	<div class="header">
		<span>2차 프로젝트!</span>
		<c:choose>
			<c:when test="${ empty sessionScope.UserId }">
				<a href="../member/LoginForm.jsp">로그인</a>
				<a href="../member/member_regist.jsp">회원가입</a>
			</c:when>
			<c:otherwise>
				<a href="#">${ sessionScope.UserName } 회원님</a>
				<a href="../member/Logout.jsp">로그아웃</a>
			</c:otherwise>
		</c:choose>
	</div>

	<div class="container">
		<div class="sidebar">
			<h4>게시판 목록</h4>
			<ul class="menu-list">
				<li class="current"><a href="../../list.do">자유 게시판</a> <c:if
						test="${ not empty sessionScope.UserId }">
						<ul class="submenu-list">
							<li><a
								href="../../list.do?searchField=id&searchWord=${ sessionScope.UserId }">ㄴ
									내가 쓴 글</a></li>
						</ul>
					</c:if></li>
				<li><a href="../../qa_list.do">Q&A 게시판</a></li>
				<li><a href="../../data_list.do">자료실 게시판</a></li>
			</ul>
		</div>

		<div class="main-area">
			<div class="board-write">
				<h2>자유 게시판 - 글쓰기 (Write)</h2>

				<form name="writeFrm" method="post" action="../../write.do"
					onsubmit="return validateForm(this);">
					<table>
						<tr>
							<th>제목</th>
							<td><input type="text" name="title" /></td>
						</tr>
						<tr>
							<th>작성자</th>
							<td>${ sessionScope.UserName }(${ sessionScope.UserId })</td>
						</tr>
						<tr>
							<th>내용</th>
							<td><textarea name="content"></textarea></td>
						</tr>
					</table>

					<div class="button-group">
						<button type="submit" class="submit-btn">작성 완료</button>
						<button type="button" class="cancel-btn"
							onclick="location.href='../../list.do';">목록으로</button>
					</div>
				</form>
			</div>

			<div class="login-status">
				<h4>계정 로그인</h4>
				<p>
					<c:choose>
						<c:when test="${ empty sessionScope.UserId }">
                            로그인 상태가 아닙니다.
                        </c:when>
						<c:otherwise>
                            로그인 상태입니다.<br />
                            ${ sessionScope.UserName } 회원님.
                        </c:otherwise>
					</c:choose>
				</p>
			</div>
		</div>
	</div>
</body>
</html>