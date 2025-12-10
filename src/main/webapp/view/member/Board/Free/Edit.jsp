<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 수정 - 2차 프로젝트</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f4f7f6;
	margin: 20px;
}

.form-container {
	width: 700px;
	margin: 0 auto;
	background-color: white;
	padding: 30px;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h2 {
	text-align: center;
	color: #007bff;
	margin-bottom: 25px;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	padding: 10px;
	border: 1px solid #ddd;
}

th {
	background-color: #f2f2f2;
	width: 15%;
	text-align: center;
}

input[type="text"], textarea {
	width: calc(100% - 10px);
	padding: 8px;
	margin: 0;
	box-sizing: border-box;
	border: 1px solid #ccc;
	border-radius: 4px;
}

textarea {
	height: 300px;
	resize: none;
}

.button-area {
	text-align: center;
	padding-top: 20px;
}

.button-area input[type="submit"], .button-area input[type="button"] {
	padding: 10px 20px;
	margin: 0 5px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
}

.button-area input[type="submit"] {
	background-color: #007bff;
	color: white;
}

.button-area input[type="submit"]:hover {
	background-color: #0056b3;
}

.button-area input[type="button"] {
	background-color: #6c757d;
	color: white;
}

.button-area input[type="button"]:hover {
	background-color: #5a6268;
}
</style>
</head>
<body>
	<div class="form-container">
		<h2>게시물 수정</h2>
		<form method="post" name="frm" action="../edit.do">
			<input type="hidden" name="num" value="${ dto.num }">
			<table>
				<tr>
					<th>작성자</th>
					<td>${ sessionScope.UserName }(${ sessionScope.UserId })</td>
				</tr>
				<tr>
					<th>제목</th>
					<td><input type="text" name="title" value="${ dto.title }"
						required></td>
				</tr>
				<tr>
					<th>내용</th>
					<td><textarea name="content" required>${ dto.content }</textarea></td>
				</tr>
			</table>
			<div class="button-area">
				<input type="submit" value="수정하기"> <input type="button"
					value="취소" onclick="history.back();">
			</div>
		</form>
	</div>
</body>
</html>