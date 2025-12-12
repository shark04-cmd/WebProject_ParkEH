<%@page import="board.BoardDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Map"%>
<%
String contextPath = request.getContextPath();

// Controller에서 전달된 데이터 받기
List<BoardDTO> boardList = (List<BoardDTO>) request.getAttribute("boardList");
Map<String, Object> map = (Map<String, Object>) request.getAttribute("map");

// 🚨 [수정된 안전 장치]: map 변수가 null인 경우 (Controller에서 정상적으로 데이터를 받지 못한 경우)
if (map == null) {
	// Controller를 다시 호출하도록 리다이렉트하여 처리를 위임
	response.sendRedirect(contextPath + "/board/list.do?boardType=free");
	return;
}

String userId = (String) session.getAttribute("UserID");
boolean isLoggedIn = (userId != null && !userId.isEmpty());

// 맵에서 필요한 값 추출
String boardType = (String) map.get("boardType");
String boardName = (String) map.get("boardName");
String searchField = (String) map.get("searchField");
String searchWord = (String) map.get("searchWord");
int totalCount = (Integer) map.get("totalCount");
String pagingStr = (String) map.get("pagingStr");

// 자료실 여부
boolean isDataBoard = "data".equals(boardType);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=boardName%> 목록 - WevProject_ParkEH</title>
<style>
/* 기존 공통 스타일 */
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

/* 목록 전용 스타일 */
.main-content {
	flex-grow: 1;
	padding: 20px 50px; /* 상하 여백 줄임 */
	text-align: center;
}

.main-content h2 {
	color: #0056b3;
	margin-bottom: 20px;
}

.board-table {
	width: 100%;
	border-collapse: collapse;
	margin-bottom: 20px;
}

.board-table th, .board-table td {
	border: 1px solid #ddd;
	padding: 10px;
	text-align: center;
}

.board-table th {
	background-color: #f0f8ff;
	color: #333;
	font-weight: bold;
}

.board-table tr:hover {
	background-color: #f9f9f9;
}

.board-table td a {
	text-decoration: none;
	color: #0056b3;
}

.board-table td a:hover {
	text-decoration: underline;
}

.title-column {
	text-align: left !important;
	padding-left: 15px !important;
}

/* 버튼 및 검색 폼 */
.top-area, .bottom-area {
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 20px;
}

.write-button a {
	padding: 8px 15px;
	background-color: #28a745;
	color: white;
	text-decoration: none;
	border-radius: 4px;
	font-weight: bold;
}

.write-button a:hover {
	background-color: #1e7e34;
}

.search-form {
	display: flex;
	gap: 5px;
}

.search-form select, .search-form input[type="text"] {
	padding: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
}

.search-form input[type="submit"] {
	padding: 8px 15px;
	background-color: #007bff;
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
}

.search-form input[type="submit"]:hover {
	background-color: #0056b3;
}

/* 페이징 스타일 */
.paging-area {
	text-align: center;
	margin-top: 20px;
}

.paging-area a, .paging-area span {
	display: inline-block;
	padding: 5px 10px;
	margin: 0 3px;
	text-decoration: none;
	color: #0056b3;
	border: 1px solid #ddd;
	border-radius: 4px;
}

.paging-area a:hover {
	background-color: #e6f0ff;
}

.paging-area .current-page {
	background-color: #007bff;
	color: white;
	border: 1px solid #007bff;
	font-weight: bold;
}

/* 게시물 없을 때 메시지 */
.no-posts {
	padding: 50px;
	border: 1px solid #eee;
	background-color: #fff;
	font-size: 1.1em;
	color: #777;
}
</style>
</head>
<body>

	<div class="header">
		<div class="header-left">
			<a href="<%=contextPath%>/Default.jsp"
				style="color: white; text-decoration: none;">WevProject_ParkEH</a>
		</div>
		<div class="header-right">
			<%
			if (isLoggedIn) {
			%>
			<a href="<%=contextPath%>/member/edit.do">회원정보수정</a> <a
				href="<%=contextPath%>/member/logout.do">로그아웃</a>
			<%
			} else {
			%>
			<a href="<%=contextPath%>/member/login.do">로그인</a> <a
				href="<%=contextPath%>/member/register.do">회원가입</a>
			<%
			}
			%>
		</div>
	</div>

	<div class="container">
		<div class="sidebar">
			<h4>게시판 메뉴</h4>
			<ul class="menu-list">
				<li class="<%="free".equals(boardType) ? "current" : ""%>"><a
					href="<%=contextPath%>/board/list.do?boardType=free">자유 게시판</a></li>
				<li class="<%="qna".equals(boardType) ? "current" : ""%>"><a
					href="<%=contextPath%>/board/list.do?boardType=qna">Q&A 게시판</a></li>
				<li class="<%="data".equals(boardType) ? "current" : ""%>"><a
					href="<%=contextPath%>/board/list.do?boardType=data">자료실 게시판</a></li>
			</ul>
		</div>

		<div class="main-content">
			<h2><%=boardName%></h2>

			<div class="top-area">
				<div class="total-count">
					총 게시물: **<%=totalCount%>** 건
				</div>
				<div class="write-button">
					<%
					if (isLoggedIn) {
					%>
					<a href="<%=contextPath%>/board/write.do?boardType=<%=boardType%>">글쓰기</a>
					<%
					} else {
					%>
					<a
						href="<%=contextPath%>/member/login.do?referer=<%=contextPath%>/board/write.do?boardType=<%=boardType%>">로그인
						후 글쓰기</a>
					<%
					}
					%>
				</div>
			</div>

			<table class="board-table">
				<thead>
					<tr>
						<th width="10%">번호</th>
						<th width="*">제목</th>
						<th width="12%">작성자</th>
						<th width="15%">작성일</th>
						<th width="10%">조회수</th>
						<th width="10%">좋아요</th>
						<%
						if (isDataBoard) {
						%>
						<th width="10%">첨부파일</th>
						<%
						}
						%>
					</tr>
				</thead>
				<tbody>
					<%
					if (boardList != null && !boardList.isEmpty()) {
						for (BoardDTO dto : boardList) {
					%>
					<tr>
						<td><%=dto.getNum()%></td>
						<td class="title-column"><a
							href="<%=contextPath%>/board/view.do?boardType=<%=dto.getBoardType()%>&num=<%=dto.getNum()%>">
								<%=dto.getTitle()%>
						</a></td>

						<%-- 🚨 [수정된 부분]: ID 제거, 이름만 출력 --%>
						<td><%=dto.getName()%></td>

						<td><%=dto.getPostdate().toString()%></td>
						<td><%=dto.getVisitcount()%></td>
						<td><%=dto.getLikeCount()%></td>
						<%
						if (isDataBoard) {
						%>
						<td><%=dto.getFileName() != null && !dto.getFileName().isEmpty() ? "O" : "X"%>
						</td>
						<%
						}
						%>
					</tr>
					<%
					}
					} else {
					%>
					<tr>
						<td colspan="<%=isDataBoard ? 7 : 6%>" class="no-posts">
							<%
							if (searchWord != null && !searchWord.isEmpty()) {
							%> 검색 결과가 없습니다. <%
							} else {
							%> 등록된 게시물이 없습니다. <%
							}
							%>
						</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>

			<div class="paging-area">
				<%=pagingStr%>
			</div>

			<div class="bottom-area">
				<div>&nbsp;</div>
				<form method="get" class="search-form"
					action="<%=contextPath%>/board/list.do">
					<input type="hidden" name="boardType" value="<%=boardType%>">
					<select name="searchField">
						<option value="TITLE"
							<%="TITLE".equals(searchField) ? "selected" : ""%>>제목</option>
						<option value="CONTENT"
							<%="CONTENT".equals(searchField) ? "selected" : ""%>>내용</option>
						<option value="ID" <%="ID".equals(searchField) ? "selected" : ""%>>작성자</option>
					</select> <input type="text" name="searchWord" placeholder="검색어 입력"
						value="<%=searchWord != null ? searchWord : ""%>"> <input
						type="submit" value="검색">
				</form>
			</div>
		</div>
	</div>

</body>
</html>