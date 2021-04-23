<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<title>Dominho Pizza | 장바구니</title>
<style type="text/css">
main {
	width: 80%;
	margin: 10px auto;
}

.box {
	border: 3px solid #FF8E21;
	width: 100%;
	height: auto;
	display: flex;
	justify-content: space-around;
	align-items: center;
	margin-bottom: 10px;
	border-radius: 10px;
}

.img-button {
	background:
		url( "${pageContext.request.contextPath}/resource/images/bin.png" )
		no-repeat;
	width: 30px;
	height: 30px;
	border: none;
	cursor: pointer;
}
</style>
<script type="text/javascript">
$(document).ready(function(){
	menu_sum();
})
function menu_sum() {
	var sum=0;
	var count=$(".cbox").length;
	for(var i=0;i<count;i++){
		if($(".cbox")[i].checked==true){
			sum+=parseInt($(".cbox")[i].value);
		}
	}
	$("#total_sum").html(sum+"원");
	console.log(sum)
}
</script>
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<main>
		<h3>장바구니</h3>

		
		<form name="form">
		
		<div class="box">
			<input  class="cbox" value="15000" type="checkbox"  onclick="menu_sum();" checked="checked">
			<img src="${pageContext.request.contextPath}/resource/images/dominho_logo.svg" alt="Card image cap" width="130px" height="130px">
			<p>콤피네이션피자</p>
			<p>15000원</p>
			<input type="button" class="img-button">
			
		</div>


		<div class="box">
			<input class="cbox"  value="15000" type="checkbox"  onclick="menu_sum();" checked="checked">
			<img src="${pageContext.request.contextPath}/resource/images/dominho_logo.svg" alt="Card image cap" width="130px" height="130px">
			<p>콤피네이션피자</p>
			<p>15000원</p>
			<input type="button" class="img-button">

		</div>
	합계:<div id="total_sum" ></div>
		</form>
		 
<!-- 
<c:if test="empty ${cartlist}">
			<h3>장바구니가 비었습니다</h3>
		</c:if>
		
		<form name="form">
		<c:forEach var="dto" items="${cartlist}">
			<div class="box">
				<input value="${dto.price}" class="cbox" type="checkbox" checked="checked" onclick="menu_sum();">
				<img src="${pageContext.request.contextPath}/uploads/photo/${dto.imageFilename}" alt="Card image cap" width="130px" height="130px">
				<p>${dto.menuName}</p>
				<p>${dto.price}원</p>
				<input type="button" class="img-button">
			</div>
		</c:forEach>
		<div id="total_sum" ></div>
		</form>
		
		
 -->
		
		
 

	</main>






	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>
</body>
</html>