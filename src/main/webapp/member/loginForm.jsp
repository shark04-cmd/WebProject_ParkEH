<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    // 쿠키에서 저장된 아이디 가져오기
    String savedID = "";
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie c : cookies) {
            if (c.getName().equals("savedID")) {
                savedID = c.getValue();
                break;
            }
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        .login-box {
            width: 300px;
            margin: 50px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 5px;
            text-align: center;
        }
        .input-group {
            margin-bottom: 15px;
        }
        .input-group input[type="text"], 
        .input-group input[type="password"] {
            width: 100%;
            padding: 10px;
            box-sizing: border-box;
            border: 1px solid #ddd;
            border-radius: 3px;
        }
        .login-button {
            width: 100%;
            padding: 10px;
            background-color: #007bff;
            color: white;
            border: none;
            border-radius: 3px;
            cursor: pointer;
        }
        .error-msg {
            color: red;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
    <%@ include file="/include/header.jsp" %>
    
    <div class="login-box">
        <h2>로그인</h2>
        
        <c:if test="${not empty msg}">
            <p class="error-msg">${msg}</p>
        </c:if>
        
        <form action="<%= request.getContextPath() %>/member/loginaction.do" method="post">
            <div class="input-group">
                <input type="text" name="id" placeholder="아이디" required
                       value="<%= savedID %>">
            </div>
            <div class="input-group">
                <input type="password" name="pass" placeholder="비밀번호" required>
            </div>
            
            <div style="text-align: left; margin-bottom: 15px;">
                <input type="checkbox" id="saveid" name="saveid" value="true"
                       <%= (savedID.length() > 0) ? "checked" : "" %>>
                <label for="saveid">아이디 저장</label>
            </div>
            
            <button type="submit" class="login-button">로그인</button>
        </form>
        
        <p style="margin-top: 20px;">
            계정이 없으신가요? <a href="<%= request.getContextPath() %>/member/joinForm.do">회원가입</a>
        </p>
    </div>
</body>
</html>