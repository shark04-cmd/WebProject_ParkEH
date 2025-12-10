<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<script type="text/javascript">
	function validateForm(form) {
		if (form.id.value == "") {
			alert("아이디를 입력하세요.");
			form.id.focus();
			return false;
		}
		if (form.pass.value == "") {
			alert("비밀번호를 입력하세요.");
			form.pass.focus();
			return false;
		}
	}
</script>
<style>
.login-container {
	width: 400px;
	margin: 100px auto;
	padding: 40px;
	border: 1px solid #ddd;
	background-color: white;
	border-radius: 8px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
	text-align: center;
	margin-bottom: 30px;
	color: #333;
}

table {
	width: 100%;
}

th, td {
	padding: 10px 0;
}

input[type="text"], input[type="password"] {
	width: 100%;
	padding: 10px;
	margin-bottom: 10px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.button-group {
	text-align: center;
	padding-top: 10px;
}

.button-group button {
	padding: 10px 20px;
	margin: 0 5px;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-weight: bold;
}

.submit-btn {
	background-color: #007bff;
	color: white;
}
</style>
</head>
<body>
	<div class="login-container">
		<h2>계정 로그인</h2>
		<form name="loginFrm" method="post" action="LoginProcess.jsp"
			onsubmit="return validateForm(this);">
			<table>
				<tr>
					<th>아이디</th>
					<td><input type="text" name="id" placeholder="ID" /></td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" name="pass" placeholder="Password" /></td>
				</tr>
			</table>
			<div class="button-group">
				<button type="submit" class="submit-btn">로그인</button>
				<button type="button" onclick="location.href='../Default.jsp'">메인으로</button>
			</div>
		</form>
	</div>
</body>
</html>