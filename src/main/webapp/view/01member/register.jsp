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
/* ... (스타일 생략 - 이전과 동일) ... */
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

.phone-group { /* 전화번호 필드 그룹을 위한 CSS 추가 */
	display: flex;
	justify-content: space-between;
	align-items: center;
	margin-bottom: 8px;
}

.phone-group input {
	width: calc(33% - 15px); /* 필드 너비 조정 */
	padding: 8px 4px; /* 패딩 조정 */
	text-align: center;
}

.phone-separator {
	font-size: 1.2em;
	font-weight: bold;
	color: #555;
	margin: 0 5px;
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
</style>

<script>
	// 전역 변수로 아이디 중복 상태를 저장합니다.
    let isIdDuplicated = false; 
    let idCheckXhr = null; 

	/**
	 * 전화번호 입력 필드 간 자동 포커스 이동 및 숫자만 입력 허용
	 * @param {HTMLInputElement} currentField 현재 입력 필드
	 * @param {number} maxLength 현재 필드의 최대 길이
	 * @param {string} nextId 다음으로 이동할 필드의 ID (없으면 null)
	 */
	function autoMove(currentField, maxLength, nextId) {
		
		// 1. 숫자 외의 문자 제거 (혹시 type=tel이 아닌 다른 타입으로 복붙된 경우 대비)
		currentField.value = currentField.value.replace(/[^0-9]/g, "");

		// 2. 최대 길이와 현재 입력 길이를 비교하여 포커스 이동
		// oninput 이벤트는 문자열이 변경될 때마다 발생
		if (currentField.value.length === maxLength) {
			
			if (nextId) {
				const nextField = document.getElementById(nextId);
				if (nextField) {
					nextField.focus(); // 다음 필드로 포커스 이동
				}
			}
		}
	}
	
	/**
	 * 키 누름 시 숫자만 입력되도록 처리
	 * @param {KeyboardEvent} event
	 */
	function isNumberKey(event) {
		const charCode = (event.which) ? event.which : event.keyCode;
		// 0-9 숫자 범위 (48-57), 백스페이스(8), 탭(9), 엔터(13), 화살표(37-40) 등 허용
		if (charCode > 31 && (charCode < 48 || charCode > 57)) {
			// 숫자 외의 문자는 입력 방지
			event.preventDefault(); 
			return false;
		}
		return true;
	}


	/**
	 * 폼 제출 전에 3개 필드의 값을 조합하여 숨겨진 필드(name="phone")에 저장
	 */
	function combinePhoneNumber() {
		const p1 = document.getElementById('phone1').value;
		const p2 = document.getElementById('phone2').value;
		const p3 = document.getElementById('phone3').value;
		const combinedInput = document.getElementById('phone');

		// 모든 필드가 비어있지 않은 경우에만 조합하여 전송합니다.
		if (p1 && p2 && p3) {
			combinedInput.value = `${p1}-${p2}-${p3}`;
		} else {
			combinedInput.value = "";
		}

		// 필수 필드인지 확인
		if (!combinedInput.value && combinedInput.required) {
			alert('전화번호를 올바르게 입력해주세요.');
			return false; // 제출 방지
		}
		
		return true; // 제출 허용
	}
	
	/**
     * 아이디 중복을 AJAX로 확인하고 결과를 화면에 표시합니다.
     * oninput 이벤트로 실시간 실행됩니다.
     */
    function checkIdDuplication() {
        const idField = document.getElementById('id');
        const idMessage = document.getElementById('idMessage');
        const idValue = idField.value.trim();
        
        // 입력 값이 비어있거나 너무 짧으면 (4자 미만) 메시지 초기화 및 가입 방지
        if (idValue.length < 4) {
            idMessage.textContent = '아이디는 최소 4자 이상 입력해야 확인됩니다.';
            idMessage.style.color = 'gray';
            isIdDuplicated = true; // 짧은 상태는 중복/사용 불가로 간주
            return;
        }

        // 이전 AJAX 요청이 있다면 취소합니다
        if (idCheckXhr && idCheckXhr.readyState !== 4) {
            idCheckXhr.abort();
        }
        
        idMessage.textContent = '확인 중...';
        idMessage.style.color = 'orange';

        // AJAX 요청
        const xhr = new XMLHttpRequest();
        idCheckXhr = xhr;
        const url = '<%=contextPath%>/member/idcheck.do'; 

        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onreadystatechange = function() {
            // 요청이 취소되지 않고, 완료되었는지 확인
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    try {
                        const response = JSON.parse(xhr.responseText);
                        
                        // 결과에 따라 메시지 표시
                        idMessage.textContent = response.message;
                        if (response.isDuplicated) {
                            // 중복인 경우: 빨간색
                            idMessage.style.color = 'red';
                            isIdDuplicated = true; 
                        } else {
                            // 사용 가능한 경우: 파란색/초록색
                            idMessage.style.color = 'blue';
                            isIdDuplicated = false; 
                        }
                    } catch (e) {
                        console.error("JSON 파싱 오류:", e);
                        idMessage.textContent = '통신 오류 발생.';
                        idMessage.style.color = 'red';
                        isIdDuplicated = true; 
                    }
                } else {
                     // 404, 500 등의 서버 오류
                     if (xhr.status !== 0) {
                        idMessage.textContent = '서버 통신 실패 (' + xhr.status + ')';
                        idMessage.style.color = 'red';
                        isIdDuplicated = true; 
                    }
                }
            }
        };
    
        // 데이터 전송
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
					onsubmit="return combinePhoneNumber()">

					<input type="hidden" id="phone" name="phone" required> <label
						for="id">아이디</label> <input type="text" id="id" name="id" required
						placeholder="아이디" maxlength="20"
						value="<%=(request.getParameter("id") != null) ? request.getParameter("id") : ""%>"
						oninput="checkIdDuplication()"> <span id="idMessage"
						style="font-size: 0.9em; margin-bottom: 5px; display: block; color: gray;">아이디는
						최소 4자 이상 입력해야 확인됩니다.</span> <label for="pass">패스워드</label> <input
						type="password" id="pass" name="pass" required placeholder="비밀번호"
						maxlength="20"> <label for="name">이름</label> <input
						type="text" id="name" name="name" required placeholder="이름"
						maxlength="30"> <label for="email">이메일</label> <input
						type="email" id="email" name="email" required
						placeholder="example@domain.com" maxlength="100"> <label
						for="phone">전화번호 (3-4-4 자리)</label>
					<div class="phone-group">
						<input type="tel" id="phone1" maxlength="3" required
							placeholder="010" oninput="autoMove(this, 3, 'phone2')"
							onkeypress="return isNumberKey(event)"> <span
							class="phone-separator">-</span> <input type="tel" id="phone2"
							maxlength="4" required placeholder="0000"
							oninput="autoMove(this, 4, 'phone3')"
							onkeypress="return isNumberKey(event)"> <span
							class="phone-separator">-</span> <input type="tel" id="phone3"
							maxlength="4" required placeholder="0000"
							oninput="autoMove(this, 4, null)"
							onkeypress="return isNumberKey(event)">
					</div>

					<input type="submit" value="회원가입 완료">
				</form>
				<a href="<%=contextPath%>/member/login.do" class="login-link">이미
					회원이신가요? 로그인</a>
			</div>
		</div>
	</div>

</body>
</html>