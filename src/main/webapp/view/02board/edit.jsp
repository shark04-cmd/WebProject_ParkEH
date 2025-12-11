<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="board.BoardDTO"%>
<%
String contextPath = request.getContextPath();

// Controller에서 전달된 데이터 받기
BoardDTO dto = (BoardDTO) request.getAttribute("dto");
String boardType = (String) request.getAttribute("boardType");
String errorMessage = (String) request.getAttribute("EditErrorMessage");

// 게시판 이름 설정
String boardName = "";
if (boardType == null)
	boardType = "free"; 
if ("free".equals(boardType)) {
	boardName = "자유 게시판";
} else if ("qna".equals(boardType)) {
	boardName = "Q&A 게시판";
} else if ("data".equals(boardType)) {
	boardName = "자료실 게시판";
}

// DTO가 null인 경우 (Controller에서 처리했겠지만, JSP에서 안전장치)
if (dto == null) {
	response.sendRedirect(contextPath + "/board/list.do?boardType=" + boardType);
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%=boardName%> 수정 - WevProject_PaekEH</title>
<style>
/* 으녀기님의 스타일 가이드라인을 따릅니다. */
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
	display: flex; /* 폼 중앙 정렬을 위해 flex 추가 */
	flex-direction: column;
	align-items: center;
}

.main-content h2 {
	color: #0056b3;
	margin-bottom: 30px;
}
/* 수정 폼 개별 스타일 (write.jsp와 동일) */
.edit-form {
	width: 600px;
	padding: 20px;
	border: 1px solid #ddd;
	border-radius: 8px;
	background-color: #ffffff;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	text-align: left;
}

.edit-form label {
	display: block;
	margin-top: 15px;
	margin-bottom: 5px;
	font-weight: bold;
	color: #333;
}

.edit-form input[type="text"], .edit-form textarea {
	width: 100%;
	padding: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
	font-size: 1em;
}

.edit-form textarea {
	height: 250px;
	resize: vertical;
}

.btn-group {
	text-align: right;
	margin-top: 25px;
}

.btn-group input[type="submit"], .btn-group button {
	padding: 10px 20px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
	margin-left: 10px;
}

.btn-group input[type="submit"] {
	background-color: #ffc107; /* 수정 버튼 색상 변경 */
	color: #333;
}

.btn-group input[type="submit"]:hover {
	background-color: #e0a800;
}

.btn-group button {
	background-color: #6c757d;
	color: white;
}

.btn-group button:hover {
	background-color: #5a6268;
}

.error-message {
	color: red;
	font-weight: bold;
	margin-bottom: 10px;
}
</style>
<script>
	function validateForm(form) {
		if (form.title.value.trim() === "") {
			alert("제목을 입력하세요.");
			form.title.focus();
			return false;
		}
		if (form.content.value.trim() === "") {
			alert("내용을 입력하세요.");
			form.content.focus();
			return false;
		}
		
		// 자료실 게시판일 경우, 파일명 유효성 검사 
		if (form.boardType.value === "data") {
			const fileNameField = form.fileName;
			if (fileNameField.value.trim() === "") {
				alert("자료실 게시물은 파일명을 입력해야 합니다. (실제 업로드는 생략)");
				fileNameField.focus();
				return false;
			}
		}
		return true;
	}
	
	function goBack() {
		// 상세 보기 페이지로 이동합니다.
		window.location.href = "<%=contextPath%>/board/view.do?boardType=<%=boardType%>&num=<%=dto.getNum()%>
	";
	}
</script>
</head>
<body>

	<div class="header">
		<div class="header-left">
			<a href="<%=contextPath%>/Default.jsp"
				style="color: white; text-decoration: none;">WevProject_PaekEH</a>
		</div>
		<div class="header-right">
			<a href="<%=contextPath%>/member/edit.do">회원정보수정</a> <a
				href="<%=contextPath%>/member/logout.do">로그아웃</a>
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
			<h2><%=boardName%>
				- 수정하기
			</h2>

			<%
			if (errorMessage != null) {
			%>
			<p class="error-message"><%=errorMessage%></p>
			<%
			}
			%>

			<form class="edit-form" action="<%=contextPath%>/board/edit.do"
				method="post" onsubmit="return validateForm(this);">
				
				<input type="hidden" name="boardType" value="<%=boardType%>">
				<input type="hidden" name="num" value="<%=dto.getNum()%>"> 

				<label>작성자</label> <input type="text" value="<%=dto.getName()%> (<%=dto.getId()%>) " readonly
					style="background-color: #eee;"> <label for="title">제목</label>
				<input type="text" id="title" name="title" required
					placeholder="제목을 입력하세요" value="<%=dto.getTitle()%>">

				<%
				// 자료실 게시판일 경우 파일명 입력 필드 추가
				if ("data".equals(boardType)) {
				%>
				<label for="fileName">파일명 (실제 업로드 대신 파일명만 입력)</label> <input
					type="text" id="fileName" name="fileName" required
					placeholder="예: image.jpg, video.mp4"
					value="<%=dto.getFileName() != null ? dto.getFileName() : ""%>">
				<span style="font-size: 0.85em; color: gray;">* cos.jar를 사용하지
						않으므로 파일명만 저장됩니다.</span>
				<%
				}
				%>

				<label for="content">내용</label>
				<textarea id="content" name="content" required
					placeholder="내용을 입력하세요"><%=dto.getContent()%></textarea>

				<div class="btn-group">
					<button type="button" onclick="goBack()">취소/상세보기</button>
					<input type="submit" value="수정 완료">
				</div>
			</form>
		</div>
	</div>

</body>
</html>