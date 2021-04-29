<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/layout2.css" type="text/css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resource/css/style2.css" type="text/css">
<style type="text/css">
.container {
	border: 3px solid #0E191A;
	text-align: center;
	width: 50%;
	border-radius: 10px;
	margin: 10px auto;
}

.box {
	border-bottom: 2px solid #adadad;
	margin: 10px auto;
	padding: 10px 10px;
	text-align: center;
}

h2 {
	color: #CA3D2A;
	font-weight: bold;
	text-align: center;
}
</style>


</head>
<body>
	<header id="header">
		<jsp:include page="/WEB-INF/views/layout/header.jsp"></jsp:include>
	</header>
	<br>
	<br>

	<c:if test="${fn:length(AllOrders) > 0}">
		<h2>주문이 완료되었습니다</h2>
	</c:if>
	<c:if test="${fn:length(AllOrders) == 0}">
		<h2>최근 주문 내역이 없습니다</h2>
	</c:if>
	<hr>
	<c:forEach var="order" items="${AllOrders}">
		<div class="container">
			<div class="box">
				<h4>주문번호</h4>
				<br> ${order.orderNum}
			</div>
			<div class="box">
				<h4>주문자 아이디</h4>
				<br>${order.userId}</div>
			<div class="box">
				<h4>주문 매장</h4>
				<br>${order.storeName}</div>
			<div class="box">
				<h4>주문 일시</h4>
				<br>${order.orderDate}</div>
			<div class="box">
				<h4>배달/포장</h4>
				<br>${order.isDelivery}</div>
			<c:if test="${fn:length(order.request) > 0}">
				<div class="box">
					<h4>요청사항</h4>
					<br>${order.request}</div>
			</c:if>

			<div class="box">
				<h4>총 결제금액(포장시에는 20% 할인된 금액이 적용되었습니다)</h4>
				<br>${order.totalPrice}</div>
			<div class="box">
				<h4>결제 카드</h4>
				<br>${order.cardNum}</div>

		</div>
	</c:forEach>
	<br>
	<h2 style="color: #084B27;">상세 주문 내역</h2>
	<c:forEach var="order" items="${AllOrderDetail}">
		<div class="container">

			<div class="box">
				<h4>주문번호</h4>
				<br>${order.orderNum}</div>
			<div class="box">
				<h4>메뉴이름</h4>
				<br>${order.menuName}</div>
			<div class="box">
				<h4>수량</h4>
				<br>${order.count}</div>
			<div class="box">
				<h4>총 금액</h4>
				<br>${order.orderPrice}</div>


		</div>
	</c:forEach>

	<footer id="footer">
		<jsp:include page="/WEB-INF/views/layout/footer.jsp"></jsp:include>
	</footer>
</body>
</html>