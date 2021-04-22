<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="header-top">
	<div class="header-left">
		<p>
			<a href="${pageContext.request.contextPath}/" class="logo"> 
				<img alt="" src="${pageContext.request.contextPath}/resource/images/dominho_logo.svg" style="width:200px">
			</a>
		</p>
	</div>
	<div class="header-right">
		<div style="padding-top: 20px; float: right;">
			<c:if test="${empty sessionScope.member}">
				<a href="${pageContext.request.contextPath}/member/login.do">로그인</a>
                    &nbsp;|&nbsp;
                <a href="${pageContext.request.contextPath}/member/member.do">회원가입</a>
			</c:if>
			<c:if test="${not empty sessionScope.member}">
				<span style="color: blue;">${sessionScope.member.userName}</span>님
                    &nbsp;|&nbsp;
                   <a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a>
                 
			</c:if>
		</div>
	</div>
</div>

<div class="menu">
    <ul class="nav">
    	<li>
            <span class="un"><a href="#">메뉴</a></span>
        </li>
        <li>
            <span class="un"><a href="#">매장</a></span>
        </li>
			
        <li>
            <span class="un"><a href="#">공지사항</a></span>
        </li>

        
        <li>
            <span class="un"><a href="#">마이페이지</a></span>
            <!-- <ul class="submenu">
                <li><a href="#">주문현황조회</a></li>
                <li><a href="#">내정보변경</a></li>
                <li><a href="#">회원탈퇴</a></li>
            </ul>
             -->
        </li>
        
    </ul>      
</div>