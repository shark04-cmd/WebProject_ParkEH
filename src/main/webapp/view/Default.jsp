<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>WevProject_PaekEH 2차 프로젝트</title>
<style>
/* 1. HTML, BODY: 화면 전체 높이 및 마진 제거 */
html, body {
	height: 100%;
	margin: 0;
	font-family: Arial, sans-serif;
	background-color: white;
}

/* 2. Header: 진한 파란색 배경 및 폰트 스타일 수정 */
.header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 10px 20px;
	background-color: #0056b3; /* 진한 파란색 계열 */
	color: white; /* 텍스트 색상 흰색 */
	border-bottom: 1px solid #004085;
}

.header-left {
	font-size: 1.2em;
	font-weight: bold;
	color: white; /* '2차 프로젝트!' 텍스트 흰색 */
}

/* 3. 로그인/회원가입 폰트 스타일 수정 */
.header-right a {
	margin-left: 15px;
	text-decoration: none;
	color: white; /* 링크 텍스트 흰색 */
	font-weight: bold; /* 굵게 */
	font-size: 1.1em; /* 크게 */
}

/* 4. Container: 너비를 100%로 늘려 좌우 빈 공간 제거 */
.container {
	display: flex;
	width: 100%; /* 화면 너비 전체 사용 */
	margin: 0; /* 마진 제거 */
	background-color: white;
	border: none;
	min-height: calc(100vh - 44px); /* 헤더 높이(약 44px)만큼 빼서 화면 전체를 채우도록 조정 */
}

/* 왼쪽 사이드바: 너비 유지 */
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

/* 메인 컨텐츠 영역: 최대한 확장 */
.board-main-content {
	flex-grow: 1;
	padding: 30px;
}

/* 오른쪽 계정 정보 패널: 너비 유지 */
.right-info-panel {
	width: 200px;
	padding: 20px;
	border-left: 1px solid #ddd;
	background-color: white;
}

/* 실제 로그인 정보 박스 */
.account-info-box {
	padding: 15px;
	background-color: #f8f8f8;
	border: 1px solid #ddd;
	border-radius: 4px;
	text-align: center;
}

.account-info-box h4 {
	margin: 0 0 10px 0;
	color: #333;
}

/* 오른쪽 패널의 게시판별 상태 리스트 */
.my-board-status-list {
	margin-top: 20px;
	text-align: left;
}

.status-item {
	margin-bottom: 10px;
	padding-bottom: 5px;
	border-bottom: 1px solid #eee;
}

.status-item a {
	display: block;
	text-decoration: none;
	color: #007bff;
	font-weight: bold;
	font-size: 0.95em;
	padding: 5px 0;
}

.status-item p {
	margin: 0;
	font-weight: bold;
	color: #555;
	font-size: 0.95em;
}

.status-item .no-posts-msg {
	display: block;
	color: #dc3545;
	font-size: 0.9em;
	margin-top: 5px;
}

.login-required {
	text-align: center;
	padding: 50px 0;
	font-size: 1.5em;
	color: #dc3545;
	border: 1px dashed #dc3545;
	margin-top: 50px;
}

.login-required.success {
	background-color: #e3f2fd;
	border: 1px solid #90caf9;
	color: #007bff;
	padding: 50px 0;
}

.login-required a {
	text-decoration: none;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="header">
		<div class="header-left">2차 프로젝트!</div>
		<div class="header-right">
			<c:choose>
				<c:when test="${ empty sessionScope.UserId }">
					<a href="member/LoginForm.jsp">로그인</a>
					<a href="member/member_regist.jsp">회원가입</a>
				</c:when>
				<c:otherwise>
					<a href="#">${ sessionScope.UserName } 회원님</a>
					<a href="member/Logout.jsp">로그아웃</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>

	<div class="container">
		<div class="sidebar">
			<h4>게시판 목록</h4>
			<ul class="menu-list">
				<li class="current"><a href="../list.do">자유 게시판</a></li>
				<li><a href="../qa_list.do">Q&A 게시판</a></li>
				<li><a href="../data_list.do">자료실 게시판</a></li>
			</ul>
		</div>

		<div class="board-main-content">
			<h2>자유 게시판</h2>

			<c:choose>
				<c:when test="${ empty sessionScope.UserId }">
					<div class="login-required">로그인을 해야 볼 수 있습니다.</div>
				</c:when>
				<c:otherwise>
					<div class="login-required success">
						로그인 상태입니다. <a href="../list.do">게시판으로 이동</a>
					</div>
				</c:otherwise>
			</c:choose>
		</div>

		<div class="right-info-panel">
			<div class="account-info-box">
				<h4>계정 로그인 정보</h4>
				<p>
					<c:choose>
						<c:when test="${ empty sessionScope.UserId }">
                            로그인 상태가 아닙니다.
                        </c:when>
						<c:otherwise>
                            환영합니다, **${ sessionScope.UserName }** 회원님!
                            
                            <div class="my-board-status-list">

								<div class="status-item">
									<c:choose>
										<c:when test="${ empty hasUserPosts }">
											<p>자유 게시판</p>
											<span class="no-posts-msg">글을 먼저 써주세요!</span>
										</c:when>
										<c:otherwise>
											<a
												href="../list.do?searchField=id&searchWord=${ sessionScope.UserId }">자유
												게시판 ㄴ 내가 쓴 글</a>
										</c:otherwise>
									</c:choose>
								</div>

								<div class="status-item">
									<c:choose>
										<c:when test="${ empty hasUserPosts }">
											<p>Q&A 게시판</p>
											<span class="no-posts-msg">글을 먼저 써주세요!</span>
										</c:when>
										<c:otherwise>
											<a
												href="../qa_list.do?searchField=id&searchWord=${ sessionScope.UserId }">Q&A
												게시판 ㄴ 내가 쓴 글</a>
										</c:otherwise>
									</c:choose>
								</div>

								<div class="status-item">
									<c:choose>
										<c:when test="${ empty hasUserPosts }">
											<p>자료실 게시판</p>
											<span class="no-posts-msg">글을 먼저 써주세요!</span>
										</c:when>
										<c:otherwise>
											<a
												href="../data_list.do?searchField=id&searchWord=${ sessionScope.UserId }">자료실
												게시판 ㄴ 내가 쓴 글</a>
										</c:otherwise>
									</c:choose>
								</div>

							</div>

						</c:otherwise>
					</c:choose>
				</p>
			</div>
		</div>

	</div>
</body>
</html>