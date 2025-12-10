<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ page import="utils.JSFunction"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유 게시판 - 상세 보기</title>
<script>
	// 삭제 버튼 클릭 시 실행될 함수
	function confirmDelete(num) {
		if (confirm("정말로 삭제하시겠습니까?")) {
			// JSFunction의 alertLocation 대신, 직접 delete.do로 이동 (Controller에서 처리)
			location.href = "../../delete.do?num=" + num;
		}
	}
</script>
<style>
/* [Default.jsp와 동일한 스타일 유지] */
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

/* 상세 보기 스타일 */
.board-view {
	flex-grow: 1;
	padding-right: 20px;
}

.board-view table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.board-view th, .board-view td {
	border: 1px solid #ddd;
	padding: 10px;
	text-align: left;
}

.board-view th {
	width: 15%;
	background-color: #f2f2f2;
	font-weight: normal;
}

.board-view td {
	width: 35%;
}

.board-view .title-row th {
	width: 15%;
}

.board-view .title-row td {
	width: 85%;
}

.board-view textarea {
	width: 100%;
	height: 300px;
	padding: 10px;
	border: none;
	box-sizing: border-box;
	font-family: inherit;
	line-height: 1.5;
	background-color: #f9f9f9;
	/* HTML로 변환된 줄바꿈이 적용되도록 처리 */
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

.list-btn {
	background-color: #6c757d;
	color: white;
}

.edit-btn {
	background-color: #ffc107;
	color: black;
}

.delete-btn {
	background-color: #dc3545;
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
			<div class="board-view">
				<h2>자유 게시판 - 상세 보기 (View)</h2>

				<table>
					<tr class="title-row">
						<th>제목</th>
						<td colspan="3">${ dto.title }</td>
					</tr>
					<tr>
						<th>작성자</th>
						<td>${ dto.name }(${ dto.id })</td>
						<th>조회수</th>
						<td>${ dto.visitcount }</td>
					</tr>
					<tr>
						<th>작성일</th>
						<td>${ dto.postdate }</td>
						<th>글번호</th>
						<td>${ dto.num }</td>
					</tr>
					<tr>
						<td colspan="4" style="padding: 0;"><textarea readonly>${ dto.content }</textarea>
						</td>
					</tr>
				</table>

				<div class="button-group">
					<c:if test="${ sessionScope.UserId eq dto.id }">
						<button type="button" class="edit-btn"
							onclick="location.href='../../edit.do?num=${ dto.num }';">수정하기</button>
						<button type="button" class="delete-btn"
							onclick="confirmDelete('${ dto.num }');">삭제하기</button>
					</c:if>

					<button type="button" class="list-btn"
						onclick="location.href='../../list.do';">목록으로</button>
				</div>
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