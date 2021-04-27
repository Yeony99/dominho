<%@ page contentType="text/html; charset=UTF-8" %>
<%@page trimDirectiveWhitespaces="true"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<title>고객정보- 도민호피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script type="text/javascript">
function listSearch() {
    var f = document.listSearchForm;
    f.submit();
 }
 
</script>

<style type="text/css">
.body_title span {
	font-size: 30px;
	color: #111;
	font-weight: 200;
}

.container {
	padding: 30px 0px 0px 30px;
}
.allDataCount {
	width: 100%;
	margin: 20px auto 0px;
	border-spacing: 0px;
	border-bottom: 1.5px solid #111;
	font-size: small;
	padding-bottom: 5px;
}
.boxTFdiv {
	float: left;
	margin-top: 53px;
	height: 47px;
}
.boxTF {
	width: 425px;
	height: 43px;
	padding-left: 15px;
	border: 1px solid #ddd;
	color: #888888;
	float: left;
	margin-right: 5px;
	
}

.btnSearch {
	width: 43px;
	height: 43px;
	background-color: white;
	border: 1px solid #ddd;
	cursor: pointer;

}
</style>
</head>
<body>

<div class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="container">
	<div class="body_con" style="width: 1200px;">
		<div class="body_title">
			<span>고객정보</span>
		</div>
		
			<table style="width: 100%; height:120px; margin: 30px auto; border-spacing: 0px;">
				<tr>
					<td align="center" style=" width:100%; border-top: 2px solid #111;">
					<form name="listSearchForm" action="${pageContext.request.contextPath}/admin/memberList" method="post">
						<span>이름검색</span>
						<div class="boxTFdiv">
						<input type="text" name="keyword" class="boxTF" value="${keyword}">
						<button type="button" class="btnSearch" onclick="listSearch()"><img alt="" src="${pageContext.request.contextPath}/resource/images/notice_search.png" style="padding-top: 5px;"></button>
						</div>
					</form>
				</td>
			</tr>
		</table>
		
		<div>
			<form>
			<table class="allDataCount">
				<tr height="20">
					<td align="left" width="50%">
						총 ${dataCount}명
					</td>
				</tr>
			</table>
			
			<table style="width: 100%; margin: 0px auto; border-spacing: 0px; border-collapse: collapse;">
				<tr align="center" height="55">
					<th width="120">번호</th>
					<th width="180">아이디</th>
					<th width="250">이름</th>
					<th width="325">연락처</th>
					<th>E-mail</th>
				</tr>
				
				<c:forEach var="dto" items="${listMember}">
					<tr align="center" height="55" style="border-bottom: 1px solid #ddd;">
						<td align="left" style="padding: 20px 0px 20px 10px;">
							<a href="${detailUrl}">${dto.userId}</a>
						</td>
						<td>{dto.userName}</td>						
						<td>${dto.tel}</td>
						<td>${dto.birth}</td>
						<td>${dto.email}</td>						
				</c:forEach>
				
				<c:forEach var="dto" items="${list}">
					<tr align="center" height="55" style="border-bottom: 1px solid #ddd;">
							<td>${dto.userId}</td>
							<td align="left" style="padding-left: 10px;">
								<a href="${detailUrl}">${dto.userName}</a>
							</td>
							<td>${dto.tel}</td>
							<td>${dto.birth}</td>
							<td>${dto.email}</td>						
					</tr>
				</c:forEach>
			</table>
		</form>
		
			
		</div>
	</div>
</div>

<div class="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>
</body>
</html> 