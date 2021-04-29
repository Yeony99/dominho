<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/jquery/jquery.bxslider.css">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script src="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.min.js"></script>
<title>Dominho Pizza!</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<div id="container">
		<div style="width: 700px; margin: 30px auto;">
			<table style="width: 100%; border-spacing: 0;">
			<tr height="45">
				<td align="left" class="title">
					<h3><span>|</span> 매장 리스트</h3>
				</td>
			</tr>
			</table>
			
			<table style="width: 100%; margin-top: 20px; border-spacing: 0;">
			   <tr height="35">
			      <td align="left" width="50%">
			          ${dataCount}개(${page}/${total_page} 페이지)
			      </td>
			      <td align="right">
			          &nbsp;
			      </td>
			   </tr>
			</table>
			
			<table style="width: 100%; border-spacing: 0; border-collapse: collapse;">
			  <tr align="center" bgcolor="#eeeeee" height="35" style="border-top: 1px solid #cccccc; border-bottom: 1px solid #cccccc;"> 
			      <th width="60" style="color: #787878;">No</th>
			      <th width="60" style="color: #787878;">매장명</th>
			      <th width="80" style="color: #787878;">매장 번호</th>
			      <th style="color: #787878;">매장 주소</th>
			      <th width="80" style="color: #787878;">오픈시간</th>
			      <th width="80" style="color: #787878;">마감시간</th>
			  </tr>
			  
			 <c:forEach var="dto" items="${list}">
			  <tr align="center" height="35" style="border-bottom: 1px solid #cccccc;"> 
			      <td>${dto.listNum}</td>
			      <td align="left" style="padding-left: 10px;">
			           <a href="${articleUrl}&storeNum=${dto.storeNum}">${dto.storeName}</a>
			      </td>
			      <td>${dto.storeTel}</td>
			      <td>${dto.storeAddress}</td>
			      <td>${dto.openingHours}</td>
			      <td>${dto.closingHours}</td>
			  </tr>
			</c:forEach>
			</table>  
			  
			  <table style="width: 100%; border-spacing: 0;">
			   <tr height="35">
				<td align="center">
			        ${dataCount==0?"등록된 게시물이 없습니다.":paging}
				</td>
			   </tr>
			</table>
			
			<table style="width: 100%; margin-top: 10px; border-spacing: 0;">
			   <tr height="40">
			      <td align="left" width="100">
			          <button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/stire/list.do';">새로고침</button>
			      </td>
			      <td align="center">
			          <form name="searchForm" action="${pageContext.request.contextPath}/store/list.do" method="post">
			              <select name="condition" class="selectField">
			                  <option value="storeName">매장명</option>
			                  <option value="storeTel">매장 번호</option>
			                  <option value="storeAddress">매장 주소</option>
			                  <option value="openingHours">오픈 시간</option>
			                  <option value="closingHours">마감 시간</option>
			            </select>
			            <input type="text" name="keyword" class="boxTF">
			            <button type="button" class="btn" onclick="searchList()">검색</button>
			        </form>
			      </td>
			      <td align="right" width="100">
					<button type="button" onclick="javascript:location.href='${pageContext.request.contextPath}/store/create.do';"> 상점등록 </button>
			      </td>
			   </tr>
			</table>
	
		</div>
	</div>
	
	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>

</body>
</html>