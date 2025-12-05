<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<header>
    <style>
        .menu-toggle {
            display: none; 
            font-size: 30px;
            cursor: pointer;
            padding: 0 10px;
            color: #333;
        }
        @media screen and (max-width: 768px) {
            .menu-toggle {
                display: block;
            }
            .pc-only-link {
                display: none !important;
            }
        }
    </style>
    <script>
        function toggleLeftMenu() {
            var menu = document.querySelector('.left-sidebar');
            menu.classList.toggle('active');
        }
    </script>
    
    <div style="background-color: #f8f9fa; 
    	padding: 10px 20px; border-bottom: 1px solid #e7e7e7; 
    	display: flex; justify-content: space-between; 
    	align-items: center;">
        
        <div style="display: flex; align-items: center;">
            <span class="menu-toggle" onclick="toggleLeftMenu()" style="margin-right: 10px;">☰</span>
            
            <h1 style="margin: 0; font-size: 24px;">
                <a href="<%= request.getContextPath() 
                	%>/main.do" style="text-decoration: none; color: #333;">2차 프로젝트!</a>
            </h1>
        </div>

        <nav style="display: flex; align-items: center;">
            <a href="<%= request.getContextPath() %>/board/list.do?type=free" class="pc-only-link" 
            	style="margin-right: 15px; text-decoration: none; color: #333;">메인화면</a>
            
            <c:choose>
                <c:when test="${not empty sessionScope.userID}">
                    <span style="font-weight: bold; color: #007bff; margin-right: 5px;">
                        ${sessionScope.userName}님
                    </span>
                    <span style="margin: 0 5px;">|</span>
                    <a href="<%= request.getContextPath() 
                    	%>/member/memberupdateform.do" style="text-decoration: none; color: #6c757d;">정보수정</a>
                    <span style="margin: 0 5px;">|</span>
                    <a href="<%= request.getContextPath() 
                    	%>/member/logoutaction.do" style="text-decoration: none; color: #dc3545;">로그아웃</a>
                </c:when>
                <c:otherwise>
                    <a href="<%= request.getContextPath() 
                    	%>/member/loginform.do" style="margin-right: 10px; text-decoration: none; color: #28a745;">로그인</a>
                    <a href="<%= request.getContextPath() 
                    	%>/member/joinform.do" style="text-decoration: none; color: #6c757d;">회원가입</a>
                </c:otherwise>
            </c:choose>
        </nav>
        
    </div>
</header>