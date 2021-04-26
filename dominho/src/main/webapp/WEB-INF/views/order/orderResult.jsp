<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
</head>
<body>
주문완료
<c:forEach var="order" items="${AllOrders}">
<div style="border: 3px solid #FF8E21; border-radius: 10px;">
${order.orderNum}
${order.userId}
${order.storeName}
${order.orderDate}
${order.isDelivery}
${order.totalPrice}
${order.cardNum}
${order.menuName}
${order.orderCount}
${order.orderPrice}
</div>
</c:forEach>


</body>
</html>