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

<title>Dominho Pizza!</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<div id="container">
		<div class="main-container">
			<div class="main-content">
				<p>여기에 신메뉴나 할인 내용 대충 넣어놓으면 어떨까용</p>
				<p>이건 참고용으로 색깔 넣어놨어요,,,^.^</p>
			</div>
		</div>
	</div>
<footer id="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>
</body>
</html>