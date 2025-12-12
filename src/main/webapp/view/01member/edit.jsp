<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="member.MemberDTO"%>
<%
String contextPath = request.getContextPath();

MemberDTO dto = (MemberDTO) request.getAttribute("dto");
String userId = (String) session.getAttribute("UserID");

if (dto == null || userId == null || !userId.equals(dto.getId())) {
	response.sendRedirect(contextPath + "/member/login.do");
	return;
}

String errorMessage = (String) request.getAttribute("EditErrorMessage");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원정보 수정 - WevProject_PaekEH</title>
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
	display: flex;
	align-items: center;
	font-size: 1.2em;
	font-weight: bold;
	color: white;
}

.menu-toggle {
	font-size: 1.5em;
	cursor: pointer;
	margin-right: 15px;
	display: none;
	color: white;
	padding: 0 5px;
	line-height: 1;
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
	flex-shrink: 0;
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
	display: flex;
	flex-direction: column;
	justify-content: flex-start;
	align-items: center;
	text-align: center;
}

.main-content h2 {
	color: #0056b3;
	margin-bottom: 30px;
}
.edit-form-box {
	padding: 30px;
	border: 1px solid #ddd;
	border-radius: 8px;
	background-color: #ffffff;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	width: 350px;
	text-align: left;
}

.edit-form-box label {
	display: block;
	margin-top: 10px;
	margin-bottom: 3px;
	font-weight: bold;
	color: #333;
}

.edit-form-box input[type="text"], .edit-form-box input[type="password"],
	.edit-form-box input[type="email"], .edit-form-box input[type="tel"] {
	width: 100%;
	padding: 8px;
	margin-bottom: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.edit-form-box input[type="submit"] {
	width: 100%;
	padding: 10px;
	background-color: #ffc107; /* 수정 버튼 색상 */
	color: #333;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1.1em;
	margin-top: 25px;
}

.edit-form-box input[type="submit"]:hover {
	background-color: #e0a800;
}

.edit-form-box .btn-cancel {
	display: block;
	width: 100%;
	padding: 10px;
	margin-top: 10px;
	background-color: #6c757d;
	color: white;
	text-align: center;
	text-decoration: none;
	border-radius: 4px;
}

.edit-form-box .btn-cancel:hover {
	background-color: #5a6268;
}

.error-message {
	color: #cc0000;
	font-weight: bold;
	margin-bottom: 15px;
	text-align: center;
}

.info-text {
	font-size: 0.9em;
	color: #555;
	margin-bottom: 15px;
	display: block;
}

@media ( max-width : 1000px) {
	.sidebar {
		display: none;
		position: absolute;
		z-index: 1000;
		height: auto;
		min-height: calc(100vh - 44px);
		box-shadow: 2px 0 5px rgba(0, 0, 0, 0.5);
	}
	.menu-toggle {
		display: block;
	}
	.header-left a {
		margin-left: 0;
		padding-left: 0;
	}
	.container {
		flex-direction: column;
	}
	.main-content {
		padding: 20px;
	}
	.edit-form-box {
		width: 100%;
		max-width: 450px;
	}
}
</style>
<script>
	function toggleSidebar() {
		const sidebar = document.querySelector('.sidebar');
		if (sidebar) {
			if (sidebar.style.display === 'block') {
				sidebar.style.display = 'none';
			} else {
				sidebar.style.display = 'block';
			}
		}
	}

	/**
	 * 숫자만 허용하고, 4번째와 9번째에 하이픈(-)을 자동으로 추가합니다. (전화번호)
	 */
	function formatPhoneNumber(currentField) {
		
		let number = currentField.value.replace(/[^0-9]/g, "");

		if (number.length > 11) {
            number = number.substring(0, 11);
        }

        let formattedNumber = '';
        
        if (number.length > 3 && number.length <= 7) {
            formattedNumber = number.substring(0, 3) + '-' + number.substring(3);
        } else if (number.length > 7) {
            formattedNumber = number.substring(0, 3) + '-' + number.substring(3, 7) + '-' + number.substring(7);
        } else {
            formattedNumber = number;
        }

        currentField.value = formattedNumber;
        currentField.maxLength = 13;
	}


	function validateForm(form) {
		const newPass = form.newPass.value;
		const pass = form.pass.value;
		const phoneField = form.phone;
		
		if (form.name.value.trim() === "") {
			alert("이름을 입력하세요.");
			form.name.focus();
			return false;
		}
		
        const phoneValue = phoneField.value.replace(/[^0-9]/g, ""); 
        
        if (phoneValue.length > 0 && (phoneValue.length < 10 || phoneValue.length > 11)) {
             alert('전화번호를 올바르게 입력해주세요. (10자리 또는 11자리)');
             phoneField.focus();
             return false;
        }

		if (pass.trim() === "") {
			alert("현재 비밀번호를 입력해야 정보를 수정할 수 있습니다.");
			form.pass.focus();
			return false;
		}

		if (newPass.trim() !== "" && newPass.length < 4) {
			alert("새 비밀번호는 4자 이상이어야 합니다.");
			form.newPass.focus();
			return false;
		}
		
		return true;
	}
	
	function goHome() {
		window.location.href = "<%=contextPath%>/Default.jsp";
	}
</script>
</head>
<body>

	<div class="header">
		<div class="header-left">
			<span class="menu-toggle" onclick="toggleSidebar()">&#9776;</span> <a
				href="<%=contextPath%>/Default.jsp"
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
				<li><a href="<%=contextPath%>/board/list.do?boardType=free">자유
						게시판</a></li>
				<li><a href="<%=contextPath%>/board/list.do?boardType=qna">Q&A
						게시판</a></li>
				<li><a href="<%=contextPath%>/board/list.do?boardType=data">자료실
						게시판</a></li>
			</ul>
		</div>

		<div class="main-content">
			<h2>회원 정보 수정</h2>

			<%
			if (errorMessage != null) {
			%>
			<p class="error-message"><%=errorMessage%></p>
			<%
			}
			%>

			<div class="edit-form-box">
				<form action="<%=contextPath%>/member/edit.do" method="post"
					onsubmit="return validateForm(this);">
					<label for="id">아이디</label> <input type="text" id="id" name="id"
						value="<%=dto.getId()%>" readonly style="background-color: #eee;">

					<label for="name">이름</label> 
					<input type="text" id="name" name="name" required placeholder="이름" 
						value="<%=dto.getName()%>" maxlength="30"> <label for="email">
						이메일
					</label> 
					<input type="email" id="email" name="email" 
						required placeholder="example@domain.com" maxlength="100"
						value="<%=dto.getEmail() != null ? dto.getEmail() : ""%>">

					<label for="phone">
						전화번호
					</label> 
					<input type="tel" id="phone" name="phone" 
						required placeholder="010-1234-5678"
						oninput="formatPhoneNumber(this)"
						value="<%=dto.getPhone() != null ? dto.getPhone() : ""%>">

					<label for="newPass">새 비밀번호 (선택)</label> <input type="password"
						id="newPass" name="newPass" 
						placeholder="새 비밀번호 (변경하지 않으려면 비워두세요)" maxlength="20"> 
					<span class="info-text">
						비밀번호를 변경하지 않으려면 비워두세요.
					</span> 
					<label for="pass">
						현재 비밀번호 (필수 인증)
					</label> 
					<input type="password" id="pass" name="pass" 
						required placeholder="정보 수정을 위해 현재 비밀번호를 입력하세요" maxlength="20"> 
					<input
						type="submit" value="정보 수정 완료">
				</form>
				<a href="javascript:goHome()" class="btn-cancel">취소/메인으로</a>
			</div>
		</div>
	</div>

</body>
</html>