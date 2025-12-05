<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원 정보 수정</title>
    <style>
        .form-container { width: 400px; margin: 50px auto; padding: 20px; border: 1px solid #ddd; background-color: #f9f9f9; }
        .form-container input:not([type="button"]) { width: 100%; padding: 10px; margin: 8px 0; box-sizing: border-box; border: 1px solid #ccc; }
        .form-container button { width: 100%; padding: 10px; margin-top: 15px; background-color: #007bff; color: white; border: none; cursor: pointer; }
        .form-container .cancel-btn { background-color: #6c757d; margin-top: 5px; }
        .error-msg { color: red; font-weight: bold; margin-bottom: 15px; text-align: center; }
        .readonly-input { background-color: #eee; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: bold; }
    </style>
</head>
<body>
    
    <%@ include file="/include/header.jsp" %>
    
    <c:set var="member" value="${requestScope.member}"/>

    <div class="form-container">
        <h2 style="text-align: center; color: #007bff;">회원 정보 수정</h2>
        
        <c:if test="${not empty requestScope.msg}">
            <p class="error-msg">${requestScope.msg}</p>
        </c:if>

        <form action="<%= request.getContextPath() %>/member/memberupdateaction.do" method="post">
            
            <div class="form-group">
                <label>아이디</label>
                <input type="text" name="id" value="${member.id}" class="readonly-input" readonly>
            </div>
            
            <div class="form-group">
                <label>새 비밀번호</label>
                <input type="password" name="pass" placeholder="변경할 비밀번호 (필수)" required 
                       value="${member.pass}">
            </div>
            
            <div class="form-group">
                <label>비밀번호 확인</label>
                <input type="password" name="pass2" placeholder="비밀번호 확인 (필수)" required>
            </div>
            
            <div class="form-group">
                <label>이름</label>
                <input type="text" name="name" placeholder="이름 (필수)" required
                       value="${member.name}">
            </div>
            
            <div class="form-group">
                <label>이메일</label>
                <input type="email" name="email" placeholder="이메일 (필수)" required
                       value="${member.email}">
            </div>
            
            <div class="form-group">
                <label>전화번호</label>
                <input type="text" name="phoneNum" placeholder="전화번호 (예: 010-1234-5678) (필수)" required
                       value="${member.phoneNum}">
            </div>
            
            <button type="submit">정보 수정 완료</button>
            <button type="button" class="cancel-btn" 
                    onclick="location.href='<%= request.getContextPath() %>/main.do'">
                취소
            </button>
        </form>
    </div>
</body>
</html>
