<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/layout2.css"
	type="text/css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/resource/css/style2.css"
	type="text/css">
<title>마이페이지 - 도민호 피자</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<style type="text/css">

</style>
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>
	<h3>myPage</h3>
	<div id="container">
		<jsp:include page="/WEB-INF/views/layout/myPageMenu.jsp"></jsp:include>
	</div>
	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>
</body>
</html>