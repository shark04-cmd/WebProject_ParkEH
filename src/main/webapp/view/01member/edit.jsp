<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="member.MemberDTO"%>
<%
String contextPath = request.getContextPath();
String userId = (String) session.getAttribute("UserID");

// Controller에서 전달된 회원 정보 DTO
MemberDTO memberInfo = (MemberDTO) request.getAttribute("memberInfo");

// Controller에서 전달된 에러 메시지
String errorMessage = (String) request.getAttribute("EditErrorMessage");

// memberInfo가 null이면 Controller에서 로그인 체크를 통과하지 못했거나 DB 조회에 실패한 경우
if (memberInfo == null) {
	// 로그인 페이지로 강제 이동 (Controller에서 이미 처리했겠지만 안전장치)
	if (userId == null || userId.isEmpty()) {
		response.sendRedirect(contextPath + "/member/login.do");
	} else {
		// 세션은 있는데 정보가 없는 특이 케이스
		response.sendRedirect(contextPath + "/Default.jsp");
	}
	return;
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보 수정 - WevProject_PaekEH</title>
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
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	text-align: center;
}

.main-content h2 {
	color: #0056b3;
	margin-bottom: 30px;
}

.edit-form-box { /* 폼 박스 스타일 */
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
	background-color: #007bff; /* 수정은 파란색 */
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1.1em;
	margin-top: 25px;
}

.edit-form-box input[type="submit"]:hover {
	background-color: #0056b3;
}

.error-message {
	color: #cc0000;
	font-weight: bold;
	margin-bottom: 15px;
	text-align: center;
}

.validation-message {
	font-size: 0.9em;
	margin-bottom: 5px;
	display: block;
}

.validation-message.error {
	color: red;
	font-weight: bold;
}

.validation-message.success {
	color: blue;
	font-weight: bold;
}
</style>

<script>
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

    /**
     * 이메일 형식 검사: @와 . 포함 여부만 간단하게 검사합니다.
     */
    function validateEmailFormat() {
        const emailField = document.getElementById('email');
        const emailMessage = document.getElementById('emailMessage');
        const emailValue = emailField.value.trim();

        emailMessage.textContent = ''; 
        emailMessage.className = 'validation-message';

        if (emailValue === '') {
            return true;
        }
        
        const atIndex = emailValue.indexOf('@');
        const dotIndex = emailValue.lastIndexOf('.'); 

        if (atIndex === -1 || dotIndex === -1 || atIndex === 0 || atIndex >= dotIndex) {
            emailMessage.textContent = '이메일 형식으로 만들어주세요! (예: user@domain.com)';
            emailMessage.className = 'validation-message error';
            emailField.focus(); 
            return false;
        }
        
        const domainPart = emailValue.substring(dotIndex + 1);
        if (domainPart.length < 2) {
             emailMessage.textContent = '유효한 이메일 형식(도메인)이 아닙니다.';
             emailMessage.className = 'validation-message error';
             emailField.focus(); 
             return false;
        }

        return true;
    }
    
	/**
	 * 폼 제출 유효성 검사 (현재 패스워드 필수 입력 포함)
	 */
	function validateForm() {
        
        // 1. 현재 패스워드 필수 입력 확인
        const currentPassField = document.getElementById('currentPass');
        if (currentPassField.value.trim() === '') {
        	alert('정보 수정을 위해 현재 비밀번호를 반드시 입력해야 합니다.');
        	currentPassField.focus();
        	return false;
        }
        
        // 2. 이름은 필수 입력
        const nameField = document.getElementById('name');
        if (nameField.value.trim() === '') {
        	alert('이름을 입력해주세요.');
        	nameField.focus();
        	return false;
        }
        
        // 3. 이메일 형식 검사
        if (!validateEmailFormat()) {
            return false;
        }
        
		// 4. 전화번호 유효성 검사 (빈 값은 통과, 입력 시 형식 검사)
        const phoneField = document.getElementById('phone');
        const phoneValue = phoneField.value.replace(/[^0-9]/g, ""); 
        
        if (phoneValue.length > 0 && (phoneValue.length < 10 || phoneValue.length > 11)) {
             alert('전화번호를 올바르게 입력해주세요. (10자리 또는 11자리)');
             phoneField.focus();
             return false;
        }
        
        const newPassField = document.getElementById('pass');
        // 5. 새 비밀번호 입력 시, 최소 4자 이상인지 확인 
        if (newPassField.value.length > 0 && newPassField.value.length < 4) {
        	alert('새 비밀번호를 변경하려면 최소 4자 이상 입력해야 합니다.');
            newPassField.focus();
            return false;
        }
        
		return true; // 제출 허용
	}
	
	// 페이지 로드 시
	window.onload = function() {
        // 1. 전화번호 형식 적용
        const phoneField = document.getElementById('phone');
        if (phoneField.value) {
            formatPhoneNumber(phoneField);
        }
        
        // 2. 정보 수정 성공 메시지 확인
        const successParam = new URLSearchParams(window.location.search).get('editSuccess');
        if (successParam === 'true') {
            alert('회원 정보가 성공적으로 수정되었습니다.');
            // 알림 후 URL에서 파라미터 제거
            history.replaceState(null, '', location.pathname + location.search.replace('?editSuccess=true', '').replace('&editSuccess=true', ''));
        }
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
					onsubmit="return validateForm()">

					<label for="id">아이디</label> <input type="text" id="id" name="id"
						value="<%=memberInfo.getId()%>" readonly
						style="background-color: #eee;"> <label for="currentPass">현재
						패스워드 <span style="color: red;">(필수)</span>
					</label> <input type="password" id="currentPass" name="currentPass"
						required placeholder="본인 확인을 위해 현재 비밀번호를 입력해주세요" maxlength="20">

					<label for="pass">새 패스워드</label> <input type="password" id="pass"
						name="pass" placeholder="변경하려면 입력 (최소 4자)" maxlength="20">
					<span class="validation-message"
						style="color: gray; font-size: 0.85em; margin-bottom: 15px;">※
						변경을 원치 않으면 비워두세요.</span> <label for="name">이름</label> <input type="text"
						id="name" name="name" required placeholder="이름" maxlength="30"
						value="<%=memberInfo.getName()%>"> <label for="email">이메일</label>
					<input type="email" id="email" name="email" required
						placeholder="example@domain.com" maxlength="100"
						onblur="validateEmailFormat()" value="<%=memberInfo.getEmail()%>">
					<span id="emailMessage" class="validation-message"></span> <label
						for="phone">전화번호</label> <input type="tel" id="phone" name="phone"
						required placeholder="010-1234-5678"
						oninput="formatPhoneNumber(this)"
						value="<%=memberInfo.getPhone()%>"> <input type="submit"
						value="정보 수정 완료">
				</form>

			</div>
		</div>
	</div>

</body>
</html>