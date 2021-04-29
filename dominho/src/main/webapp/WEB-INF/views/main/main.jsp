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
<style type="text/css">
#ad-content ul, #ad-content span {
	
	list-style: none;
	display: flex;
	justify-content: center;
	font-size: 30px;
}
#ad-content div span {
	padding: 10px;
	border:2px solid black; 
	border-right:none;
	border-left:none;
}

#ad-content img {
	width:250px;
	float: left;
	margin: 30px;
}
.main-container {
	height: 500px;
}

</style>
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<div id="container">
		<div class="main-container">
			<ul class="bxslider">
				<li><a href="#"><img src="https://cdn.dominos.co.kr/admin/upload/banner/20210308_HwQ23ROJ.jpg" alt="" title="이미지1" style="min-height:400px; min-width:1300px; overflow: hidden"></a></li>
				<li><a href="#"><img src="https://cdn.dominos.co.kr/admin/upload/banner/20210331_2H2SWL96.jpg" alt="" title="이미지2" style="min-height:400px; min-width:1300px; overflow: hidden"></a></li>
				<li><a href="#"><img src="https://cdn.dominos.co.kr/admin/upload/banner/20210412_W6Vq3D23.jpg" alt="" title="이미지3" style="min-height:400px; min-width:1300px; overflow: hidden"></a></li>
			</ul>
		</div>
		<div id="ad-content">
			<div><span>진행 중인 EVENT</span></div>
			<ul>
				<li><img src="${pageContext.request.contextPath}/resource/images/서베이.jpg" style="width:500px"> </li>
				<li><img src="${pageContext.request.contextPath}/resource/images/sale.jpg" style="width:500px"></li>
			</ul>
			<ul style="justify-content: space-around;">
				<li><a style="font-size: 20px;"> 도민호 피자에게 여러분의 의견을!</a></li>
				<li><a style="font-size: 20px;"> 5월의 피자는 사랑을 싣고~♡</a></li>
			</ul>
			
			<div style="background-color:#eee; margin:30px 0; "><span style="border:none;">With Dominho</span></div>
			
			<ul>
				<li> <img src="${pageContext.request.contextPath}/resource/images/대외활동.png"> </li>
				<li> <img src="${pageContext.request.contextPath}/resource/images/도민호.png"> </li>
				<li> <img src="${pageContext.request.contextPath}/resource/images/에코.png"> </li>
			</ul>
			<ul>
				<li><a style="font-size: 20px; margin: 0 30px;"> 도민호 캐릭터를 만들어주세요!</a></li>
				<li><a style="font-size: 20px; margin: 0 30px;"> 도민호를 운영할 도.민.호를 찾아라!</a></li>
				<li><a style="font-size: 20px; margin: 0 30px;"> 환경을 생각한다면, 도민호 피자 </a></li>
			
			</ul>
		
		</div>
	</div>
<footer id="footer">
	<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
</footer>
<script type="text/javascript" src="${pageContext.request.contextPath}/resource/jquery/bxslider.js"></script>
</body>
</html>