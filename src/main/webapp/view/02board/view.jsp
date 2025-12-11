<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${boardName}-${dto.title}</title>
<style>
/* ... (스타일 유지) ... */
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

.menu-list>li>a {
	display: block;
	padding: 10px 20px;
	text-decoration: none;
	color: #333;
}

.menu-list>li>a:hover {
	background-color: #d1e2ff;
}

.main-content {
	flex-grow: 1;
	padding: 50px;
	align-items: center;
}

.main-content h2 {
	color: #0056b3;
	margin-bottom: 30px;
	text-align: center;
}

/* 게시물 상세 스타일 */
.board-view-table {
	width: 80%;
	max-width: 900px;
	margin: 0 auto;
	border-collapse: collapse;
	background-color: white;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.board-view-table th, .board-view-table td {
	border: 1px solid #ddd;
	padding: 12px;
}

.board-view-table th {
	background-color: #f8f8f8;
	color: #555;
	width: 15%;
	text-align: center;
	font-weight: bold;
}

.board-view-table .title-row td {
	font-size: 1.5em;
	font-weight: bold;
	background-color: #eef;
	text-align: center;
	padding: 15px;
}

.board-view-table .info-row td {
	font-size: 0.9em;
	color: #777;
}

.board-view-table .content-row td {
	height: 300px;
	vertical-align: top;
	line-height: 1.6;
}

.button-area {
	width: 80%;
	max-width: 900px;
	margin: 20px auto;
	text-align: right;
}

.button-area a, .button-area button {
	display: inline-block;
	padding: 8px 15px;
	margin-left: 5px;
	text-decoration: none;
	border-radius: 4px;
	font-weight: bold;
	cursor: pointer;
	border: none;
}

.btn-list {
	background-color: #6c757d;
	color: white;
}

.btn-list:hover {
	background-color: #5a6268;
}

.btn-edit {
	background-color: #ffc107;
	color: #212529;
}

.btn-edit:hover {
	background-color: #e0a800;
}

.btn-delete {
	background-color: #dc3545;
	color: white;
}

.btn-delete:hover {
	background-color: #c82333;
}

.file-download {
	margin-top: 10px;
	font-size: 1em;
	font-weight: bold;
	color: #007bff;
}

.file-download:hover {
	text-decoration: underline;
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
			<c:choose>
				<c:when test="${not empty sessionScope.UserID}">
					<a href="<%=contextPath%>/member/edit.do">회원정보수정</a>
					<a href="<%=contextPath%>/member/logout.do">로그아웃</a>
				</c:when>
				<c:otherwise>
					<a href="<%=contextPath%>/member/login.do">로그인</a>
					<a href="<%=contextPath%>/member/register.do">회원가입</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="container">
		<div class="sidebar">
			<h4>게시판 메뉴</h4>
			<ul class="menu-list">
				<li class="<c:if test="${boardType eq 'free'}">current</c:if>"><a
					href="<%=contextPath%>/board/list.do?boardType=free">자유 게시판</a></li>
				<li class="<c:if test="${boardType eq 'qna'}">current</c:if>"><a
					href="<%=contextPath%>/board/list.do?boardType=qna">Q&A 게시판</a></li>
				<li class="<c:if test="${boardType eq 'data'}">current</c:if>"><a
					href="<%=contextPath%>/board/list.do?boardType=data">자료실 게시판</a></li>
			</ul>
		</div>

		<div class="main-content">
			<h2>${boardName}상세</h2>

			<c:choose>
				<c:when test="${dto != null and not empty dto.num}">
					<table class="board-view-table">
						<tr class="title-row">
							<td colspan="4">${dto.title}</td>
						</tr>
						<tr class="info-row">
							<th>작성자</th>
							<td>${dto.name}(${dto.id})</td>
							<th>작성일</th>
							<td><fmt:formatDate value="${dto.postdate}"
									pattern="yyyy-MM-dd HH:mm" /></td>
						</tr>
						<tr class="info-row">
							<th>조회수 / 좋아요</th>
							<td>${dto.visitcount}/${dto.likeCount}</td>
							<th>게시물 번호</th>
							<td>${dto.num}</td>
						</tr>

						<c:if test="${boardType eq 'data' and not empty dto.fileName}">
							<tr class="info-row">
								<th>첨부 파일</th>
								<td colspan="3">
									<%-- 파일 다운로드 기능은 download.do로 요청한다고 가정 --%> <a
									href="<%=contextPath%>/board/download.do?num=${dto.num}&boardType=${boardType}"
									class="file-download"> ${dto.fileName} (다운로드) </a>
								</td>
							</tr>
						</c:if>

						<tr class="content-row">
							<th>내용</th>
							<td colspan="3">${dto.content}</td>
						</tr>
					</table>

					<div class="button-area">
						<%-- 로그인된 사용자 ID를 가져와서 작성자인지 확인 --%>
						<c:set var="sessionId" value="${sessionScope.UserID}" />

						<c:if test="${not empty sessionId and sessionId eq dto.id}">
							<%-- 작성자만 수정/삭제 버튼 노출. URL에 boardType 추가 --%>
							<a
								href="<%=contextPath%>/board/edit.do?num=${dto.num}&boardType=${boardType}"
								class="btn-edit">수정하기</a>
							<a
								href="<%=contextPath%>/board/delete.do?num=${dto.num}&boardType=${boardType}"
								class="btn-delete" onclick="return confirm('정말로 삭제하시겠습니까?');">삭제하기</a>
						</c:if>

						<%-- 목록 보기 버튼 클릭 시 현재 게시판 타입으로 이동 --%>
						<a href="<%=contextPath%>/board/list.do?boardType=${boardType}"
							class="btn-list">목록 보기</a>
					</div>
				</c:when>
				<c:otherwise>
					<p style="text-align: center; color: red;">해당 게시물을 찾을 수 없거나 게시물
						번호가 올바르지 않습니다.</p>
					<div class="button-area" style="text-align: center;">
						<a href="<%=contextPath%>/board/list.do" class="btn-list">게시판
							목록으로</a>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</body>
</html>