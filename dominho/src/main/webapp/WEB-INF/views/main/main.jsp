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
<!-- <link rel="stylesheet" href="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.css">
-->

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
		<div class="main-container">
			<ul class="bxslider">
				<li><a href="#"><img src="https://cdn.dominos.co.kr/admin/upload/banner/20210308_HwQ23ROJ.jpg" alt="" title="이미지1" style="min-height:400px; overflow: hidden"></a></li>
				<li><a href="#"><img src="https://cdn.dominos.co.kr/admin/upload/banner/20210331_2H2SWL96.jpg" alt="" title="이미지2" style="min-height:400px; overflow: hidden"></a></li>
				<li><a href="#"><img src="https://cdn.dominos.co.kr/admin/upload/banner/20210412_W6Vq3D23.jpg" alt="" title="이미지3" style="min-height:400px; overflow: hidden"></a></li>
			</ul>
		</div>
		
	</div>
<footer id="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/bxslider.js"></script>
</body>
</html>