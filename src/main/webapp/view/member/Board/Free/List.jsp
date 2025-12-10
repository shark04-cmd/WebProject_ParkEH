<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유 게시판</title>
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

.board-list {
	flex-grow: 1;
}

.board-list table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

.board-list th, .board-list td {
	border: 1px solid #ddd;
	padding: 8px;
	text-align: center;
}

.board-list th {
	background-color: #f2f2f2;
}

.search-box {
	text-align: center;
	margin-bottom: 20px;
}

.write-button {
	text-align: right;
	margin-top: 10px;
}

.write-button button {
	padding: 10px 20px;
	background-color: #ffc107;
	border: none;
	cursor: pointer;
	border-radius: 4px;
	font-weight: bold;
}

.paging {
	text-align: center;
	margin-top: 20px;
}

.paging a {
	text-decoration: none;
	color: #007bff;
	padding: 5px 10px;
	border: 1px solid #007bff;
	margin: 0 2px;
	border-radius: 3px;
}

.paging a:hover {
	background-color: #e6f0ff;
}

.paging span.current {
	background-color: #007bff;
	color: white;
	font-weight: bold;
	padding: 5px 10px;
	border: 1px solid #007bff;
	margin: 0 2px;
	border-radius: 3px;
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
			<div class="board-list">
				<h2>자유 게시판 - 목록 보기(List)</h2>

				<div class="search-box">
					<form method="get" action="../../list.do">
						<select name="searchField">
							<option value="subject"
								<c:if test="${map.searchField eq 'subject'}">selected</c:if>>제목</option>
							<option value="content"
								<c:if test="${map.searchField eq 'content'}">selected</c:if>>내용</option>
							<option value="id"
								<c:if test="${map.searchField eq 'id'}">selected</c:if>>작성자</option>
						</select> <input type="text" name="searchWord" value="${map.searchWord}"
							placeholder="검색어 입력" /> <input type="submit" value="검색" />
					</form>
				</div>

				<table>
					<tr>
						<th width="10%">번호</th>
						<th width="*">제목</th>
						<th width="15%">작성자</th>
						<th width="10%">조회수</th>
						<th width="15%">작성일</th>
					</tr>
					<c:choose>
						<c:when test="${ empty boardLists }">
							<tr>
								<td colspan="5" align="center">등록된 게시물이 없습니다^^*</td>
							</tr>
						</c:when>
						<c:otherwise>
							<c:forEach items="${ boardLists }" var="row" varStatus="loop">
								<tr align="center">
									<td>${ map.totalCount - (((map.pageNum - 1) * map.pageSize) + loop.index) }
									</td>
									<td align="left"><a
										href="../../view.do?num=${ row.num }&${ map.queryString }">
											${ row.title }</a></td>
									<td>${ row.id }</td>
									<td>${ row.visitcount }</td>
									<td>${ row.postdate }</td>
								</tr>
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</table>

				<div class="paging">
					<div style="float: left; width: 50%;">${ map.pagingImg }</div>
					<div class="write-button" style="float: right; width: 50%;">
						<c:if test="${ not empty sessionScope.UserId }">
							<button type="button" onclick="location.href='../../write.do';">글쓰기</button>
						</c:if>
					</div>
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