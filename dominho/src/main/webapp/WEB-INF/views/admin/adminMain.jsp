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
<title>관리자모드- 도민호피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">
.btn{
	width: 150px;
	height: 50px;
	font-size: medium;
	border-radius: 0;
	background-color: #424242;
	color: white;
	cursor: pointer;
	border: none;
}
.txt
{
	font-size: 30px;
	color: #111;
	font-weight: 200;

}
</style>
</head>

<body>
<div class="header">
<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
</div>

<div class="txt">관리모드</div>

<div>
<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/admin_Member/memberList';">회원정보</button>
<button type="button" class="btn" onclick="javascript:location.href='${pageContext.request.contextPath}/';">주문내역</button>
</div>

<div class="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</div>

</body>

</html>