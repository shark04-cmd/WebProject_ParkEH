<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 가입</title>
    <style>
        /* CSS 스타일 생략 (이전 답변과 동일) */
        .form-container { width: 400px; margin: 50px auto; padding: 20px; border: 1px solid #ddd; background-color: #f9f9f9; }
        .form-container input { width: 100%; padding: 10px; margin: 8px 0; box-sizing: border-box; border: 1px solid #ccc; }
        .form-container button { width: 100%; padding: 10px; margin-top: 15px; background-color: #28a745; color: white; border: none; cursor: pointer; }
        .error-msg { color: red; font-weight: bold; margin-bottom: 15px; }
    </style>
</head>
<body>
    
    <%@ include file="/include/header.jsp" %>

    <div class="form-container">
        <h2 style="text-align: center; color: #333;">회원 가입</h2>
        
        <c:if test="${not empty requestScope.msg}">
            <p class="error-msg">${requestScope.msg}</p>
        </c:if>

        <form action="<%= request.getContextPath() %>/member/JoinAction.do" method="post">
            <input type="text" name="id" placeholder="아이디 (필수)" required
                   value="${requestScope.member.id}">
            <input type="password" name="pass" placeholder="비밀번호 (필수)" required>
            <input type="password" name="pass2" placeholder="비밀번호 확인 (필수)" required>
            <input type="text" name="name" placeholder="이름 (필수)" required
                   value="${requestScope.member.name}">
            <input type="email" name="email" placeholder="이메일 (필수)" required
                   value="${requestScope.member.email}">
            <input type="text" name="phoneNum" placeholder="전화번호 (예: 010-1234-5678) (필수)" required
                   value="${requestScope.member.phoneNum}">
            
            <button type="submit">가입 완료</button>
            <p style="text-align: center; margin-top: 15px;">
                <a href="<%= request.getContextPath() %>/member/loginForm.do" style="color: #007bff;">로그인 페이지로 돌아가기</a>
            </p>
        </form>
    </div>
</body>
</html>
