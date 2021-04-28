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

main h2 {
	text-align: center;
	color: #CA3D2A;
	font-weight: bold;
}

.box, .prices {
	border: 3px solid #0E191A;
	width: 100%;
	height: auto;
	display: flex;
	justify-content: space-around;
	align-items: center;
	margin-bottom: 10px;
	border-radius: 20px;
}

.prices {
	background: linear-gradient(125deg, #FCD6B8, #FF8E21, #CA3D2A);
	color: white;
	border:none;
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

h3 {
	float: center;
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		menu_sum();
	})
	function menu_sum() {
		var sum = 0;
		var count = $(".cbox").length;
		for (var i = 0; i < count; i++) {
			if ($(".cbox")[i].checked == true) {
				var temp=$(".cbox")[i].value.split(",")
				sum += parseInt(temp[0]);

			}
		}
		$("#total_sum").html("합계: " + sum + "원");
		console.log(sum)
	}
	function sendOrder() {
		var f = document.form;
		if ($("input:checkbox[name=cbox]:checked").length == 0) {
			alert('주문할 메뉴를 선택하세요')
			return false
		}
		return true

	}
</script>
</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>

	<main>
		<h3>장바구니</h3>


		<h2>${dataCount==0?"장바구니가 비었습니다":"장바구니 목록"}</h2>
		<hr>
		<form name="form" action="${pageContext.request.contextPath}/order/order.do" method="post" onsubmit="return sendOrder();">
			<c:forEach var="dto" items="${cartlist}">
				<div class="box">
					<input value="${dto.price},${dto.menuNum},${dto.quantity}" name="cbox" class="cbox" type="checkbox" checked="checked" onclick="menu_sum();">
					<img src="${pageContext.request.contextPath}/resource/images/dominho_logo.svg" alt="Card image cap" width="130px" height="130px">
					<p>${dto.menuName}× ${dto.quantity}</p>
					<p>총 ${dto.price}원</p>
					<input type="button" class="img-button" onclick="javascript:location.href='${pageContext.request.contextPath}/order/cart_delete.do?num=${dto.cartId}';">
				</div>
				
			</c:forEach>
			<div class="prices">
			
				<button type="submit" class="btn btn-danger btn-lg">주문하기</button>
				<div id="total_sum"></div>
			</div>
		</form>

	</main>






	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>
</body>
</html>