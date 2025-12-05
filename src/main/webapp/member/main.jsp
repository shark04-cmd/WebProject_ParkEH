<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>WebProject 게시판 메인</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"> 
    <style>
        html, body {
            margin: 0;
            padding: 0;
            height: 100%; 
            width: 100%;
            overflow-x: hidden; 
        }
        
        * { box-sizing: border-box; } 
        
        .left-sidebar .menu-separator {
            display: none; 
        }
        
        .main-container {
            display: flex; 
            width: 100%; 
            margin: 0; 
            min-height: calc(100vh - 60px); 
            background-color: #e6f0ff;
        }

        .left-sidebar {
            width: 20%; 
            padding: 15px; 
            background-color: #d1e5ff;
            min-height: 100%; 
        }
        .main-content {
            width: 60%; 
            padding: 15px; 
            background-color: white;
            min-height: 100%; 
        }
        .right-sidebar {
            width: 20%;
            padding: 15px;
            background-color: #e6f0ff;
            min-height: 100%;
        }
        
        .board-table { width: 100%; border-collapse: collapse; margin-top: 15px; font-size: 14px; }
        .board-table th, .board-table td { border: 1px solid #ddd; padding: 8px; text-align: center; }
        .board-table th { background-color: #f2f2f2; }
        .board-table .subject-link { text-align: left; padding-left: 10px; }

        @media screen and (max-width: 768px) {
            .main-container {
                flex-direction: column; 
                min-height: auto; 
            }
            .main-content {
                width: 100%; 
                min-height: auto;
                border-bottom: 1px solid #ccc;
                padding: 10px;
            }
            
            .left-sidebar {
                position: fixed; 
                top: 60px; 
                left: -100%; 
                height: 100%; 
                max-width: 250px; 
                width: 70%; 
                background-color: #d1e5ff;
                z-index: 1000; 
                transition: left 0.3s ease; 
                padding-top: 20px;
                border-right: 1px solid #ccc;
                overflow-y: auto; 
            }
            .left-sidebar.active {
                left: 0; 
            }
            .left-sidebar .menu-separator { 
                display: block !important; 
                border-top: 1px solid #a0c3f0; 
                margin: 15px 0; 
                padding-top: 15px; 
            }

            .right-sidebar {
                display: none;
            }
            
            .pc-only-link {
                display: none !important;
            }
            
            .main-content { 
                order: 1; 
            }
            
            .board-table th:nth-child(4), .board-table td:nth-child(4), 
            .board-table th:nth-child(5), .board-table td:nth-child(5) { 
                display: none; 
            }
        }
    </style>
</head>
<body>
    
    <%@ include file="/include/header.jsp" %>
    
    <div class="main-container">
        
        <div class="left-sidebar">
            <h3 style="color: #004d99; border-bottom: 2px solid #004d99; padding-bottom: 5px;">게시판 목록</h3>
            
            <p style="margin-top: 10px;">
                <a href="<%= request.getContextPath() %>/board/list.do?type=free" 
                   style="display: block; padding: 5px; 
                   text-decoration: none; color: 
                   ${boardType == 'free' ? 'white' : '#004d99'}; 
                   background-color: ${boardType == 'free' ? '#007bff' : 'transparent'}; 
                   font-weight: ${boardType == 'free' ? 'bold' : 'normal'};">자유 게시판</a>
            </p>
            <p>
                <a href="<%= request.getContextPath() %>/board/list.do?type=qna" 
                   style="display: block; padding: 5px; 
                   text-decoration: none; color: ${boardType == 'qna' ? 'white' : '#004d99'}; 
                   background-color: ${boardType == 'qna' ? '#007bff' : 'transparent'}; 
                   font-weight: ${boardType == 'qna' ? 'bold' : 'normal'};">Q&A 게시판</a>
            </p>
            <p>
                <a href="<%= request.getContextPath() %>/board/list.do?type=data" 
                   style="display: block; padding: 5px; text-decoration: none; color: 
                   ${boardType == 'data' ? 'white' : '#004d99'}; background-color: 
                   ${boardType == 'data' ? '#007bff' : 'transparent'}; 
                   font-weight: ${boardType == 'data' ? 'bold' : 'normal'};">자료실 게시판</a>
            </p>
            
            <div class="menu-separator">
                <c:choose>
                    <c:when test="${not empty sessionScope.userID}">
                        <h3 style="color: #004d99;">내가 쓴 글 (${boardType})</h3>
                        <div style="font-size: 14px; margin-top: 10px;">
                            <p>ㄴ [제목 L1] (TODO)</p>
                            <p>ㄴ [제목 L2] (TODO)</p>
                            <p>ㄴ [제목 L3] (TODO)</p>
                            <p style="margin-top: 15px; text-align: right;">
                               <a href="#" style="color: #007bff; text-decoration: none; font-size: 12px;">더 보기 &gt;</a>
                            </p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <h3 style="color: #004d99;">계정 로그인</h3>
                        <p style="font-size: 14px; margin-top: 10px;">
                           로그인 하시면 내가 쓴 글 목록을 볼 수 있습니다.
                        </p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <div class="main-content">
            
            <h2 style="text-align: center; border-bottom: 2px solid #ccc; padding-bottom: 10px;">
                ${boardType == 'free' ? '자유 게시판' : boardType == 'qna' ? 'Q&A 게시판' : '자료실 게시판'}
            </h2>
            
            <form action="<%= request.getContextPath() %>/board/list.do" method="get" style="text-align: center; margin-bottom: 15px;">
                <input type="hidden" name="type" value="${boardType}"> 
                <input type="text" name="search" placeholder="제목 검색" value="${param.search}" style="padding: 5px; border: 1px solid #007bff;">
                <button type="submit" style="padding: 5px 10px; background-color: #007bff; color: white; border: none;">검색</button>
            </form>
            
            <div style="min-height: 400px; padding: 10px;">
                
                <table class="board-table">
                    <thead>
                        <tr>
                            <th style="width: 10%;">번호</th>
                            <th style="width: 50%;">제목</th>
                            <th style="width: 15%;">작성자</th>
                            <th class="pc-only" style="width: 10%;">조회</th>
                            <th class="pc-only" style="width: 15%;">날짜</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty requestScope.boardList}">
                                <c:forEach var="board" items="${requestScope.boardList}">
                                    <tr>
                                        <td>${board.num}</td>
                                        <td class="subject-link">
                                            <a href="<%= request.getContextPath() %>/board/content.do?num=${board.num}&type=${boardType}" style="text-decoration: none; color: #333;">
                                                ${board.subject}
                                            </a>
                                        </td>
                                        <td>${board.writer}</td>
                                        <td class="pc-only">${board.readcount}</td>
                                        <td class="pc-only"><fmt:formatDate value="${board.regdate}" pattern="yy.MM.dd"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" style="padding: 20px;">
                                        <c:if test="${not empty param.search}">
                                            검색 결과가 없습니다.
                                        </c:if>
                                        <c:if test="${empty param.search}">
                                            작성된 게시글이 없습니다.
                                        </c:if>
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
                
                <div style="text-align: right; margin-top: 10px;">
                    <a href="<%= request.getContextPath() %>/board/writeform.do?type=${boardType}" 
                       style="padding: 5px 10px; background-color: #ffc107; color: #333; text-decoration: none; border-radius: 3px; font-weight: bold;">
                       글쓰기
                    </a>
                </div>
            </div>
        </div>
        
        <div class="right-sidebar">
            <c:choose>
                <c:when test="${not empty sessionScope.userID}">
                    <h3 style="color: #004d99;">내가 쓴 글 (${boardType})</h3>
                    <div style="font-size: 14px; margin-top: 10px;">
                        <p>ㄴ [제목 L1] (TODO)</p>
                        <p>ㄴ [제목 L2] (TODO)</p>
                        <p>ㄴ [제목 L3] (TODO)</p>
                        <p style="margin-top: 15px; text-align: right;">
                           <a href="#" style="color: #007bff; text-decoration: none; font-size: 12px;">더 보기 &gt;</a>
                        </p>
                    </div>
                </c:when>
                <c:otherwise>
                    <h3 style="color: #004d99;">계정 로그인</h3>
                    <p>로그인 상태가 아니므로 표시되지 않습니다.</p>
                </c:otherwise>
            </c:choose>
        </div>
        
    </div>
    
</body>
</html>