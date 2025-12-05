<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 작성</title>
    <style>
        .write-container { 
            width: 80%; 
            max-width: 800px; 
            margin: 50px auto; 
            padding: 30px; 
            border: 1px solid #007bff; 
            box-shadow: 0 0 10px rgba(0, 123, 255, 0.1);
            background-color: white;
        }
        .write-container h2 { text-align: center; border-bottom: 2px solid #ccc; padding-bottom: 10px; margin-bottom: 20px; }
        .write-container input[type="text"], 
        .write-container textarea { 
            width: 100%; 
            padding: 10px; 
            margin-bottom: 15px; 
            border: 1px solid #ddd; 
            box-sizing: border-box; 
            resize: vertical; 
            min-height: 200px; /* 내용 최소 높이 */
        }
        .write-container .form-group { margin-bottom: 15px; }
        .write-container button { 
            padding: 10px 20px; 
            background-color: #007bff; 
            color: white; 
            border: none; 
            cursor: pointer;
            margin-right: 10px;
        }
        .write-container .cancel-btn { background-color: #6c757d; }
        .error-msg { color: red; font-weight: bold; margin-bottom: 15px; text-align: center; }
    </style>
</head>
<body>
    
    <%@ include file="/include/header.jsp" %>
    
    <div class="write-container">
        <h2 style="color: #007bff;">
            ${param.type == 'free' ? '자유 게시판' : param.type == 'qna' ? 'Q&A 게시판' : '자료실 게시판'} 글쓰기
        </h2>
        
        <c:if test="${not empty requestScope.msg}">
            <p class="error-msg">${requestScope.msg}</p>
        </c:if>

        <form action="<%= request.getContextPath() %>/board/WriteAction.do" method="post">
            <input type="hidden" name="type" value="${param.type}"> 
            
            <div class="form-group">
                <input type="text" name="subject" placeholder="제목을 입력하세요" required 
                       value="${requestScope.board.subject}" maxlength="100">
            </div>
            
            <div class="form-group">
                <textarea name="content" placeholder="내용을 입력하세요" required>${requestScope.board.content}</textarea>
            </div>
            
            <div style="text-align: right;">
                <button type="submit">등록</button>
                <button type="button" class="cancel-btn" 
                        onclick="location.href='<%= request.getContextPath() %>/board/list.do?type=${param.type}'">
                    취소
                </button>
            </div>
        </form>
    </div>

</body>
</html>
