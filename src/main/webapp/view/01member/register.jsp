<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
String contextPath = request.getContextPath();
String errorMessage = (String) request.getAttribute("RegisterErrorMessage");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입 - WevProject_PaekEH</title>
<style>
/* ... (기존 스타일 유지) ... */
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

.register-form-box { /* 폼 박스 스타일 */
	padding: 30px;
	border: 1px solid #ddd;
	border-radius: 8px;
	background-color: #ffffff;
	box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
	width: 350px;
	text-align: left;
}

.register-form-box label {
	display: block;
	margin-top: 10px;
	margin-bottom: 3px;
	font-weight: bold;
	color: #333;
}

.register-form-box input[type="text"], .register-form-box input[type="password"],
	.register-form-box input[type="email"], .register-form-box input[type="tel"]
	{
	width: 100%;
	padding: 8px;
	margin-bottom: 8px;
	border: 1px solid #ccc;
	border-radius: 4px;
	box-sizing: border-box;
}

.register-form-box input[type="submit"] {
	width: 100%;
	padding: 10px;
	background-color: #28a745; /* 가입은 초록색 */
	color: white;
	border: none;
	border-radius: 4px;
	cursor: pointer;
	font-size: 1.1em;
	margin-top: 25px;
}

.register-form-box input[type="submit"]:hover {
	background-color: #1e7e34;
}

.login-link {
	display: block;
	margin-top: 20px;
	text-align: center;
	color: #007bff;
	text-decoration: none;
}

.login-link:hover {
	text-decoration: underline;
}

.error-message {
	color: #cc0000;
	font-weight: bold;
	margin-bottom: 15px;
	text-align: center;
}

/* 메시지 표시 스타일 */
.validation-message {
	font-size: 0.9em;
	margin-bottom: 5px;
	display: block;
}

.validation-message.error {
	color: red;
	font-weight: bold;
}

.validation-message.info {
	color: gray;
}
</style>

<script>
	// 전역 변수로 아이디 중복 상태를 저장합니다.
    let isIdDuplicated = true; 
    let idCheckXhr = null; 
    
    // 이전에 정의했던 ALLOWED_DOMAINS 리스트 제거 (모든 도메인 허용)

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
     * [수정] 이메일 형식 검사: @와 . 포함 여부만 간단하게 검사합니다. (모든 도메인 허용)
     */
    function validateEmailFormat() {
        const emailField = document.getElementById('email');
        const emailMessage = document.getElementById('emailMessage');
        const emailValue = emailField.value.trim();

        emailMessage.textContent = ''; // 메시지 초기화
        emailMessage.className = 'validation-message';

        if (emailValue === '') {
            return true;
        }
        
        // ★★★ 핵심 수정: @와 .이 모두 포함되어 있는지 확인하는 기본 형식만 검사 ★★★
        const atIndex = emailValue.indexOf('@');
        const dotIndex = emailValue.lastIndexOf('.'); // 마지막 .의 위치 확인 (도메인 형식에 유리)

        // 1. @가 없거나, .이 없거나, @가 맨 앞이거나, @가 .보다 뒤에 있거나, .이 맨 뒤인 경우 등
        if (atIndex === -1 || dotIndex === -1 || atIndex === 0 || atIndex >= dotIndex) {
            emailMessage.textContent = '이메일 형식으로 만들어주세요! (예: user@domain.com)';
            emailMessage.className = 'validation-message error';
            emailField.focus(); 
            return false;
        }
        
        // 2. 도메인 부분이 최소 2글자 이상인지 확인 (예: .co, .kr, .com)
        const domainPart = emailValue.substring(dotIndex + 1);
        if (domainPart.length < 2) {
             emailMessage.textContent = '유효한 이메일 형식(도메인)이 아닙니다.';
             emailMessage.className = 'validation-message error';
             emailField.focus(); 
             return false;
        }


        // 모든 검사 통과 (모든 유효한 형식의 도메인 허용)
        return true;
    }


	/**
	 * 폼 제출 유효성 검사
	 */
	function validateForm() {
		
        // 0. 폼 제출 시 최종 이메일 형식 검사
        if (!validateEmailFormat()) {
            return false;
        }
        
		// 1. 아이디 중복 상태 확인
        if (isIdDuplicated) {
            alert('사용할 수 없는 아이디입니다. 다시 확인해 주세요.');
            document.getElementById('id').focus();
            return false;
        }
		
		// 2. 전화번호 유효성 검사
        const phoneField = document.getElementById('phone');
        const phoneValue = phoneField.value.replace(/[^0-9]/g, ""); 
        
        if (phoneValue.length > 0 && (phoneValue.length < 10 || phoneValue.length > 11)) {
             alert('전화번호를 올바르게 입력해주세요. (10자리 또는 11자리)');
             phoneField.focus();
             return false;
        }
        
		return true; // 제출 허용
	}
	
	/**
     * 아이디 중복을 AJAX로 확인하고 결과를 화면에 표시합니다.
     */
    function checkIdDuplication() {
        const idField = document.getElementById('id');
        const idMessage = document.getElementById('idMessage');
        const idValue = idField.value.trim();
        
        if (idValue.length < 4) {
            idMessage.textContent = '아이디는 최소 4자 이상 입력해야 확인됩니다.';
            idMessage.className = 'validation-message info';
            isIdDuplicated = true; 
            return;
        }

        if (idCheckXhr && idCheckXhr.readyState !== 4) {
            idCheckXhr.abort();
        }
        
        idMessage.textContent = '확인 중...';
        idMessage.className = 'validation-message info';

        const xhr = new XMLHttpRequest();
        idCheckXhr = xhr;
        const url = '<%=contextPath%>/member/idcheck.do'; 

        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onreadystatechange = function() {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        
                        idMessage.textContent = response.message;
                        if (response.isDuplicated) {
                            idMessage.className = 'validation-message error';
                            isIdDuplicated = true; 
                        } else {
                            idMessage.style.color = 'blue'; 
                            idMessage.className = 'validation-message'; 
                            isIdDuplicated = false; 
                        }
                    } catch (e) {
                        console.error("JSON 파싱 오류:", e);
                        idMessage.textContent = '통신 오류 발생.';
                        idMessage.className = 'validation-message error';
                        isIdDuplicated = true; 
                    }
                } else {
                     if (xhr.status !== 0) {
                        idMessage.textContent = '서버 통신 실패 (' + xhr.status + ')';
                        idMessage.className = 'validation-message error';
                        isIdDuplicated = true; 
                    }
                }
            }
        };
    
        xhr.send('id=' + encodeURIComponent(idValue));
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
			<a href="<%=contextPath%>/member/login.do">로그인</a> <a
				href="<%=contextPath%>/member/register.do">회원가입</a>
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
			<h2>회원 가입</h2>

			<%
			if (errorMessage != null) {
			%>
			<p class="error-message"><%=errorMessage%></p>
			<%
			}
			%>

			<div class="register-form-box">
				<form action="<%=contextPath%>/member/register.do" method="post"
					onsubmit="return validateForm()">

					<label for="id">아이디</label> <input type="text" id="id" name="id"
						required placeholder="아이디" maxlength="20"
						value="<%=(request.getParameter("id") != null) ? request.getParameter("id") : ""%>"
						oninput="checkIdDuplication()"> <span id="idMessage"
						class="validation-message info">아이디는 최소 4자 이상 입력해야 확인됩니다.</span> <label
						for="pass">패스워드</label> <input type="password" id="pass"
						name="pass" required placeholder="비밀번호" maxlength="20"> <label
						for="name">이름</label> <input type="text" id="name" name="name"
						required placeholder="이름" maxlength="30"> <label
						for="email">이메일</label> <input type="email" id="email"
						name="email" required placeholder="example@domain.com"
						maxlength="100" onblur="validateEmailFormat()"> <span
						id="emailMessage" class="validation-message"></span> <label
						for="phone">전화번호</label> <input type="tel" id="phone" name="phone"
						required placeholder="010-1234-5678"
						oninput="formatPhoneNumber(this)"> <input type="submit"
						value="회원가입 완료">
				</form>
				<a href="<%=contextPath%>/member/login.do" class="login-link">이미
					회원이신가요? 로그인</a>
			</div>
		</div>
	</div>

</body>
</html>